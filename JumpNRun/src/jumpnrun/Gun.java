/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import worldeditor.IO;

/**
 *
 * @author Norbert
 */
public class Gun extends ImageView implements OnlineUpdatableObject{

    private final static String imageSource = "sprites/GunGes.png";
    private boolean facingLeft, shootGenerated;
    private double xPos, yPos;
    private Rectangle2D currViewport;
    Protagonist owner;

    Gun(Protagonist owner) {
        shootGenerated = false;
        setImage(new Image(ConfigManager.getFileStream(imageSource)));
        setVisible(false);
        setX(200);
        setY(200);
        facingLeft = true;
        setViewport(AnimationState.LEFT.getRect());
        this.owner = owner;

    }
    
    Gun() {
        shootGenerated = false;
        setImage(new Image(ConfigManager.getFileStream(imageSource)));
        setVisible(false);
        setX(200);
        setY(200);
        facingLeft = true;
        setViewport(AnimationState.LEFT.getRect());
    }

    public void updateShoot(double shootTimer) {
        if (shootTimer > 1) {

            if (facingLeft) {
                setViewport(AnimationState.LEFT_SHOOT.getRect());
                if (!shootGenerated) {
                    shootGenerated = true;
                    Shoot shoot = new Shoot(getX(), getY(), 1000,0,false, owner);
                    JumpNRun.addUpdatable(shoot);
                    JumpNRun.addNode(shoot);
                }

            } else {
                setViewport(AnimationState.RIGHT_SHOOT.getRect());
                if (!shootGenerated) {
                    shootGenerated = true;
                    Shoot shoot = new Shoot(getX() + 40, getY(), 1000, 0, true, owner);
                    JumpNRun.addUpdatable(shoot);
                    JumpNRun.addNode(shoot);
                }
            }
        } else {
            shootGenerated = false;
        }
    }

    public void updateMachinePistol(double shootTimer) {
        if (shootTimer > 0.5) {
            if (facingLeft) {
                setViewport(AnimationState.LEFT_SHOOT.getRect());
                if (((int) (shootTimer * 100)) % 10 == 0) {
                    Shoot shoot = new Shoot(getX()-10, getY(), 700 ,0 ,false, owner);
                    JumpNRun.addUpdatable(shoot);
                    JumpNRun.addNode(shoot);
                }

            } else {
                setViewport(AnimationState.RIGHT_SHOOT.getRect());
                if (((int) (shootTimer * 100)) % 10 == 0) {
                    Shoot shoot = new Shoot(getX() + 50, getY(), 700,0,true, owner);
                    JumpNRun.addUpdatable(shoot);
                    JumpNRun.addNode(shoot);
                }

            }
        }

    }

    public void setFacingLeft(boolean b) {
        facingLeft = b;
        if (facingLeft) {
            setViewport(AnimationState.LEFT.getRect());
        } else {
            setViewport(AnimationState.RIGHT.getRect());
        }
    }

    public boolean getFacingLeft() {
        return facingLeft;
    }

    @Override
    public void updatePos(double x, double y, int animationState) {
        xPos = x;
        yPos = y;
        currViewport = AnimationState.values()[animationState].getRect();
    }

    @Override
    public void updateGraphic(double scrollX, double scrollY) {
        setX(xPos + scrollX);
        setY(yPos + scrollY);
        setViewport(currViewport);
    }

    public enum AnimationState {
        LEFT(0, 0, 55, 10),
        LEFT_SHOOT(56, 0, 56, 10),
        RIGHT_SHOOT(114, 0, 56, 10),
        RIGHT(172, 0, 55, 10);
        private Rectangle2D r;

        AnimationState(double x, double y, double w, double h) {
            r = new Rectangle2D(x, y, w, h);
        }

        public Rectangle2D getRect() {
            return r;
        }
    }
    
    public Protagonist getOwner() {
        return owner;
    }

}
