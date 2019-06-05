/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minortom.davidjumpnrun.netcode.OnlineCommandHandler;

public class TCPServer {
    Server server;
    
    public static ServerSocket serverSocket;
    Socket clientSocket;
    private OnlineCommandHandler commandHandler;
    BufferedReader in;
    NetworkTCPReceiverServer tcpReceiver;
    
    public String userName, userId, userToken;
    public String pubId;
    
    public TCPServer(Server getServ, String pubId){
        server = getServ;
        this.pubId = pubId;
        
        try{
            clientSocket = serverSocket.accept();
            commandHandler = new OnlineCommandHandler(
                new PrintWriter(clientSocket.getOutputStream(), true));                   
            in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        }catch(Exception e){
            System.err.print("ENTON ");
            return;
        }
        
        tcpReceiver = new NetworkTCPReceiverServer(server, this);
        tcpReceiver.start();
    }
    
    public static void init(int port){
        try {
            TCPServer.serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public OnlineCommandHandler getCommandHandler() {
        return commandHandler;
    }
}
