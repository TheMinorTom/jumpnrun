/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import jumpnrun.Gun;
import jumpnrun.JumpNRun;
import jumpnrun.JumpNRun.Gamemode;
import jumpnrun.Pitchfork;
import jumpnrun.Protagonist;
import jumpnrun.Shoot;
import net.minortom.davidjumpnrun.netcode.GameObjectType;
import net.minortom.davidjumpnrun.netcode.ServerCommand;
import worldeditor.Block;

public class RemotePlayer extends Protagonist implements Runnable, OnlineGameObject {

    public static final double ySpectatorSpeed = 500;
    public static final double xSpectatorSpeed = 500;

    public boolean udpConnected;

    Server server;
    OnlGame game;

    private boolean isInited = false;

    public String pubId;
    public String userId;
    public String skin;     //Skin fileName
    public String name;
    public int index; //Number >= 0
    private int animationStateAsInt;

    private long startTime, now, oldTime, timeElapsed;
    private double timeElapsedSeconds;

    private boolean intersectsPlayer = false;

    private RemoteObject remotePitchfork, remoteGun, remoteRespawnTimer, remoteGameTimer;

    private final GameObjectType objectType = GameObjectType.PROTAGONIST;

    private final String objectId;

    private boolean shootGenerated = false;

    private OnlineCounterLabel respawnLabel;

    private ObservableList<ServerCommand> serverCommandsToSend;
    private ObservableList<String[]> argsToSend;

    private int kills, deaths;

    private OnlineCounterLabel killCounter, deathCounter;

    private ObservableList<OnlineCounterLabel> thisCounterLabels;

    private boolean isDead;

    private boolean isUp, isDown;

    private boolean endGame = false;

    private String placement = "";
    
    private int coinsCollected;

    public RemotePlayer(Server server, OnlGame game, String pubId, String objectId, String skin, String name, int index, int maxPlayer, String userId) {
        super(index, (game.worldWidth / (maxPlayer + 1)) * (index + 1), OnlGame.spawnY);
        this.server = server;
        this.game = game;
        this.pubId = pubId;
        this.userId = userId;
        this.skin = skin;
        this.name = name;
        this.index = index;
        this.objectId = objectId;
        accPerSec = 1000;
        animationStateAsInt = currCostume.ordinal();
        remotePitchfork = new RemoteObject(Pitchfork.AnimationState.LEFT.getRect(), GameObjectType.PITCHFORK, game.nextObjectId());
        remotePitchfork.setAnimationState(-1);
        remoteGun = new RemoteObject(Gun.AnimationState.LEFT.getRect(), GameObjectType.GUN, game.nextObjectId());
        remoteGun.setAnimationState(-1);
        remoteRespawnTimer = new RemoteObject(0, 0, 0, 0, GameObjectType.RESPAWNTIMER, game.nextObjectId());
        game.onlineGameObjects.put(remotePitchfork.getObjectId(), remotePitchfork);
        game.onlineGameObjects.put(remoteGun.getObjectId(), remoteGun);
        //game.onlineGameObjects.put(remoteRespawnTimer.getObjectId(), remoteRespawnTimer);
        respawnLabel = new OnlineCounterLabel(game.nextObjectId(), GameObjectType.RESPAWNTIMER, -1, xSpawn + width / 2, ySpawn, game);

        serverCommandsToSend = FXCollections.observableArrayList();
        argsToSend = FXCollections.observableArrayList();
        thisCounterLabels = FXCollections.observableArrayList();
        kills = 0;
        deaths = 0;

        killCounter = new OnlineCounterLabel(game.nextObjectId(), GameObjectType.KILLCOUNT, 0, game);
        if (game.gamemode.equals(Gamemode.DEATHS)) {
            deathCounter = new OnlineCounterLabel(game.nextObjectId(), GameObjectType.DEATHCOUNT, game.respawnLimit, game);
        } else {
            deathCounter = new OnlineCounterLabel(game.nextObjectId(), GameObjectType.DEATHCOUNT, 0, game);
        }
        thisCounterLabels.addAll(killCounter, deathCounter);
        isDead = false;
        isUp = false;
        isDown = false;
        coinsCollected = 0;

    }

