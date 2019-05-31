/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minortom.davidjumpnrun.netcode;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static net.minortom.davidjumpnrun.netcode.ServerCommand.*;
import net.minortom.davidjumpnrun.server.RemotePlayer;
import net.minortom.davidjumpnrun.server.Server;

/**
 *
 * @author d.betko
 */
public class OnlineCommandHandler {

    public PrintWriter out;
    private String message = "";
    private HashMap<String, Integer> killsHM;

    public OnlineCommandHandler(PrintWriter out) {
        this.out = out;
    }

    public void sendCommand(ServerCommand command, String[] args) {
        message = "";
        message += NetworkManager.keyword + NetworkManager.infoSeperator + String.valueOf(command.ordinal());
        for (String s : args) {
            message += NetworkManager.infoSeperator + s;
        }

        out.println(message);
        //if (!command.equals(ServerCommand.OGAME_UPDATEOBJECT)) {
        System.out.println(message);
        //}

    }

    public void sendUpdateCommand(ObservableList<String[]> objArgs) {
        message = "";
        message += NetworkManager.keyword + NetworkManager.infoSeperator + String.valueOf(OGAME_UPDATEOBJECTS.ordinal()) + NetworkManager.infoSeperator;

        objArgs.forEach((String[] currArgs) -> {

            for (String currArg : currArgs) {
                message += currArg + NetworkManager.subArgsSeperator;
            }

            message += NetworkManager.differentObjectsSeperator;
        });

        out.println(message);
    }

    public void sendEndGame(HashMap<String, RemotePlayer> players) {
        message = "";
        message += NetworkManager.keyword + NetworkManager.infoSeperator + String.valueOf(OGAME_ENDGAME.ordinal()) + NetworkManager.infoSeperator;
        sortPlayers(players).forEach((String key, RemotePlayer player) -> {
            message += player.getPlacement() + NetworkManager.subArgsSeperator;
            message += player.name + NetworkManager.subArgsSeperator;
            message += String.valueOf(player.getKills()) + NetworkManager.subArgsSeperator;
            message += String.valueOf(player.getDeaths()) + NetworkManager.subArgsSeperator;
            message += NetworkManager.differentObjectsSeperator;
        });

        out.println(message);
    }

    public HashMap<String, RemotePlayer> sortPlayers(HashMap<String, RemotePlayer> unsortedMap) {
        killsHM = new HashMap<>();
        unsortedMap.forEach((String key, RemotePlayer p) -> {
            killsHM.put(key, new Integer(p.getKills()));
        });
        List<Map.Entry<String, Integer>> list
                = new LinkedList<Map.Entry<String, Integer>>(killsHM.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                    Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap  
        int counter = unsortedMap.size();
        HashMap<String, RemotePlayer> sortedMap = new HashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            sortedMap.put(aa.getKey(), unsortedMap.get(aa.getKey()));
            sortedMap.get(aa.getKey()).setPlacement(String.valueOf(counter));
            counter--;
        }
        return sortedMap;
    }
}
