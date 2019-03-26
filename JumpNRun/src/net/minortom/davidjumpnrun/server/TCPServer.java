/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServer {
    Server server;
    
    public static ServerSocket serverSocket;
    Socket clientSocket;
    PrintWriter out;
    BufferedReader in;
    NetworkTCPReceiver tcpReceiver;
    
    public String userName;
    public String pass;
    
    public String token;
    public int pubId;
    
    public TCPServer(Server getServ){
        server = getServ;
        
        
        try{
            clientSocket = serverSocket.accept();
            out =
                new PrintWriter(clientSocket.getOutputStream(), true);                   
            in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        }catch(Exception e){
            System.err.print("E");
            return;
        }
        
        tcpReceiver = new NetworkTCPReceiver(server, this);
        tcpReceiver.start();
    }
    
    public static void init(int port){
        try {
            TCPServer.serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
