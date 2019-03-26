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
                            tcpServ.pubId = server.tcpServer.indexOf(tcpServ);
                            tcpServ.token = Integer.toString((int) (Math.random()*10000));
                            tcpServ.out.println(server.keyword + server.infoSeperator + "AUTH-OK" + server.infoSeperator + tcpServ.pubId + server.infoSeperator + tcpServ.token);
                            System.out.println("AUTHENTHICATED " + tcpServ.userName);
                        } else if (type.equals("LOGOUT")) {
                            end();
                        }
                    } else if (packageContent[1].startsWith("MAP")){
                        String type = packageContent[1].split("-")[1];
                        if(type.equals("REQ")){
                            
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e){
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
