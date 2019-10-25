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
import jumpnrun.JumpNRun;

public class ServerConnection {
    public String userName;
    public String userId;
    public String userToken;
    private String hostName;
    private int hostPort;
    
    public String pubId;
    public String token;
    
    public ConnState currentConnState;
    private NetworkTCPReceiverClient tcpreceiver;
    
    private Socket socket;
    private OnlineCommandHandler commandHandler;
    public BufferedReader in;
    JumpNRun game;
    
    public ServerConnection(String setUser, String setToken, String setHost, JumpNRun setGame){
        currentConnState = ConnState.WAITING;
        
        userId = setUser;
        userToken = setToken;
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
            System.out.println("Trying to connect");
            socket = new Socket(hostName, hostPort);
            commandHandler = new OnlineCommandHandler(
                new PrintWriter(socket.getOutputStream(), true));
            in =
                new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            System.out.println("Command handler generated");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            currentConnState = ConnState.ERROR_INVALID_HOST;
            return;
        } catch (IOException e) {
            e.printStackTrace();
            currentConnState = ConnState.ERROR_INVALID_HOST;
            return;
        }
        tcpreceiver = new NetworkTCPReceiverClient(game, this);
        tcpreceiver.start();
        System.out.println("AUTH_REQ sent");
        commandHandler.sendCommand(ServerCommand.AUTH_REQ, new String[]{userId, userToken});
        //out.println(NetworkManager.keyword + NetworkManager.infoSeperator + "AUTH-REQ" + NetworkManager.infoSeperator + userName + NetworkManager.infoSeperator + pass);
        
        
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
        System.out.println("CONNECTED!!!");
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
            commandHandler.sendCommand(ServerCommand.AUTH_LOGOUT, new String[]{pubId, game.gameName});
            // out.println(NetworkManager.keyword + NetworkManager.infoSeperator + "AUTH-LOGOUT");
        } catch (Exception e) {}
        tcpreceiver.stop();
        game.networkManager.serverConnection = null;
    }
    
    @Override
    public String toString(){
        return "State: " + currentConnState + "\nUser: " + userId + "\nToken: " + userToken + "\nHost: " + hostName + ":" + hostPort;
    }
    
    public OnlineCommandHandler getCommandHandler() {
        return commandHandler;
    }
}
