/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import java.util.Vector;
import worldeditor.Block;

/**
 *
 * @author Norbert
 */
public class PowerupCollect extends Powerup implements Updatable, OnlineUpdatableObject {

    private double timer;
    private IconViewport viewport;
    private double xPos = 0;
    private double yPos = 0;

    public PowerupCollect() {
        super(Powerup.IconViewport.DOUBLE_SPEED);
        timer = 0;
        viewport = IconViewport.DOUBLE_SPEED;
    }

    public IconViewport getIcon() {
        return viewport;
    }

    @Override
    public void update(double timeElapsed, Vector<Vector<Block>> world, Protagonist prot1, Protagonist prot2, Vector<PowerupCollect> powerupCollects) {
        timer += timeElapsed * 5;
        int timerInt = (int) timer;
        int currIcon = timerInt % 3;

        switch (currIcon) {
            case 0:
                setViewport(Powerup.IconViewport.DOUBLE_SPEED.getRect());
                viewport = Powerup.IconViewport.DOUBLE_SPEED;
                break;
            case 1:
                setViewport(Powerup.IconViewport.MACHINE_PISTOL.getRect());
                viewport = Powerup.IconViewport.MACHINE_PISTOL;
                break;
            case 2:
                setViewport(Powerup.IconViewport.TRUCK.getRect());
                viewport = Powerup.IconViewport.TRUCK;
                break;
        }
    }
    
    public void updateViewportOnline (double timeElapsed) {
        update(timeElapsed, null, null, null, null);
    }

    @Override
    public void updatePos(double x, double y, int animationState) {
        xPos = x;
        yPos = y;
    }

    @Override
    public void updateGraphic(double xScroll, double yScroll) {
        setLayoutX(xPos + xScroll);
        setLayoutY(yPos + yScroll);
    }
}
