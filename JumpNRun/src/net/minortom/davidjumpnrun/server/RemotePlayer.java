/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import jumpnrun.Gun;
import jumpnrun.Pitchfork;
import jumpnrun.Protagonist;
import net.minortom.davidjumpnrun.netcode.ServerCommand;
import worldeditor.Block;

public class RemotePlayer extends Protagonist implements Runnable {

    public boolean udpConnected;

    Server server;
    OnlGame game;

    public String pubId;
    public String skin;     //Skin fileName
    public String name;
    public int index; //Number >= 0
    private int animationStateAsInt;

    private long startTime, now, oldTime, timeElapsed;
    private double timeElapsedSeconds;

    private boolean intersectsPlayer = false;

    private RemoteObject remotePitchfork, remoteGun;

    private static final double fpsLimit = 60;

    public RemotePlayer(Server server, OnlGame game, String pubId, String skin, String name, int index, int maxPlayer) {
        super(index, (game.worldWidth / (maxPlayer + 1)) * (index + 1), OnlGame.spawnY);
        this.server = server;
        this.game = game;
        this.pubId = pubId;
        this.skin = skin;
        this.name = name;
        this.index = index;
        accPerSec = 1000;
        animationStateAsInt = currCostume.ordinal();
        shootDoing = false;
        hitDoing = false;
        remotePitchfork = new RemoteObject(Pitchfork.AnimationState.LEFT.getRect());
        remoteGun = new RemoteObject(Gun.AnimationState.LEFT.getRect());
    }

