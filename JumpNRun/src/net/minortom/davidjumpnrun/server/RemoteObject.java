/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minortom.davidjumpnrun.server;

import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;


/**
 *
 * @author DavidPrivat
 */
public class RemoteObject extends Rectangle{
    private int animationStateInt;  // 0 = default; -1 = invisible
    
    public RemoteObject(double x, double y, double w, double h) {
        super(x,y,w,h);
        animationStateInt = 0;
    }
    
    public RemoteObject(Rectangle2D shape) {
        super(shape.getMinX(), shape.getMinY(), shape.getWidth(), shape.getHeight());
        animationStateInt = 0;
    }
    
    
    public void setAnimationState(int a) {
        animationStateInt = a;
        ;
    }
    
    public int getAnimationState() {
        return animationStateInt;
    }
}
