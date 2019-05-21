/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minortom.davidjumpnrun.server;

import javafx.geometry.Rectangle2D;
import jumpnrun.Gun;
import jumpnrun.JumpNRun;
import jumpnrun.Shoot;
import net.minortom.davidjumpnrun.netcode.GameObjectType;

/**
 *
 * @author DavidPrivat
 */
public class RemoteUpdatableObject extends RemoteObject{
    private double xSpd, ySpd, xAcc, yAcc;
    private OnlGame game;
    private boolean isDamaging;
    private RemotePlayer owner;


    public RemoteUpdatableObject(String objectId, Rectangle2D rect, GameObjectType objectType, OnlGame game) {
        super(rect, objectType, objectId);
        this.xSpd = 0;
        this.ySpd = 0;
        this.xAcc = 0;
        this.yAcc = 0;  
        this.game = game;
        isDamaging = false;
        owner = null;
    }
    
    public RemoteUpdatableObject(String objectId, Rectangle2D rect, GameObjectType objectType, double xSpd, double ySpd, double xAcc, double yAcc, OnlGame game) {
        super(rect, objectType, objectId);
        this.xSpd = xSpd;
        this.ySpd = ySpd;
        this.xAcc = xAcc;
        this.yAcc = yAcc;  
        this.game = game;
        isDamaging = false;
        setAnimationState(0);
        owner = null;
    }
    
    public RemoteUpdatableObject(String objectId, Rectangle2D rect, GameObjectType objectType, double xPos, double yPos, double xSpd, double ySpd, double xAcc, double yAcc, OnlGame game, RemotePlayer owner, int animationState) {
        super(rect, objectType, objectId);
        this.xSpd = xSpd;
        this.ySpd = ySpd;
        this.xAcc = xAcc;
        this.yAcc = yAcc;  
        this.game = game;
        setX(xPos);
        setY(yPos);
        isDamaging = true;
        this.owner = owner;
        setAnimationState(animationState);
    }
    
    public void update(double timeElapsed) {
        
        xSpd += timeElapsed * xAcc;
        ySpd += timeElapsed * yAcc;
        
        setX(getX() + (xSpd * timeElapsed));
        setY(getY() + (ySpd * timeElapsed));       

    }
    
    public RemotePlayer getOwner() {
        return owner;
    }
}
