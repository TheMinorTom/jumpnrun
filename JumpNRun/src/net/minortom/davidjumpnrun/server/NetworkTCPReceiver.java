/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

import java.io.IOException;

public class NetworkTCPReceiver extends Thread{
    Server server;
    TCPServer tcpServ;
    
    @Override
    public void run(){
        String line;
        try {
            while ((line = tcpServ.in.readLine()) != null){
                System.out.println(line);
                try{
                    String[] packageContent = line.split(server.infoSeperator);
                    if(!packageContent[0].equals(server.keyword)){
                        System.err.println("Invalid package received, "+ server.keyword +": " + packageContent[0]);
                        continue;
                    }
                    if(packageContent[1].startsWith("AUTH")){
                        String type = packageContent[1].split("-")[1];
                        if(type.equals("REQ")){
                            tcpServ.userName = packageContent[2];
                            tcpServ.pass = packageContent[3];
                            tcpServ.token = "T" + Integer.toString((int) (Math.random()*10000000));
                            tcpServ.out.println(server.keyword + server.infoSeperator + "AUTH-OK" + server.infoSeperator + tcpServ.pubId + server.infoSeperator + tcpServ.token);
                            System.out.println("AUTHENTHICATED " + tcpServ.userName);
                        } else if (type.equals("LOGOUT")) {
                            end();
                        }
                    } else if (packageContent[1].startsWith("MAP")){
                        String type = packageContent[1].split("-")[1];
                        if(type.equals("LISTREQ")){
                            String[] maps = MapHelper.listMaps();
                            String lmaps;
                            lmaps = maps[0];
                            for(String currMap : maps){
                                if (currMap.equals(maps[0]) ){
                                    continue;
                                }
                                lmaps += ",";
                                lmaps += currMap;
                            }
                            String toPrint = server.keyword + server.infoSeperator + "MAP-LISTOK" + server.infoSeperator + lmaps;
                            tcpServ.out.println(toPrint);
                        }
                    } else if (packageContent[1].startsWith("OGAME")){
                        String type = packageContent[1].split("-")[1];
                        if(type.equals("CREATE")){
                            if(server.games.containsKey(packageContent[2])){
                                tcpServ.out.println(server.keyword + server.infoSeperator + "OGAME-ERR" + server.infoSeperator + "true" + server.infoSeperator + "namealreadyexists");
                            } else {
                                OnlGame onlGame = new OnlGame(server, packageContent[2], Integer.parseInt(packageContent[3]), packageContent[4], Double.parseDouble(packageContent[5]), (int)Double.parseDouble(packageContent[5]), packageContent[6], tcpServ.pubId, packageContent[7]);
                                server.games.put(packageContent[2], onlGame);
                                (new Thread(onlGame)).start();
                            }
                        } else if(type.equals("JOIN")){
                            if(!server.games.containsKey(packageContent[2])){
                                tcpServ.out.println(server.keyword + server.infoSeperator + "OGAME-ERR" + server.infoSeperator + "true" + server.infoSeperator + "namedoesntexist");
                            } else {
                                server.games.get(packageContent[2]).addPlayer(tcpServ.pubId, packageContent[3]);
                            }
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
                if(line.startsWith("JUMPNRUN ")){
                    System.out.println(line);
                }else{
                    System.err.println(line);
                }
                
                
                if(line.equals("JUMPNRUN test")){
                    tcpServ.out.println("JUMPNRUN test");
                } **/
            }
        } catch (IOException ex) {
            System.err.println("IOException: "+ ex);
        } catch (NullPointerException ex) {
            end();
        }
    }
    
    private void end(){
        server.tcpServer.remove(tcpServ);
        tcpServ.tcpReceiver.stop();
    }
    
    NetworkTCPReceiver(Server serv, TCPServer  totcp){
        server = serv;
        tcpServ = totcp;
    }
}
