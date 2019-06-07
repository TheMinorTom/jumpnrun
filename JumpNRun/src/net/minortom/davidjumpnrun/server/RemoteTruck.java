/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minortom.davidjumpnrun.server;

import java.util.Vector;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import net.minortom.davidjumpnrun.netcode.GameObjectType;
import net.minortom.davidjumpnrun.server.OnlGame;
import net.minortom.davidjumpnrun.server.RemoteObject;
import net.minortom.davidjumpnrun.server.RemotePlayer;
import worldeditor.Block;
import worldeditor.IO;

/**
 *
 * @author David
 */
public class RemoteTruck extends RemoteObject {

    private double xPos, yPos, xSpd, ySpd, yAcc, width, height;
    private final String imageSource = "sprites/TruckGes.png";
    private RemotePlayer owner;
    private AnimationState animationState;
    private double existTimer;
    private final double lifeTime = 10;
    private final OnlGame game;

    public RemoteTruck(double x, double y, double xSpd, double ySpd, RemotePlayer owner, OnlGame game, String objId) {
        super(jumpnrun.Truck.AnimationState.LEFT.getRect(), GameObjectType.TRUCK, objId);
        existTimer = 0;
        xPos = x;
        yPos = y;
        this.owner = owner;
        if (xSpd < 0) {
            setAnimationState(1);
            animationState = AnimationState.LEFT;
        } else {
            setAnimationState(0);
            animationState = AnimationState.RIGHT;
        }
        yAcc = 5000;
        this.game = game;
        width = jumpnrun.Truck.AnimationState.LEFT.getRect().getWidth();
        height = jumpnrun.Truck.AnimationState.LEFT.getRect().getHeight();
        this.xSpd = xSpd;
        this.ySpd = ySpd;
    }

    public void update(double timeElapsed) {
        if (xSpd != 0) {
            ySpd += yAcc * timeElapsed;
            yPos += ySpd * timeElapsed;
            if (OnlGame.worldCollisionCheck(game.worldVector, xPos, yPos, width, height, game.blockSize)) {
                yPos -= ySpd * timeElapsed;
                ySpd = 0;
            }

            xPos += xSpd * timeElapsed;

            if (OnlGame.worldCollisionCheck(game.worldVector, xPos, yPos, width, height, game.blockSize)) {
                xPos -= xSpd * timeElapsed;
                xSpd *= -1;
                if (animationState.equals(AnimationState.LEFT)) {
                    animationState = AnimationState.RIGHT;
                    setAnimationState(0);
                } else {
                    animationState = AnimationState.LEFT;
                    setAnimationState(1);
                }
                xPos += xSpd * timeElapsed;
                if (OnlGame.worldCollisionCheck(game.worldVector, xPos, yPos, width, height, game.blockSize)) {
                    xSpd = 0;
                }
                if (animationState == AnimationState.LEFT) {
                    setAnimationState(1);
                } else if (animationState == AnimationState.RIGHT) {
                    setAnimationState(0);
                }
            }
        }

        game.players.forEach((String id, RemotePlayer p) -> {
            if (!p.equals(owner)) {
                if ((!p.isRespawning()) && (!p.isDead())) {
                    if (owner.intersects(xPos, yPos, width, height, prefHeight(getXPos()), p.getYPos(), RemotePlayer.width, RemotePlayer.height)) {
                        p.hitten();
                        owner.incrementKills();
                    }
                }
            }
        });

        existTimer += timeElapsed;
        if (existTimer > lifeTime) {
            game.removeTruck(this);
            owner.removeTruck(this);
        }
    }

    @Override
    public double getXPos() {
        return xPos;
    }

    @Override
    public double getYPos() {
        return yPos;
    }

    public enum AnimationState {

        RIGHT(0, 0, 70, 45),
        LEFT(71, 0, 70, 45);
        private Rectangle2D r;

        AnimationState(double x, double y, double w, double h) {
            r = new Rectangle2D(x, y, w, h);
        }

        public Rectangle2D getRect() {
            return r;
        }

    }

}
