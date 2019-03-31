/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

import java.util.HashMap;
import java.util.Vector;
import javafx.geometry.Point2D;
import jumpnrun.JumpNRun;
import worldeditor.Block;
import worldeditor.IO;

public class OnlGame implements Runnable{
    
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
    
    public HashMap<String,String> playerSkins; // Id, skin
    public HashMap<String,RemotePlayer> players; // Id, RemotePlayer
    
    private Vector<Vector<Block>> worldVector;
    
    public OnlGame(Server server, String gameName, int playersMax, String gamemode, double timeLimit, int respawnLimit, String mapName, String playerOneId, String playerOneSkin){
        this.server = server;
        
        this.gameName = gameName;
        this.playersMax = playersMax;
        ended = false;
        switch (gamemode) {
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
        
        players = new HashMap<>();
        playerSkins = new HashMap<>();
        
        addPlayer(playerOneId, playerOneSkin);
    }
    
    public void addPlayer(String pubId, String skin){
        playerSkins.put(pubId, skin);
        String name = server.tcpServer.get(pubId).userName;
        players.put(pubId, new RemotePlayer(server, this, pubId, skin, name));
        
        sendAllTCP(server.keyword + server.infoSeperator + "OGAME-PJOINED" + server.infoSeperator + name);
        if(isReadyToStart()) {
            startGame();
        }
    }
    
    public void sendAllTCP(String text){
        players.forEach((k,v) -> {
            server.tcpServer.get(k).out.println(text);
            
        });
    }
    
    public void sendAllUDP(String text){
        if(!willStart) return;
        players.forEach((k,v) -> {
            server.tcpServer.get(k).out.println(text);
        });
    }
    
    public int getPlayersUdpConnected(){
        int c = 0;
        for(java.util.Map.Entry<String, RemotePlayer> entry : players.entrySet()) {
            String key = entry.getKey();
            RemotePlayer value = entry.getValue();
            if(value.udpConnected){
                c++;
            }
        }
        return c;
    }
    
    public void startGame(){
        
        worldVector = IO.openWorld(mapText, Server.getBlocksFolder());
        double worldWidth = worldVector.size() * worldVector.get(0).get(0).getFitWidth();
        System.out.println("World successfully loaded!");
        (new Thread(this)).start();
    }
    
    public void startGameNow(){
        
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
        for(java.util.Map.Entry<String, RemotePlayer> entry : players.entrySet()) {
            (new Thread(entry.getValue())).start();
        }
        
        while(!ended) {
            
        }      
    }
    
    
    private boolean isReadyToStart() {
        //if(willStart){

            if(players.size() == playersMax){
                return true;
            }
        
        //}
        return false;
    }
}