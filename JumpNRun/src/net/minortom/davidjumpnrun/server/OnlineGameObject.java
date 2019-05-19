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
public interface OnlineGameObject {
    public int getObjectTypeAsInt();
    public double getXPos();
    public double getYPos();
    public int getAnimationStateAsInt();
    public String getObjectId();
}
