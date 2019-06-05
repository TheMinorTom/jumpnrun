/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import jumpnrun.JumpNRun;
import jumpnrun.Shoot;
import net.minortom.davidjumpnrun.netcode.GameObjectType;
import net.minortom.davidjumpnrun.netcode.ServerCommand;
import worldeditor.Block;
import worldeditor.IO;

public class OnlGame implements Runnable {

    public static final double spawnY = 0;

    Server server;

    public boolean ended;
    public boolean willStart;
    public boolean started;

    public String gameName;
    public int playersMax;
    public JumpNRun.Gamemode gamemode;
    public double timeLimitSeconds;
    public int respawnLimit;

    public String mapName;
    public String mapText;
    public String gamemodeAsUpperString;

    public HashMap<String, String> playerSkins; // Id, skin
    public HashMap<String, RemotePlayer> players; // Id, RemotePlayer
    public HashMap<String, RemotePlayer> playersAtBeginn;

    public Vector<Vector<Block>> worldVector;
    public double worldWidth;
    public double blockSize;

    public int udpPort;
    public DatagramSocket udpSocket;

    public HashMap<String, OnlineGameObject> onlineGameObjects;
    public ObservableList<RemoteUpdatableObject> movingObjects;
    public ObservableList<RemoteUpdatableObject> shoots;

    private int currObjectId = Integer.MIN_VALUE;

    // loop
    private double timeElapsedSeconds;

    private double gameTime;
    private OnlineCounterLabel gameTimer;

    private boolean isStarted = false;

    private ObservableList<OnlineCounterLabel> counterLabels, counterLabelsToRemove;
    private int playersAlive;

    public OnlGame(Server server, String gameName, int playersMax, String gamemode, double timeLimit, int respawnLimit, String mapName, String playerOneId, String playerOneSkin) {
        this.server = server;

        this.gameName = gameName;
        this.playersMax = playersMax;
        onlineGameObjects = new HashMap<>();
        movingObjects = FXCollections.observableArrayList();
        shoots = FXCollections.observableArrayList();
        counterLabels = FXCollections.observableArrayList();
        counterLabelsToRemove = FXCollections.observableArrayList();
        playersAtBeginn = new HashMap<>();

        ended = false;
        gamemodeAsUpperString = gamemode.toUpperCase();
        this.timeLimitSeconds = timeLimit * 60;
        this.respawnLimit = respawnLimit;
        this.mapName = mapName;

        switch (gamemodeAsUpperString) {
            case "DEATHS":
                this.gamemode = JumpNRun.Gamemode.DEATHS;
                gameTimer = new OnlineCounterLabel(nextObjectId(), GameObjectType.GAMETIMER, 0, this);
                break;
            case "TIME":
                this.gamemode = JumpNRun.Gamemode.TIME;

                gameTimer = new OnlineCounterLabel(nextObjectId(), GameObjectType.GAMETIMER, timeLimitSeconds, this);
                break;
            case "ENDLESS":
            default:
                this.gamemode = JumpNRun.Gamemode.ENDLESS;
                gameTimer = new OnlineCounterLabel(nextObjectId(), GameObjectType.GAMETIMER, 0, this);
                break;
        }
        counterLabels.add(gameTimer);

        mapText = MapHelper.getMap(MapHelper.getMapCfgFile().get(mapName).fileName);
        worldVector = IO.openWorldForServer(mapText, Server.getBlocksFolder());
        worldWidth = worldVector.size() * worldVector.get(0).get(0).getFitWidth();
        blockSize = worldVector.get(0).get(0).getFitWidth();
        players = new HashMap<>();
        playerSkins = new HashMap<>();

        addPlayer(playerOneId, playerOneSkin);
        playersAlive = 0;

    }

