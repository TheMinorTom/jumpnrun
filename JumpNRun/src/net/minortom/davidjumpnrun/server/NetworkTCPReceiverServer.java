/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

import java.io.IOException;
import javafx.scene.input.KeyCode;
import net.minortom.davidjumpnrun.netcode.ServerCommand;

public class NetworkTCPReceiverServer extends Thread {

    Server server;
    TCPServer tcpServ;

    @Override
    public void run() {
        String line;
        try {
            while ((line = tcpServ.in.readLine()) != null) {
                try {
                    String[] packageContent = line.split(server.infoSeperator);
                    if (!packageContent[0].equals(server.keyword)) {
                        System.err.println("Invalid package received, " + server.keyword + ": " + packageContent[0]);
                        continue;
                    }

                    int commandOrdinary = Integer.parseInt(packageContent[1]);
                    ServerCommand command = ServerCommand.values()[commandOrdinary];

                    switch (command) {
                        case AUTH_REQ:
                            tcpServ.userId = packageContent[2];
                            tcpServ.userToken = packageContent[3];
                            // tcpServ.out.println(server.keyword + server.infoSeperator + "AUTH-OK" + server.infoSeperator + tcpServ.pubId + server.infoSeperator + tcpServ.token);
                            tcpServ.userName="user";
                            if(tcpServ.userName==null) {
                                tcpServ.getCommandHandler().sendCommand(ServerCommand.AUTH_WRONGCREDS, new String[]{});
                                System.out.println("DID NOT AUTHENTHICATE " + tcpServ.userId);
                                end();
                            } else {
                                tcpServ.getCommandHandler().sendCommand(ServerCommand.AUTH_OK, new String[]{tcpServ.pubId, tcpServ.userName});
                                System.out.println("AUTHENTHICATED " + tcpServ.userName);
                            }
                            break;
                        case AUTH_LOGOUT:
                            end();
                            break;

                        case MAP_LISTREQ:
                            System.out.println("Listrequest detected");  ///////!!!!!!!!!!!!!!!!!!!!!!
                            String[] maps = MapHelper.listMaps();
                            String lmaps;
                            lmaps = maps[0];
                            for (String currMap : maps) {
                                if (currMap.equals(maps[0])) {
                                    continue;
                                }
                                lmaps += ",";
                                lmaps += currMap;
                            }
                            //tcpServ.out.println(server.keyword + server.infoSeperator + "MAP-LISTOK" + server.infoSeperator + lmaps);
                            tcpServ.getCommandHandler().sendCommand(ServerCommand.MAP_LISTOK, new String[]{lmaps});
                            break;

                        case OGAME_CREATE:
                            if (server.games.containsKey(packageContent[2])) {
                                // tcpServ.out.println(server.keyword + server.infoSeperator + "OGAME-ERR" + server.infoSeperator + "true" + server.infoSeperator + "namealreadyexists");
                                tcpServ.getCommandHandler().sendCommand(ServerCommand.OGAME_ERR, new String[]{"1", "nameallreadyexist"});
                            } else {
                                OnlGame onlGame = new OnlGame(server, packageContent[2], Integer.parseInt(packageContent[3]), packageContent[4], Double.parseDouble(packageContent[5]), (int) Double.parseDouble(packageContent[5]), packageContent[6], tcpServ.pubId, packageContent[7]);
                                server.games.put(packageContent[2], onlGame);
                                // (new Thread(onlGame)).start();
                            }
                            break;
                        case OGAME_JOIN:
                            if (!server.games.containsKey(packageContent[2])) {
                                //tcpServ.out.println(server.keyword + server.infoSeperator + "OGAME-ERR" + server.infoSeperator + "true" + server.infoSeperator + "namedoesntexist");
                                tcpServ.getCommandHandler().sendCommand(ServerCommand.OGAME_ERR, new String[]{"1", "namedoesntexist"});
                            } else {
                                server.games.get(packageContent[2]).addPlayer(tcpServ.pubId, packageContent[3]);
                            }
                            break;
                        case OGAME_KEYPRESS:
                            server.games.get(packageContent[3]).players.get(packageContent[2]).handleKeyPress(packageContent[4]);
                            break;
                        case OGAME_KEYRELEASE:
                            server.games.get(packageContent[3]).players.get(packageContent[2]).handleKeyRelease(packageContent[4]);
                            break;
                    }

                } catch (Exception e) {
                    System.err.println("Invalid package received");
                    e.printStackTrace();
                    System.err.println("Invalid package recieved by server:" + line);
                }
                /**
                 * if(line.startsWith("JUMPNRUN ")){ System.out.println(line);
                 * }else{ System.err.println(line); }
                 *
                 *
                 * if(line.equals("JUMPNRUN test")){
                 * tcpServ.out.println("JUMPNRUN test"); } *
                 */
            }
        } catch (IOException ex) {
            System.err.println("IOException: " + ex);
        } catch (NullPointerException ex) {
            end();
        }
    }

    private void end() {
        server.tcpServer.remove(tcpServ);
        tcpServ.tcpReceiver.stop();
    }

    NetworkTCPReceiverServer(Server serv, TCPServer totcp) {
        server = serv;
        tcpServ = totcp;
    }
}
