/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import jumpnrun.JumpNRun;
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
    public double timeLimit;
    public int respawnLimit;

    public String mapName;
    public String mapText;
    public String gamemodeAsUpperString;

    public HashMap<String, String> playerSkins; // Id, skin
    public HashMap<String, RemotePlayer> players; // Id, RemotePlayer

    public Vector<Vector<Block>> worldVector;
    public double worldWidth;
    public double blockSize;

    public int udpPort;
    public DatagramSocket udpSocket;

    public OnlGame(Server server, String gameName, int playersMax, String gamemode, double timeLimit, int respawnLimit, String mapName, String playerOneId, String playerOneSkin) {
        this.server = server;

        this.gameName = gameName;
        this.playersMax = playersMax;

        ended = false;
        gamemodeAsUpperString = gamemode.toUpperCase();
        switch (gamemodeAsUpperString) {
            case "DEATHS":
                this.gamemode = JumpNRun.Gamemode.DEATHS;
                break;
            case "TIME":
                this.gamemode = JumpNRun.Gamemode.TIME;
                break;
            case "ENDLESS":
                this.gamemode = JumpNRun.Gamemode.ENDLESS;
                break;
            default:
                this.gamemode = JumpNRun.Gamemode.ENDLESS;
                break;
        }

        this.timeLimit = timeLimit;
        this.respawnLimit = respawnLimit;
        this.mapName = mapName;

        mapText = MapHelper.getMap(MapHelper.getMapCfgFile().get(mapName).fileName);
        worldVector = IO.openWorld(mapText, Server.getBlocksFolder());
        worldWidth = worldVector.size() * worldVector.get(0).get(0).getFitWidth();
        blockSize = worldVector.get(0).get(0).getFitWidth();
        players = new HashMap<>();
        playerSkins = new HashMap<>();

        addPlayer(playerOneId, playerOneSkin);
    }

    public void addPlayer(String pubId, String skin) {
        playerSkins.put(pubId, skin);
        String name = server.tcpServer.get(pubId).userName;
        RemotePlayer addPlayer = new RemotePlayer(server, this, pubId, skin, name, players.size(), playersMax);
        players.put(pubId, addPlayer);
        sendAllTCP(ServerCommand.OGAME_PJOINED, new String[]{name, pubId, String.valueOf(playersMax)});
        if (isReadyToStart()) {
            startGame();
        }
    }

    public void sendAllTCP(ServerCommand command, String[] args) {
        players.forEach((k, v) -> {
            server.tcpServer.get(k).getCommandHandler().sendCommand(command, args);

        });
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
            limit = String.valueOf(timeLimit);
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

        while (!ended) {

            players.forEach((id, p) -> {
                sendAllTCP(ServerCommand.OGAME_UPDATEPROT, new String[]{p.pubId, String.valueOf(p.getXPos()), String.valueOf(p.getYPos()), String.valueOf(p.getAnimationStateAsInt())});
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    Logger.getLogger(OnlGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

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
}
