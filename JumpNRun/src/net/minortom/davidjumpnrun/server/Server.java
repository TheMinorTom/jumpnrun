/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import net.minortom.davidjumpnrun.netcode.NetworkManager;

public class Server {

    public static Server server;
    
    public final String infoSeperator = NetworkManager.infoSeperator;
    public final String keyword = NetworkManager.keyword;
    
    public HashMap<String,OnlGame> games;
    
    public static void main(String[] args) {
        new Server(args);
    }
    
    public HashMap<String, TCPServer> tcpServer;
    public int tcpPort;
    
    public Server(String[] args){
        /* try{
            tcpPort = new Integer(args[0]);
        }catch(Exception e){
            System.err.println("Invalid argument: Port");
            System.exit(1);
        } */
        Server.server = this;
        
        System.out.println("Server Hello World");
        tcpPort = 26656;
        tcpServer = new HashMap<>();
        TCPServer.init(tcpPort);
        
        games = new HashMap<>();
        
        while(true){
            String pubId = "P" + Integer.toString((int) (Math.random()*10000000));
            tcpServer.put(pubId, new TCPServer(this, pubId));
        }
    }
    
    public String getMapFolder(){
        return ConfigManager.getStorageLocation() + "worlds/";
    }
}
