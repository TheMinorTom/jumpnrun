/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

import java.util.ArrayList;
import java.util.List;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import net.minortom.davidjumpnrun.netcode.NetworkManager;

public class Server {

    public static Server server;
    
    public final String infoSeperator = NetworkManager.infoSeperator;
    public final String keyword = NetworkManager.keyword;
    
    public static void main(String[] args) {
        Server.server = new Server(args);
    }
    
    public List<TCPServer> tcpServer;
    public int tcpPort;
    
    public Server(String[] args){
        /* try{
            tcpPort = new Integer(args[0]);
        }catch(Exception e){
            System.err.println("Invalid argument: Port");
            System.exit(1);
        } */
        System.out.println(infoSeperator);
        tcpPort = 26656;
        tcpServer = new ArrayList<>();
        TCPServer.init(tcpPort);
        while(true){
            tcpServer.add(new TCPServer(this));
        }
    }
    
    public String getMapFolder(){
        return ConfigManager.getStorageLocation() + "maps/";
    }
}
