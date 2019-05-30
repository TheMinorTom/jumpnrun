/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minortom.davidjumpnrun.server;

import javafx.geometry.Rectangle2D;
import net.minortom.davidjumpnrun.netcode.GameObjectType;

/**
 *
 * @author DavidPrivat
 */
public class OnlineCounterLabel extends RemoteUpdatableObject{

    private double val;
    private boolean needsUpdate;
    private GameObjectType type;

    public OnlineCounterLabel(String objId, GameObjectType type, double startVal, OnlGame game) {
        super(objId, Rectangle2D.EMPTY, type, 0, 0, 0, 0, 0, 0, game, null, (int)startVal);
        val = startVal;
        needsUpdate = true;
        this.type = type;
    }
    
    public OnlineCounterLabel(String objId, GameObjectType type, double startVal, double x, double y, OnlGame game) {
        super(objId, Rectangle2D.EMPTY, type, x, y, 0, 0, 0, 0, game, null, (int)startVal);
        val = startVal;
        needsUpdate = true;
        this.type = type;
    }

    public void addVal(double addVal) {
        if (((int) val) != ((int) (val + addVal))) {
            needsUpdate = true;
        }
        val += addVal;

    }

    public int getValInt() {
        return (int) val;
    }
    
    public String getValIntString() {
        return String.valueOf((int)val);
    }

    public void setUpdated() {
        needsUpdate = false;
    }

    public boolean needsUpdate() {
        return needsUpdate;
    }
    
    public GameObjectType getType() {
        return type;
    }
    
    public String getTypeString() {
        return String.valueOf(type.ordinal());
    }
    
    public void setVal(double val) {
        this.val = val;
        needsUpdate = true;
    }
    
    public enum CounterType {

        GAME_TIMER,
        KD_COUNTER;
    }
}
