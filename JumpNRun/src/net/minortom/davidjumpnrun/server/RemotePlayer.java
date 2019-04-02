/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

import java.util.HashMap;
import java.util.Vector;
import javafx.scene.input.KeyCode;
import jumpnrun.Protagonist;
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

    public RemotePlayer(Server server, OnlGame game, String pubId, String skin, String name, int index, int maxPlayer) {
        super(index, (game.worldWidth/(maxPlayer+1)) * (index+1), OnlGame.spawnY);
        this.server = server;
        this.game = game;
        this.pubId = pubId;
        this.skin = skin;
        this.name = name;
        this.index = index;
        accPerSec = 10;
        animationStateAsInt = currCostume.ordinal();
        shootDoing = false;
        hitDoing = false;
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

            update();
            server.tcpServer.get(pubId).out.println(server.keyword + server.infoSeperator + "OGAME-UPDATEPROT" + server.infoSeperator + pubId + server.infoSeperator + String.valueOf(xPos) + server.infoSeperator + String.valueOf(yPos) + server.infoSeperator + String.valueOf(animationStateAsInt));
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

                    // xSpeed -= (xSpeed / 10);
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
        server.tcpServer.get(pubId).out.println(server.keyword + server.infoSeperator + "OGAME-INITPROT" + server.infoSeperator + p2.name + server.infoSeperator + p2.skin + server.infoSeperator + String.valueOf(p2.index) + server.infoSeperator + p2.pubId + server.infoSeperator + "0");
    }

    void initClientPendant(RemotePlayer p2) {
        server.tcpServer.get(pubId).out.println(server.keyword + server.infoSeperator + "OGAME-INITPROT" + server.infoSeperator + p2.name + server.infoSeperator + p2.skin + server.infoSeperator + String.valueOf(p2.index) + server.infoSeperator + p2.pubId + server.infoSeperator + "1");
    }

    void handleKeyPress(String action) {
        System.out.println("Keypress!"); /////!!!!!!!!!!!!!!!!!!!!
        switch(action.toUpperCase()) {
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
        switch(action.toUpperCase()) {
            case "LEFT":
                releaseLeft();
                break;
            case "RIGHT":
                releaseRight();
                break;
        }
    }

}
