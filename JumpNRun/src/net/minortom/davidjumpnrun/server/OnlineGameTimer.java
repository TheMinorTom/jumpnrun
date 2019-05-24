/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minortom.davidjumpnrun.server;

import net.minortom.davidjumpnrun.netcode.GameObjectType;

/**
 *
 * @author DavidPrivat
 */
public class OnlineGameTimer extends RemoteObject{
    private int minutes, seconds;
    private double time;
    private boolean isCountdown;
    
    public OnlineGameTimer(double x, double y, double w, double h, GameObjectType objectType, String objectId, boolean isCountdown, double startTime) {
        super(x, y, w, h, objectType, objectId);
        minutes = 0;
        seconds = 0;
        time = startTime;
        this.isCountdown = isCountdown;
    }
    
}
