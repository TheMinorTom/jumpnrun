/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.netcode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import jumpnrun.JumpNRun;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import net.minortom.davidjumpnrun.netcode.ServerConnection.ConnState;
import net.minortom.davidjumpnrun.netcode.screens.WaitScreen;
import net.minortom.davidjumpnrun.netcode.screens.WaitScreen.WaitAnimation;

public class NetworkTCPReceiverClient extends Thread {

    JumpNRun game;
    ServerConnection sconn;

    @Override
    public void run() {
        String line;
        try {
            while ((line = game.networkManager.serverConnection.in.readLine()) != null) {
                try {
                    String[] packageContent = line.split(NetworkManager.infoSeperator);
                    if (!packageContent[0].equals(NetworkManager.keyword)) {
                        System.err.println("Invalid package received: " + line);
                        continue;
                    }
                    int commandOrdinary = Integer.parseInt(packageContent[1]);
                    ServerCommand command = ServerCommand.values()[commandOrdinary];

                    switch (command) {

                        case AUTH_OK:
                            sconn.pubId = packageContent[2];
                            sconn.token = packageContent[3];
                            game.networkManager.serverConnection.currentConnState = ConnState.CONNECTED;
                            System.out.println("CONNECTED " + sconn.pubId + " " + sconn.token);
                            break;
                        case MAP_LISTOK:
                            String[] maps = packageContent[2].split(",");
                            game.networkManager.mapSelectionDone(maps);
                            break;
                        case OGAME_YJOINED:
                            game.networkManager.onlineWaitScreenPlayers = new HashMap<>();
                            break;
                        case OGAME_PJOINED:
                            if (packageContent[3].equals(sconn.pubId)) {
                                game.networkManager.onlineWaitScreenPlayers = new HashMap<>();
                                game.networkManager.onlineWaitScreenPlayersNeeded = Integer.parseInt(packageContent[4]);
                            }
                            game.networkManager.onlineWaitScreenPlayers.put(packageContent[3], packageContent[2]);
                            String waitText = game.language.WaitOtherPlayersA;
                            waitText += game.networkManager.onlineWaitScreenPlayers.size() + "/" + game.networkManager.onlineWaitScreenPlayersNeeded;
                            waitText += game.language.WaitOtherPlayersB;
                            for (Entry<String, String> e : game.networkManager.onlineWaitScreenPlayers.entrySet()) {
                                String key = e.getKey();
                                String value = e.getValue();
                                waitText += "\n" + value;
                            }
                            if (game.networkManager.onlineWaitScreenPlayers.size() >= game.networkManager.onlineWaitScreenPlayersNeeded) {
                                game.networkManager.waitScreen.setText(game.language.WaitGameStart, WaitAnimation.LOADING);
                            } else {
                                game.networkManager.waitScreen.setText(waitText, WaitAnimation.WAITING);
                            }
                            break;
                        case OGAME_ERR:
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
                            if (Boolean.parseBoolean(packageContent[2])) {
                                ConfigManager.error(game.language.ErrorFatalTitle, text);
                                game.openNetworkScreen();
                            } else {
                                ConfigManager.error(game.language.ErrorTitle, text);
                            }
                            break;
                        case OGAME_INITMAP:
                            game.initMap(packageContent[2]);
                            break;
                        case OGAME_INITPROT:
                            if (packageContent[6].equals("1")) {
                                game.initLocalProt(packageContent[2], packageContent[3], Integer.valueOf(packageContent[4]), packageContent[5]);
                            } else {
                                game.initOtherProt(packageContent[2], packageContent[3], Integer.valueOf(packageContent[4]), packageContent[5]);
                            }
                            break;
                        case OGAME_INITGAME:
                            game.initOnlineGame(packageContent[2], packageContent[3], packageContent[4], packageContent[5], packageContent[6]);
                            break;
                        case OGAME_UPDATEPROT:
                            game.updateProt(packageContent[2], packageContent[3], packageContent[4], packageContent[5]);
                        default:
                            break;
                    }

                } catch (NullPointerException e) {
                } catch (Exception e) {
                    System.err.println("Invalid package received");
                    e.printStackTrace();
                    System.err.println(line);
                }
                /**
                 * if(line.contains("JUMPNRUN")){ System.out.println(line);
                 * game.networkManager.serverConnection.currentConnState =
                 * ServerConnection.ConnState.CONNECTED; }else{
                 * System.err.println(line); }
                 *
                 *
                 * if(line.equals("JUMPNRUN test")){
                 * game.networkManager.serverConnection.currentConnState =
                 * ServerConnection.ConnState.CONNECTED; }
                 *
                 */
            }
        } catch (IOException ex) {
            game.networkManager.serverConnection.currentConnState = ServerConnection.ConnState.ERROR_INTERNAL;
        }
    }

    NetworkTCPReceiverClient(JumpNRun setgame, ServerConnection setsconn) {
        game = setgame;
        sconn = setsconn;
    }
}
