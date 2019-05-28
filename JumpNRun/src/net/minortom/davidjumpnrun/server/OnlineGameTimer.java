/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minortom.davidjumpnrun.server;

import javafx.geometry.Rectangle2D;
import jumpnrun.Updatable;
import net.minortom.davidjumpnrun.netcode.GameObjectType;

/**
 *
 * @author DavidPrivat
 */
public class OnlineGameTimer extends RemoteUpdatableObject {

    private double time;
    private boolean isCountdown;

    public OnlineGameTimer(String objectId, boolean isCountdown, double startTime, OnlGame game) {
        super(objectId, new Rectangle2D(0,0,0,0), GameObjectType.GAMETIMER, game);

        time = startTime;
        this.isCountdown = isCountdown;
    }

    @Override
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
    
    @Override
    public int getAnimationStateAsInt() {
        return ((int)time);
    }


}
