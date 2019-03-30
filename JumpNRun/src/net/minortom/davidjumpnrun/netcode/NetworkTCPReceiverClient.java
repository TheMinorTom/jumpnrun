/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.netcode;

import java.io.IOException;
import jumpnrun.JumpNRun;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import net.minortom.davidjumpnrun.netcode.ServerConnection.ConnState;

public class NetworkTCPReceiverClient extends Thread{
    JumpNRun game;
    ServerConnection sconn;
    
    @Override
    public void run(){
        String line;
        try {
            while ((line = game.networkManager.serverConnection.in.readLine()) != null){
                System.out.println(line);
                try{
                    String[] packageContent = line.split(NetworkManager.infoSeperator);
                    if(!packageContent[0].equals(NetworkManager.keyword)){
                        System.err.println("Invalid package received: " + line);
                        continue;
                    }
                    if(packageContent[1].startsWith("AUTH")){
                        String type = packageContent[1].split("-")[1];
                        if(type.equals("OK")){
                            sconn.pubId = packageContent[2];
                            sconn.token = packageContent[3];
                            game.networkManager.serverConnection.currentConnState = ConnState.CONNECTED;
                            System.out.println("CONNECTED " + sconn.pubId + " " + sconn.token);
                        }
                    } else if (packageContent[1].startsWith("MAP")){
                        String type = packageContent[1].split("-")[1];
                        if(type.equals("LISTOK")){
                            String[] maps = packageContent[2].split(",");
                            game.networkManager.mapSelectionDone(maps);
                        } 
                    } else if (packageContent[1].startsWith("OGAME")){
                        String type = packageContent[1].split("-")[1];
                        switch (type) {
                            case "YJOINED":
                                break;
                            case "PJOINED":

                                break;
                            case "ERR":
                                String text;
                                switch (packageContent[3].toLowerCase()) {
                                    case "namealreadyexists":
                                        text = game.language.ErrorNameAlreadyExists;
                                        break;
                                    case "namedoesntexist":
                                        text = game.language.ErrorNameDoesntExist;
                                        break;
                                    default:
                                        text = game.language.ErrorUnknown;
                                        break;
                                }
                                if(Boolean.parseBoolean(packageContent[2])){
                                    ConfigManager.error(game.language.ErrorFatalTitle, text);
                                    game.openNetworkScreen();
                                } else {
                                    ConfigManager.error(game.language.ErrorTitle, text);
                                }
                                break;
                            default:
                                break;
                        }
                    } else if (packageContent[1].startsWith("IGAME")){
                        String type = packageContent[1].split("-")[1];
                        if(type.equals("TYPE")){
                            
                        }
                    }
                } catch (Exception e){
                    System.err.println("Invalid package received");
                    e.printStackTrace();
                }
                /**
                if(line.contains("JUMPNRUN")){
                    System.out.println(line);
                    game.networkManager.serverConnection.currentConnState = ServerConnection.ConnState.CONNECTED;
                }else{
                    System.err.println(line);
                }
                
                
                if(line.equals("JUMPNRUN test")){
                    game.networkManager.serverConnection.currentConnState = ServerConnection.ConnState.CONNECTED;
                }
                **/
            }
        } catch (IOException ex) {
            game.networkManager.serverConnection.currentConnState = ServerConnection.ConnState.ERROR_INTERNAL;
        }
    }
    
    NetworkTCPReceiverClient(JumpNRun setgame, ServerConnection setsconn){
        game = setgame;
        sconn = setsconn;
    }
}
