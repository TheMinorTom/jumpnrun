/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

import java.util.HashMap;
import jumpnrun.JumpNRun;

public class OnlGame {
    
    Server server;
    
    public String gameName;
    public int playersMax;
    public JumpNRun.Gamemode gamemode;
    public double timeLimit;
    public int respawnLimit;
    
    public String mapName;
    public String mapText;
    
    public HashMap<String,String> players; // Id, skin
    
    
    
    public OnlGame(Server server, String gameName, int playersMax, JumpNRun.Gamemode gamemode, double timeLimit, int respawnLimit, String mapName, String playerOneId, String playerOneSkin){
        this.server = server;
        
        this.gameName = gameName;
        this.playersMax = playersMax;
        this.gamemode = gamemode;
        this.timeLimit = timeLimit;
        this.respawnLimit = respawnLimit;
        this.mapName = mapName;
        
        mapText = MapHelper.getMap(mapName);
        
        players = new HashMap<>();
        
        players.put(playerOneId, playerOneSkin);
    }
}