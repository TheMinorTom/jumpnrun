/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minortom.davidjumpnrun.server;

import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;
import net.minortom.davidjumpnrun.netcode.GameObjectType;


/**
 *
 * @author DavidPrivat
 */
public class RemoteObject extends Rectangle implements OnlineGameObject{
    private int animationStateInt;  // 0 = default; -1 = invisible
    private final GameObjectType objectType;
    private final String objectId;
    
    public RemoteObject(double x, double y, double w, double h, GameObjectType objectType, String objectId) {
        super(x,y,w,h);
        animationStateInt = 0;
        this.objectType = objectType;
        this.objectId = objectId;
    }
    
    public RemoteObject(Rectangle2D shape, GameObjectType objectType, String objectId) {
        super(shape.getMinX(), shape.getMinY(), shape.getWidth(), shape.getHeight());
        animationStateInt = 0;
        this.objectType = objectType;
        this.objectId = objectId;
    }
    
    
    public void setAnimationState(int a) {
        animationStateInt = a;
    }
    
    public int getAnimationState() {
        return animationStateInt;
    }

    @Override
    public int getObjectTypeAsInt() {
        return objectType.ordinal();
    }

    @Override
    public double getXPos() {
        return getX();
    }

    @Override
    public double getYPos() {
        return getY();
    }

    @Override
    public int getAnimationStateAsInt() {
        return animationStateInt;
    }

    @Override
    public String getObjectId() {
        return objectId;
    }
}
