/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minortom.davidjumpnrun.netcode;

import java.io.PrintWriter;
import javafx.collections.ObservableList;
import static net.minortom.davidjumpnrun.netcode.ServerCommand.*;
import net.minortom.davidjumpnrun.server.Server;

/**
 *
 * @author d.betko
 */
public class OnlineCommandHandler {

    public PrintWriter out;
    private String message = "";

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

    public void sendUpdateObjectsCommand(ObservableList<String[]> args) {
        message = "";
        message += NetworkManager.keyword + NetworkManager.infoSeperator + String.valueOf(OGAME_UPDATEOBJECTS.ordinal()) + NetworkManager.infoSeperator;

        args.forEach((String[] currArgs) -> {

            for (String currArg : currArgs) {
                message += currArg + NetworkManager.subArgsSeperator;
            }

            message += NetworkManager.differentObjectsSeperator;
        });
        out.println(message);
    }
}