    public void addPlayer(String pubId, String skin) {
        playerSkins.put(pubId, skin);
        String name = server.tcpServer.get(pubId).userName;
        String addObjectId = nextObjectId();
        String userId = server.tcpServer.get(pubId).userId;
        RemotePlayer addPlayer;
        try {
            addPlayer = new RemotePlayer(server, this, pubId, addObjectId, skin, name, players.size(), playersMax, userId, server.dbConn.getScore(userId));
        } catch (SQLException ex) {
            addPlayer = new RemotePlayer(server, this, pubId, addObjectId, skin, name, players.size(), playersMax, userId, 0);
        }
        playersAtBeginn.put(pubId, addPlayer);
        onlineGameObjects.put(addObjectId, addPlayer);

        players.put(pubId, addPlayer);

        sendAllTCP(ServerCommand.OGAME_PJOINED, new String[]{name, pubId, String.valueOf(playersMax)});
        if (isReadyToStart()) {
            startGame();
        }
    }

    public synchronized String nextObjectId() {
        String returnId = String.valueOf(currObjectId);
        currObjectId++;
        return returnId;
    }

    public synchronized void sendAllTCP(ServerCommand command, String[] args) {
        players.forEach((String k, RemotePlayer v) -> {

            server.tcpServer.get(k).getCommandHandler().sendCommand(command, args);

        }
        );
    }

    public void sendAllTCPDelayed(ServerCommand command, String[] args) {
        players.forEach((String k, RemotePlayer v) -> {

            v.sendCommand(command, args);

        }
        );
    }

    public void sendAllUDP(ServerCommand command, String[] args) {
        if (!willStart) {
            return;
        }
        players.forEach((k, v) -> {
            server.tcpServer.get(k).getCommandHandler().sendCommand(command, args);

        });
    }

    public int getPlayersUdpConnected() {
        int c = 0;
        for (java.util.Map.Entry<String, RemotePlayer> entry : players.entrySet()) {
            String key = entry.getKey();
            RemotePlayer value = entry.getValue();
            if (value.udpConnected) {
                c++;
            }
        }
        return c;
    }

    public void startGame() {
        String limit;
        if (gamemode == JumpNRun.Gamemode.DEATHS) {
            limit = String.valueOf(respawnLimit);
        } else if (gamemode == JumpNRun.Gamemode.TIME) {
            limit = String.valueOf(timeLimitSeconds);
        } else {
            limit = "0";
        }
        // sendAllTCP(server.keyword + server.infoSeperator + "OGAME-INITGAME" + server.infoSeperator + String.valueOf(playersMax) + server.infoSeperator + String.valueOf(spawnY) + server.infoSeperator + gamemodeAsUpperString + server.infoSeperator + limit + server.infoSeperator + gameName);
        sendAllTCP(ServerCommand.OGAME_INITGAME, new String[]{String.valueOf(playersMax), String.valueOf(spawnY), gamemodeAsUpperString, limit, gameName});

        System.out.println("World successfully loaded!");
        // sendAllTCP(server.keyword + server.infoSeperator + "OGAME-INITMAP" + server.infoSeperator + mapText);
        sendAllTCP(ServerCommand.OGAME_INITMAP, new String[]{mapText});

        players.forEach((id1, p1) -> {
            players.forEach((id2, p2) -> {
                if (id1.equals(id2)) {
                    p1.initClientPendant(p2);
                } else {
                    p1.initClientOtherPlayer(p2);
                }
            });
        });
        (new Thread(this)).start();

    }

    public void startGameNow() {

    }

