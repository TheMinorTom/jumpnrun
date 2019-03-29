/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

public class RemotePlayer implements Runnable{
    
    public boolean udpConnected;
    
    Server server;
    OnlGame game;
    
    public String pubId;
    public String skin;
    public String name;
    
    
    public RemotePlayer(Server server, OnlGame game, String pubId, String skin, String name){
        this.server = server;
        this.game = game;
        this.pubId = pubId;
        this.skin = skin;
        this.name = name;
    }
    
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