    @Override
    public void run() {
        now = System.nanoTime();
        startTime = now;
        oldTime = now;
        timeElapsed = 0;
        while (true) {
            now = System.nanoTime();
            timeElapsed = now - oldTime;
            oldTime = now;
            timeElapsedSeconds = timeElapsed / (1000.0d * 1000.0d * 1000.0d);
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                Logger.getLogger(RemotePlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
            update();
            // game.sendAllTCP(server.keyword + server.infoSeperator + "OGAME-UPDATEPROT" + server.infoSeperator + pubId + server.infoSeperator + String.valueOf(xPos) + server.infoSeperator + String.valueOf(yPos) + server.infoSeperator + String.valueOf(animationStateAsInt));
            // game.sendAllTCP(ServerCommand.OGAME_UPDATEPROT, new String[]{pubId, String.valueOf(xPos), String.valueOf(yPos), String.valueOf(animationStateAsInt)});

            //server.tcpServer.get(pubId).out.println(server.keyword + server.infoSeperator + "OGAME-UPDATEPROT" + server.infoSeperator + pubId + server.infoSeperator + String.valueOf(xPos) + server.infoSeperator + String.valueOf(yPos) + server.infoSeperator + String.valueOf(animationStateAsInt));
        }
    }

    public void update() {
        if (yPos > 5000) {
            hitten();
        }

        if (!respawnDoing) {
            updateJump(timeElapsedSeconds);
            if (((!goesRight) && (!goesLeft)) && (ySpeed == 0)) {
                if (xSpeed != 0) {

                    xSpeed -= (xSpeed / 10);
                    xSpeed = 0;
                    resetAnimation();
                }
            }

            if (!shootDoing) {
                if (goesRight) {
                    xSpeed = defaultXSpeed;
                }
                if (goesLeft) {
                    xSpeed = -1 * defaultXSpeed;
                }
            }

            intersects = false;
            ySpeed += timeElapsedSeconds * accPerSec;
            yPos += ySpeed * timeElapsedSeconds;
            setX(xPos);
            setY(yPos);

            intersects = collisionCheck(game.worldVector, game.players);

            if (intersects) {
                yPos -= timeElapsedSeconds * ySpeed;
                ySpeed = 0;
                if (!isMachinePistol) {
                    updateAnimation(timeElapsedSeconds);
                }
                intersects = collisionCheck(game.worldVector, game.players);
                if (intersects) {
                    yPos -= 50;
                }
            }

            xPos += xSpeed * spdFactor * timeElapsedSeconds;
            setX(xPos);
            setY(yPos);

            intersects = collisionCheck(game.worldVector, game.players);
            if (intersects) {
                xPos -= xSpeed * spdFactor * timeElapsedSeconds;
                xSpeed = 0;
                //resetAnimation();
            }
            setX(xPos);
            setY(yPos);

            intersects = intersectsPlayer(game.players);
            if (intersects) {
                //yPos -= height + 5;
            }

            setX(xPos);
            setY(yPos);

        } else {
            updateRespawn(timeElapsedSeconds);

        }
        animationStateAsInt = currCostume.ordinal();
    }

    public double getXPos() {
        return xPos;
    }
    
    public double getYPos() {
        return yPos;
    }
    
    public int getAnimationStateAsInt() {
        return animationStateAsInt;
    }

    public boolean intersectsPlayer(HashMap<String, RemotePlayer> players) {
        intersectsPlayer = false;
        players.forEach((id, player) -> {
            if (!id.equals(pubId)) {
                if (intersectsPlayer(players.get(id))) {
                    intersectsPlayer = true;
                }
            }
        });

        return intersectsPlayer;
    }

    public boolean collisionCheck(Vector<Vector<Block>> worldVec, HashMap<String, RemotePlayer> players) {
        double blockSize = game.blockSize;
        for (int i = 0; i < worldVec.size(); i++) {
            for (int j = 0; j < worldVec.get(i).size(); j++) {
                if (worldVec.get(i).get(j) != null) {
                    Block b = worldVec.get(i).get(j);
                    if (b.getIsSolid()) {
                        if (intersects(xPos, yPos, width, height, i * blockSize, j * blockSize, blockSize, blockSize)) {

                            return true;
                        }
                    }
                }
            }
        }
        if (intersectsPlayer(players)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean intersectsPlayer(Protagonist p) {
        if (intersects(xPos, yPos, width, height, p.getX(), p.getY(), width, height)) {
            if (!p.isRespawning()) {

                return true;
            }
        }
        return false;

    }

    public boolean intersects(double x1, double y1, double w1, double h1, double x2, double y2, double w2, double h2) {
        double endX1 = x1 + w1;
        double endY1 = y1 + h1;
        double endX2 = x2 + w2;
        double endY2 = y2 + h2;

        if (x2 > endX1
                || endX2 < x1
                || y2 > endY1
                || endY2 < y2) {
            return false;
        }
        return true;
    }

    void initClientOtherPlayer(RemotePlayer p2) {
        server.tcpServer.get(pubId).getCommandHandler().sendCommand(ServerCommand.OGAME_INITPROT, new String[]{p2.name, p2.skin, String.valueOf(p2.index), p2.pubId, "0"});
        // server.tcpServer.get(pubId).out.println(server.keyword + server.infoSeperator + "OGAME-INITPROT" + server.infoSeperator + p2.name + server.infoSeperator + p2.skin + server.infoSeperator + String.valueOf(p2.index) + server.infoSeperator + p2.pubId + server.infoSeperator + "0");
    }

    void initClientPendant(RemotePlayer p2) {
        server.tcpServer.get(pubId).getCommandHandler().sendCommand(ServerCommand.OGAME_INITPROT, new String[]{p2.name, p2.skin, String.valueOf(p2.index), p2.pubId, "1"});
        //server.tcpServer.get(pubId).out.println(server.keyword + server.infoSeperator + "OGAME-INITPROT" + server.infoSeperator + p2.name + server.infoSeperator + p2.skin + server.infoSeperator + String.valueOf(p2.index) + server.infoSeperator + p2.pubId + server.infoSeperator + "1");
    }

    void handleKeyPress(String action) {
        System.out.println("Keypress!"); /////!!!!!!!!!!!!!!!!!!!!
        switch (action.toUpperCase()) {
            case "LEFT":
                doLeft();
                break;
            case "RIGHT":
                doRight();
                break;
            case "JUMP":
                doJump();
                break;
            case "HIT":
                doHit();
                break;
            case "SHOOT":
                doShoot();
                break;
            case "USE":
                doUse();
                break;
        }
    }

    void handleKeyRelease(String action) {
        switch (action.toUpperCase()) {
            case "LEFT":
                releaseLeft();
                break;
            case "RIGHT":
                releaseRight();
                break;
        }
    }

    @Override
    public void doRight() {
        isFacingRight = true;
        goesRight = true;
        goesLeft = false;
    }

    @Override
    public void doLeft() {
        isFacingRight = false;
        goesLeft = true;
        goesRight = false;
    }

    @Override
    public void doJump() {
        if (ySpeed == 0) {
            jumpDone = true;
        }
    }

    @Override
    public void updateJump(double timeElapsedSeconds) {
        if (!jumpDone) {
            jumpTimer = 0;
        } else {
            if (remotePitchfork.getAnimationState() == 0) {
                forkAnimationXPosAdd = -5;
            } else if (remotePitchfork.getAnimationState() == 1) {
                forkAnimationXPosAdd = -10;
            }

            jumpTimer += timeElapsedSeconds;
            if (jumpTimer < 0.2) {
                if (goesLeft) {
                    setAnimationState(CostumeViewport.LEFT_JUMP);
                } else if (goesRight) {
                    setAnimationState(CostumeViewport.RIGHT_JUMP);
                } else {
                    setAnimationState(CostumeViewport.MID_JUMP);
                }
            } else {
                if (currCostume == CostumeViewport.LEFT_JUMP) {
                    setAnimationState(CostumeViewport.LEFT_0);
                } else if (currCostume == CostumeViewport.RIGHT_JUMP) {
                    setAnimationState(CostumeViewport.RIGHT_0);
                } else {
                    setAnimationState(CostumeViewport.MID);
                }
                jumpDone = false;
                ySpeed = -1 * (game.blockSize * 10.5);
            }
        }
    }

    @Override
    public void updateHit(double timeElapsedSeconds, Protagonist otherProt) {
        hitTimer += timeElapsedSeconds;
        if (remotePitchfork.getAnimationState() == 0) {
            remotePitchfork.setX(xPos - 40); //- forkAnimationXPosAdd);
            setAnimationState(CostumeViewport.LEFT_HIT);
        } else if (remotePitchfork.getAnimationState() == 1) {
            remotePitchfork.setX(xPos + 30); //+ forkAnimationXPosAdd);
            setAnimationState(CostumeViewport.RIGHT_HIT);
        }
        remotePitchfork.setY(yPos + 55);
        game.players.forEach((id, p) -> {
            if (!id.equals(pubId)) {
                if (intersects(remotePitchfork.getX(), remotePitchfork.getY(), remotePitchfork.getWidth(), remotePitchfork.getHeight(), p.getX(), p.getY(), width, height)) {
                    p.hitten();
                }
            }
        });

        if (hitTimer > 0.3) {
            hitDoing = false;
            remotePitchfork.setAnimationState(-1);
            hitTimer = 0;
            setAnimationState(CostumeViewport.MID);

        }
    }

    @Override
    public void updateShoot(double timeElapsedSeconds, Protagonist otherProt) {
        /*
        shootTimer += timeElapsedSeconds;
        gun.setVisible(true);
        if (gun.getFacingLeft()) {
            gun.setX(getX() - 20); //- forkAnimationXPosAdd);
            setAnimationState(CostumeViewport.LEFT_SHOOT);
        } else {
            gun.setX(getX() + 5); //+ forkAnimationXPosAdd);
            setAnimationState(CostumeViewport.RIGHT_SHOOT);
        }
        gun.setY(getY() + 22); ///
        gun.updateShoot(shootTimer);

        if (shootTimer > 2) {
            shootDoing = false;
            gun.setVisible(false);
            shootTimer = 0;
            setAnimationState(CostumeViewport.MID);

        }
         */
    }

}
