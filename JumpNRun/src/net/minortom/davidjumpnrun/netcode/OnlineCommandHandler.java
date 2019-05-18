/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minortom.davidjumpnrun.netcode;

import java.io.PrintWriter;
import net.minortom.davidjumpnrun.server.Server;

/**
 *
 * @author d.betko
 */
public class OnlineCommandHandler {

    public PrintWriter out;

    public OnlineCommandHandler(PrintWriter out) {
        this.out = out;
    }

    public void sendCommand(ServerCommand command, String[] args) {
        String message = "";
        message += NetworkManager.keyword + NetworkManager.infoSeperator + String.valueOf(command.ordinal());
        for (String s : args) {
            message += NetworkManager.infoSeperator + s;
        }

        out.println(message);
        if (!command.equals(ServerCommand.OGAME_UPDATEPROT)) {
            System.out.println(message);
        }

    }
}