    @Override
    public void run() {
        ObservableList<String[]> objectsUpdateArgs = FXCollections.observableArrayList();
        HashMap<String, OnlineGameObject> gameObjects;
        while (!endGame) {
            objectsUpdateArgs.clear();
            gameObjects = new HashMap<>(game.onlineGameObjects);
            /*
             game.players.forEach((id, player)->{
             server.tcpServer.get(pubId).getCommandHandler().sendCommand(ServerCommand.OGAME_UPDATEPROT, new String[]{player.pubId, String.valueOf(player.getXPos()), String.valueOf(player.getYPos()), String.valueOf(player.getAnimationStateAsInt())});
             });
             */
            gameObjects.forEach((String id, OnlineGameObject o) -> {
                //server.tcpServer.get(pubId).getCommandHandler().sendCommand(ServerCommand.OGAME_UPDATEOBJECT, new String[]{o.getObjectId(), String.valueOf(o.getObjectTypeAsInt()), String.valueOf((float)o.getXPos()), String.valueOf((float)o.getYPos()), String.valueOf(o.getAnimationStateAsInt())});
                objectsUpdateArgs.add(new String[]{o.getObjectId(), String.valueOf(o.getObjectTypeAsInt()), String.valueOf((float) o.getXPos()), String.valueOf((float) o.getYPos()), String.valueOf(o.getAnimationStateAsInt())});
            });
            game.getCounterLabels().forEach((OnlineCounterLabel currCounter) -> {
                if (currCounter.needsUpdate()) {
                    objectsUpdateArgs.add(new String[]{currCounter.getObjectId(), currCounter.getTypeString(), String.valueOf(currCounter.getXPos()), String.valueOf(currCounter.getYPos()), currCounter.getValIntString()});
                }

            });
            thisCounterLabels.forEach((OnlineCounterLabel currCounter) -> {
                if (currCounter.needsUpdate()) {
                    objectsUpdateArgs.add(new String[]{currCounter.getObjectId(), currCounter.getTypeString(), String.valueOf(currCounter.getXPos()), String.valueOf(currCounter.getYPos()), currCounter.getValIntString()});
                }

            });
            server.tcpServer.get(pubId).getCommandHandler().sendUpdateCommand(objectsUpdateArgs);
            try {
                Thread.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (serverCommandsToSend.size() > 0) {
                for (int i = 0; i < serverCommandsToSend.size(); i++) {
                    server.tcpServer.get(pubId).getCommandHandler().sendCommand(serverCommandsToSend.get(i), argsToSend.get(i));
                }
                serverCommandsToSend.clear();
                argsToSend.clear();
            }
            /*
             update();
             // game.sendAllTCP(server.keyword + server.infoSeperator + "OGAME-UPDATEPROT" + server.infoSeperator + pubId + server.infoSeperator + String.valueOf(xPos) + server.infoSeperator + String.valueOf(yPos) + server.infoSeperator + String.valueOf(animationStateAsInt));
             // game.sendAllTCP(ServerCommand.OGAME_UPDATEPROT, new String[]{pubId, String.valueOf(xPos), String.valueOf(yPos), String.valueOf(animationStateAsInt)});

             //server.tcpServer.get(pubId).out.println(server.keyword + server.infoSeperator + "OGAME-UPDATEPROT" + server.infoSeperator + pubId + server.infoSeperator + String.valueOf(xPos) + server.infoSeperator + String.valueOf(yPos) + server.infoSeperator + String.valueOf(animationStateAsInt));
             */
        }

        // #ENDGAME
        server.tcpServer.get(pubId).getCommandHandler().sendEndGame(game.playersAtBeginn);
    }

    public void update(double timeElapsedSeconds) {

        remotePitchfork.setX(xPos);
        remotePitchfork.setY(yPos);

        if (!isInited) {
            now = System.nanoTime();
            startTime = now;
            oldTime = now;
            timeElapsed = 0;
            isInited = true;
        }
        now = System.nanoTime();
        timeElapsed = now - oldTime;
        oldTime = now;
        timeElapsedSeconds = timeElapsed / (1000.0d * 1000.0d * 1000.0d);

        if (yPos > 5000) {
            hitten();
        }

        if ((!respawnDoing) && (!isDead)) {

            if (hitDoing) {
                updateHit(timeElapsedSeconds);
            } else if (isMachinePistol) {
                updateMachinePistol(timeElapsedSeconds);
            } else if (shootDoing) {
                goesRight = false;
                goesLeft = false;
                updateShoot(timeElapsedSeconds);
            }

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
            animationStateAsInt = currCostume.ordinal();
        } else if (respawnDoing) {
            updateRespawn(timeElapsedSeconds);

        } else {
            if (isUp) {
                ySpeed = -1 * ySpectatorSpeed;
            } else if (isDown) {
                ySpeed = ySpectatorSpeed;
            } else if (goesLeft) {
                xSpeed = -1 * xSpectatorSpeed;
            } else if (goesRight) {
                xSpeed = xSpectatorSpeed;
            } else {
                xSpeed = 0;
                ySpeed = 0;
            }
            xPos += xSpeed * timeElapsedSeconds;
            yPos += ySpeed * timeElapsedSeconds;
            animationStateAsInt = -1;

        }
        setX(xPos);
        setY(yPos);

    }

    @Override
    public double getXPos() {
        return xPos;
    }

    @Override
    public double getYPos() {
        return yPos;
    }

    @Override
    public String getObjectId() {
        return objectId;
    }

    @Override
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

    @Override
    public int getObjectTypeAsInt() {
        return objectType.ordinal();
    }

    public boolean collisionCheck(Vector<Vector<Block>> worldVec, HashMap<String, RemotePlayer> players) {
        double blockSize = game.blockSize;
        if(OnlGame.worldCollisionCheck(worldVec, xPos, yPos, width, height, blockSize)) {
            return true;
        }
        if (intersectsPlayer(players)) {
            return true;
        }
        return false;
    }

    public boolean intersectsPlayer(RemotePlayer p) {
        if (intersects(xPos, yPos, width, height, p.getX(), p.getY(), width, height)) {
            if ((!p.isRespawning()) && (!p.isDead)) {

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
                || endY2 < y1) {
            return false;
        }
        return true;
    }

    void initClientOtherPlayer(RemotePlayer p2) {
        server.tcpServer.get(pubId).getCommandHandler().sendCommand(ServerCommand.OGAME_INITPROT, new String[]{p2.name, p2.skin, String.valueOf(p2.index), p2.pubId, p2.getObjectId(), "0", p2.userId});
        // server.tcpServer.get(pubId).out.println(server.keyword + server.infoSeperator + "OGAME-INITPROT" + server.infoSeperator + p2.name + server.infoSeperator + p2.skin + server.infoSeperator + String.valueOf(p2.index) + server.infoSeperator + p2.pubId + server.infoSeperator + "0");
    }

    void initClientPendant(RemotePlayer p2) {
        server.tcpServer.get(pubId).getCommandHandler().sendCommand(ServerCommand.OGAME_INITPROT, new String[]{p2.name, p2.skin, String.valueOf(p2.index), p2.pubId, p2.getObjectId(), "1", p2.userId});
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
            case "DOWN":
                doDown();
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
            case "JUMP":
                isUp = false;
                break;
            case "DOWN":
                isDown = false;
        }
    }

    @Override
    public void doRight() {
        isFacingLeft = false;
        goesRight = true;
        goesLeft = false;
    }

    @Override
    public void doLeft() {
        isFacingLeft = true;
        goesLeft = true;
        goesRight = false;
    }

    @Override
    public void doJump() {
        isUp = true;
        if (ySpeed == 0) {
            jumpDone = true;
        }
    }

    @Override
    public void doShoot() {
        shootDoing = true;
    }

    public void doDown() {
        isDown = true;
    }

    @Override
    public void hitten() {
        if ((!respawnDoing) && (!isDead)) {
            xPos = xSpawn;
            yPos = ySpawn;
            setX(xPos);
            setY(yPos);
            respawnDoing = true;
            respawnLabel.setVal(3.999999999);
            game.getCounterLabels().add(respawnLabel);
            ySpeed = 0;
            incrementDeaths();
            game.checkEndGame();
            remoteGun.setAnimationState(-1);
            remotePitchfork.setAnimationState(-1);
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

    public void updateHit(double timeElapsedSeconds) {
        hitTimer += timeElapsedSeconds;
        if (isFacingLeft) {
            remotePitchfork.setX(xPos - 40); //- forkAnimationXPosAdd);
            setAnimationState(CostumeViewport.LEFT_HIT);
            remotePitchfork.setAnimationState(0);
        } else {
            remotePitchfork.setX(xPos + 30); //+ forkAnimationXPosAdd);
            setAnimationState(CostumeViewport.RIGHT_HIT);
            remotePitchfork.setAnimationState(1);
        }
        remotePitchfork.setY(yPos + 55);
        game.players.forEach((id, p) -> {
            if (!id.equals(pubId)) {
                if (intersects(remotePitchfork.getX(), remotePitchfork.getY(), remotePitchfork.getWidth(), remotePitchfork.getHeight(), p.getX(), p.getY(), width, height)) {
                    p.hitten();
                    incrementKills();
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

    public void updateShoot(double timeElapsedSeconds) {
        if ((shootTimer > 1) && (!shootGenerated)) {
            game.generateShoot(this);
            shootGenerated = true;
        }
        shootTimer += timeElapsedSeconds;
        if (isFacingLeft) {
            remoteGun.setX(getX() - 20); //- forkAnimationXPosAdd);
            setAnimationState(CostumeViewport.LEFT_SHOOT);
            if (shootTimer < 1) {
                remoteGun.setAnimationState(Gun.AnimationState.LEFT.ordinal());
            } else {
                remoteGun.setAnimationState(Gun.AnimationState.LEFT_SHOOT.ordinal());
            }
        } else {
            remoteGun.setX(getX() + 5); //+ forkAnimationXPosAdd);
            setAnimationState(CostumeViewport.RIGHT_SHOOT);
            if (shootTimer < 1) {
                remoteGun.setAnimationState(Gun.AnimationState.RIGHT.ordinal());
            } else {
                remoteGun.setAnimationState(Gun.AnimationState.RIGHT_SHOOT.ordinal());
            }
        }
        remoteGun.setY(getY() + 22); ///
        // gun.updateShoot(shootTimer);

        if (shootTimer > 2) {
            shootDoing = false;
            remoteGun.setAnimationState(-1);
            shootTimer = 0;
            setAnimationState(CostumeViewport.MID);
            shootGenerated = false;

        }

    }

    @Override
    public void updateRespawn(double timeElapsedSeconds) {
        animationStateAsInt = -1;
        respawnTimer -= timeElapsedSeconds;
        setVisible(false);
        respawnLabel.addVal(-1 * timeElapsedSeconds);
        remoteGun.setAnimationState(-1);
        remotePitchfork.setAnimationState(-1);

        if (respawnTimer < 0) {
            remoteGun.setAnimationState(-1);
            remotePitchfork.setAnimationState(-1);
            game.removeCounterLabel(respawnLabel);

            respawnDoing = false;
            respawnTimer = 3;

            setVisible(true);
            animationStateAsInt = CostumeViewport.MID.ordinal();
            setAnimationState(CostumeViewport.MID);
            currCostume = CostumeViewport.MID;
            game.sendAllTCPDelayed(ServerCommand.OGAME_REMOVEOBJECT, new String[]{respawnLabel.getObjectId()});

        }

    }

    public void endGame() {
        endGame = true;
    }

    public RemoteObject getRemotePitchfork() {
        return remotePitchfork;
    }

    public RemoteObject getRemoteGun() {
        return remoteGun;
    }

    public boolean isFacingLeft() {
        return isFacingLeft;
    }

    private String getRespawnLabelVal() {
        return String.valueOf((int) respawnTimer);
    }

    public synchronized void sendCommand(ServerCommand command, String[] args) {
        serverCommandsToSend.add(command);
        argsToSend.add(args);
    }

    public void incrementDeaths() {
        deaths++;
        if (game.gamemode.equals(Gamemode.DEATHS)) {
            deathCounter.addVal(-1);
            if (deathCounter.getValInt() < 0) {
                isDead = true;
                animationStateAsInt = -1;
            }
        } else {
            deathCounter.addVal(1);
        }
    }

    public void incrementKills() {
        kills++;
        killCounter.addVal(1);
    }

    public boolean isDead() {
        return isDead;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKills() {
        return kills;
    }

    public void setPlacement(String p) {
        placement = p;
    }

    public String getPlacement() {
        return placement;
    }
    
    public int getCoinsCollected() {
        return coinsCollected;
    }

}