    /*
     @Override
     public void run() {
     while (!ended) {
     if(willStart){
     if(started){
                    
     } else {
     if(players.size() == getPlayersUdpConnected()){
     startGameNow();
     } else {
                        
     }
     }
     } else {
     if(players.size() == playersMax){
     startGame();
     } else {
                    
     }
     }
     }
     }
     */
    //Game main-loop
    @Override
    public void run() {
        sendAllTCP(ServerCommand.OGAME_START, new String[]{});
        Random random = new Random();
        udpPort = random.nextInt(26670 - 26660) + 26660;
        try {
            udpSocket = new DatagramSocket(udpPort);
        } catch (SocketException ex) {
            ex.printStackTrace();
        }

        for (java.util.Map.Entry<String, RemotePlayer> entry : players.entrySet()) {
            (new Thread(entry.getValue())).start();
        }

        double now = System.nanoTime();
        double startTime = now;
        double oldTime = now;
        double timeElapsed = 0;
        double runtimeSeconds = 0;
        timeElapsedSeconds = timeElapsed;

        isStarted = true;
        while (!ended) {
            now = System.nanoTime();
            timeElapsed = now - oldTime;
            oldTime = now;
            timeElapsedSeconds = timeElapsed / (1000.0d * 1000.0d * 1000.0d);
            runtimeSeconds += timeElapsedSeconds;
            /*
             players.forEach((id, p) -> {
             sendAllTCP(ServerCommand.OGAME_UPDATEPROT, new String[]{p.pubId, String.valueOf(p.getXPos()), String.valueOf(p.getYPos()), String.valueOf(p.getAnimationStateAsInt())});
             try {
             Thread.sleep(5);
             } catch (InterruptedException ex) {
             Logger.getLogger(OnlGame.class.getName()).log(Level.SEVERE, null, ex);
             }
             });
             */
            players.forEach((id, p) -> {
                p.update(timeElapsedSeconds);
            });

            movingObjects.forEach((o) -> {
                o.update(timeElapsedSeconds);
            });

            try {
                shoots.forEach((shoot) -> {
                    players.forEach((id, player) -> {
                        if (!player.equals(shoot.getOwner())) {
                            if (shoot.intersects(player.getBoundsInLocal())) {
                                deleteShoot(shoot);
                                player.hitten();
                                shoot.getOwner().incrementKills();
                            }
                        }
                    });

                    if (worldCollisionCheck(worldVector, shoot.getXPos(), shoot.getYPos(), shoot.getWidth(), shoot.getHeight(), blockSize)) {
                        deleteShoot(shoot);
                    };
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (gamemode == JumpNRun.Gamemode.TIME) {
                gameTimer.addVal(-1 * timeElapsedSeconds);
            } else {
                gameTimer.addVal(timeElapsedSeconds);
            }

            if (counterLabelsToRemove.size() != 0) {
                counterLabels.removeAll(counterLabelsToRemove);
                counterLabelsToRemove.clear();
            }

            if (gamemode.equals(gamemode.TIME)) {
                if (runtimeSeconds > timeLimitSeconds) {
                    endGame();
                }
            }
        }
    }

    private boolean isReadyToStart() {
        //if(willStart){

        if (players.size() == playersMax) {
            return true;
        }

        //}
        return false;
    }

    public void deleteShoot(RemoteUpdatableObject o) {
        movingObjects.remove(o);
        onlineGameObjects.remove(o.getObjectId());
        shoots.remove(o);
        sendAllTCP(ServerCommand.OGAME_REMOVEOBJECT, new String[]{o.getObjectId()});
    }

    public void addShoot(RemoteUpdatableObject o) {
        movingObjects.add(o);
        onlineGameObjects.put(o.getObjectId(), o);
        shoots.add(o);
    }

    public void generateShoot(RemotePlayer p) {
        RemoteUpdatableObject shoot;
        if (p.isFacingLeft()) {
            shoot = new RemoteUpdatableObject(nextObjectId(), Shoot.AnimationState.LEFT.getRect(), GameObjectType.SHOOT, p.getRemoteGun().getX() + 40, p.getRemoteGun().getY(), -1000, 0, 0, 200, this, p, Shoot.AnimationState.RIGHT.ordinal());
        } else {
            shoot = new RemoteUpdatableObject(nextObjectId(), Shoot.AnimationState.LEFT.getRect(), GameObjectType.SHOOT, p.getRemoteGun().getX() + 40, p.getRemoteGun().getY(), 1000, 0, 0, 200, this, p, Shoot.AnimationState.RIGHT.ordinal());
        }
        addShoot(shoot);
    }

    public OnlineCounterLabel getGameTimer() {
        return gameTimer;
    }

    public ObservableList<OnlineCounterLabel> getCounterLabels() {
        return counterLabels;
    }

    public void removeCounterLabel(OnlineCounterLabel l) {
        counterLabelsToRemove.add(l);
    }

    public void checkEndGame() {
        playersAlive = 0;
        players.forEach((String key, RemotePlayer p) -> {
            if (!p.isDead()) {
                playersAlive++;
            }
        });
        if ((playersAlive <= 1) && (!gamemode.equals(gamemode.ENDLESS))) {
            endGame();
        }
    }

    private void endGame() {
        ended = true;
        players = sortPlayers(players);
        int playerAmount = players.size();
        
        players.forEach((String key, RemotePlayer p) -> {
            p.endGame();
            try {
                int winsAdd = 0;
                if(Integer.parseInt(p.getPlacement()) == 1) {
                    winsAdd = 1;
                }
                server.dbConn.updateStats(p.userId, p.getKills(), p.getDeaths(), winsAdd, 1, ScoreEngine.calculateXP(p.getKills(), p.getDeaths(), Integer.parseInt(p.getPlacement()), playerAmount), p.getCoinsCollected(), ScoreEngine.calculateScore(Integer.parseInt(p.getPlacement()), playerAmount, p.userId));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        server.games.remove(gameName);

    }
    
    public HashMap<String, RemotePlayer> sortPlayers(HashMap<String, RemotePlayer> unsortedMap) {
        /*
         killsHM = new HashMap<>();
         unsortedMap.forEach((String key, RemotePlayer p) -> {
         killsHM.put(key, new Integer(p.getKills()));
         });
         List<Map.Entry<String, Integer>> list
         = new LinkedList<Map.Entry<String, Integer>>(killsHM.entrySet());
         Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
         public int compare(Map.Entry<String, Integer> o1,
         Map.Entry<String, Integer> o2) {
         return (o1.getValue()).compareTo(o2.getValue());
         }
         });

         // put data from sorted list to hashmap  
         int counter = unsortedMap.size();
         HashMap<String, RemotePlayer> sortedMap = new HashMap<>();
         for (Map.Entry<String, Integer> aa : list) {
         sortedMap.put(aa.getKey(), unsortedMap.get(aa.getKey()));
         sortedMap.get(aa.getKey()).setPlacement(String.valueOf(counter));
         counter--;
         }
         return sortedMap;
         */

        List<java.util.Map.Entry<String, RemotePlayer>> playerList
                = new LinkedList<java.util.Map.Entry<String, RemotePlayer>>(unsortedMap.entrySet());
        Collections.sort(playerList, new Comparator<java.util.Map.Entry<String, RemotePlayer>>() {
            public int compare(java.util.Map.Entry<String, RemotePlayer> o1,
                    java.util.Map.Entry<String, RemotePlayer> o2) {

                if (o1.getValue().getKills() > o2.getValue().getKills()) {
                    return 1;
                } else if (o1.getValue().getKills() < o2.getValue().getKills()) {
                    return -1;
                } else {
                    if (o1.getValue().getDeaths() > o2.getValue().getDeaths()) {
                        return -1;
                    } else if (o1.getValue().getDeaths() < o2.getValue().getDeaths()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        });

        // put data from sorted list to hashmap  
        int counter = unsortedMap.size();
        HashMap<String, RemotePlayer> sortedMap = new HashMap<>();
        for (java.util.Map.Entry<String, RemotePlayer> aa : playerList) {
            sortedMap.put(aa.getKey(), unsortedMap.get(aa.getKey()));
            sortedMap.get(aa.getKey()).setPlacement(String.valueOf(counter));
            counter--;
        }
        return sortedMap;
    }

    public static boolean worldCollisionCheck(Vector<Vector<Block>> worldVec, double x, double y, double w, double h, double blockSize) {
        for (int xIndex = (int) (x / blockSize); (xIndex <= (int) ((x + w) / blockSize)) && (xIndex < worldVec.size()); xIndex++) {
            if (xIndex >= 0) {
                for (int yIndex = (int) (y / blockSize); (yIndex <= (int) ((y + h) / blockSize)) && (yIndex < worldVec.get(xIndex).size()); yIndex++) {
                    if ((yIndex >= 0) && worldVec.get(xIndex).get(yIndex).getIsSolid()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
