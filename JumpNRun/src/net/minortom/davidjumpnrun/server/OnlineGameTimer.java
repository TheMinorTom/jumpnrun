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
public class OnlineGameTimer extends RemoteObject {

    private double time;
    private boolean isCountdown;

    public OnlineGameTimer(double x, double y, double w, double h, GameObjectType objectType, String objectId, boolean isCountdown, double startTime) {
        super(x, y, w, h, objectType, objectId);

        time = startTime;
        this.isCountdown = isCountdown;
    }

    public void update(double timeElapsedSeconds) {
        if (isCountdown) {
            time -= timeElapsedSeconds;
        } else {
            time += timeElapsedSeconds;
        }
    }
    
    @Override
    public int getAnimationState() {
        return ((int)time);
    }


}
