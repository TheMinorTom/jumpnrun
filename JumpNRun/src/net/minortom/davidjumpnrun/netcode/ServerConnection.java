/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.netcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jumpnrun.JumpNRun;

public class ServerConnection {
    private String username;
    private String pass;
    private String hostName;
    private int hostPort;
    
    public int pubId;
    public String token;
    
    public ConnState currentConnState;
    private NetworkTCPReceiver tcpreceiver;
    
    private Socket socket;
    public PrintWriter out;
    public BufferedReader in;
    JumpNRun game;
    
    public ServerConnection(String setUser, String setPass, String setHost, JumpNRun setGame){
        currentConnState = ConnState.WAITING;
        
        username = setUser;
        pass = setPass;
        hostName = setHost.split(":")[0];
        
        game = setGame;
        
        try{
            hostPort = new Integer(setHost.split(":")[1]);
        }catch(Exception e){
            currentConnState = ConnState.ERROR_INVALID_HOST;
        }
    }
    
    public void connect(){
        
        currentConnState = ConnState.CONNECTING;
        try{
            socket = new Socket(hostName, hostPort);
            out =
                new PrintWriter(socket.getOutputStream(), true);
            in =
                new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            currentConnState = ConnState.ERROR_INVALID_HOST;
            return;
        } catch (IOException e) {
            currentConnState = ConnState.ERROR_INVALID_HOST;
            return;
        }
        tcpreceiver = new NetworkTCPReceiver(game, this);
        tcpreceiver.start();
        
        out.println(NetworkManager.keyword + NetworkManager.infoSeperator + "AUTH-REQ" + NetworkManager.infoSeperator + username + NetworkManager.infoSeperator + pass);
        
        
        while (currentConnState == ConnState.CONNECTING){
            int time = 0;
            try {
                Thread.sleep(1);
                time++;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            //if(time<=60000){
            //    currentConnState = ConnState.ERROR_INTERNAL;
            //}
        }
    }
    
    public enum ConnState{
        CONNECTED,
        CONNECTING,
        WAITING,
        ERROR_INVALID_HOST,
        ERROR_INVALID_AUTH,
        ERROR_INTERNAL
    }
    
    public void end(){
        try {
            out.println(NetworkManager.keyword + NetworkManager.infoSeperator + "AUTH-LOGOUT");
        } catch (Exception e) {}
        tcpreceiver.stop();
        game.networkManager.serverConnection = null;
    }
    
    @Override
    public String toString(){
        return "State: " + currentConnState + "\nUser: " + username + "\nPass: " + pass + "\nHost: " + hostName + ":" + hostPort;
    }
}
