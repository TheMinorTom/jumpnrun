/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import net.minortom.davidjumpnrun.netcode.NetworkManager;

public class Server {

    public static String storageLocation;
    public static Server server;

    public ServerMysqlConnection dbConn;

    public final String infoSeperator = NetworkManager.infoSeperator;
    public final String keyword = NetworkManager.keyword;

    public HashMap<String, OnlGame> games;

    public static void main(String[] args) {
        new Server(args);
    }

    public HashMap<String, TCPServer> tcpServer;
    public int tcpPort;
    public boolean isDatabaseBlocked;
    
    public final boolean isLocal;

    public Server(String[] args) {
        /* try{
         tcpPort = new Integer(args[0]);
         }catch(Exception e){
         System.err.println("Invalid argument: Port");
         System.exit(1);
         } */
        // TODO: Remove before release

        if (false) {
            storageLocation = "H:\\Eigene Dateien\\Informatik1819\\meine_programme\\JumpNRun\\jumpnrun-master\\jumpnrun-master\\JumpNRun\\appdata\\";
        } else {
            storageLocation = ConfigManager.getStorageLocation();
        }

        Server.server = this;
        System.out.println("Server Hello World");

        if (args.length != 0 && (!args[args.length - 1].equals("local"))) {     //Is local??
            isLocal = false;
            Scanner in = new Scanner(System.in);
            System.out.println("Please enter the DB Connection String");
            String dbString = in.nextLine();
            String[] dbCreds = dbString.split("!");
            try {
                if (true) {
                    int i = 1 / 0;
                }
                dbConn = new ServerMysqlConnection(dbCreds[0], dbCreds[1], dbCreds[2], dbCreds[3]);
                isDatabaseBlocked = false;
            } catch (Exception e) {
                isDatabaseBlocked = true;
                e.printStackTrace();
                System.err.println("No database-connection");
            }
        } else {
            isLocal = true;
            isDatabaseBlocked = true;
        }
        System.out.println("Now starting");

        tcpPort = 26656;
        tcpServer = new HashMap<>();
        TCPServer.init(tcpPort);

        games = new HashMap<>();

        while (true) {
            String pubId = "P" + Integer.toString((int) (Math.random() * 10000000));
            tcpServer.put(pubId, new TCPServer(this, pubId));
        }
    }

    public static String getMapFolder() {
        return storageLocation + "worlds" + File.separator;
    }

    public static String getBlocksFolder() {
        return storageLocation + "sprites" + File.separator + "blocks" + File.separator;
    }

    public static String getStorageLocation() {
        return ConfigManager.getStorageLocation();
    }
}
