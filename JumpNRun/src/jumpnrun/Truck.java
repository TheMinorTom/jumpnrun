/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import java.util.Vector;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import worldeditor.Block;
import worldeditor.IO;

/**
 *
 * @author Norbert
 */
class Truck extends ImageView implements Updatable {

    private double xPos, yPos, xSpd, ySpd, yAcc;
    private final String imageSource = "sprites/TruckGes.png";
    private Protagonist owner;
    private AnimationState animationState;
    private double existTimer;
    private final double lifeTime = 8;

    Truck(double x, double y, boolean facingRight, Protagonist owner) {
        setImage(new Image(ConfigManager.getFileStream(imageSource)));
        existTimer = 0;
        xPos = x;
        yPos = y;
        this.owner = owner;
        if (facingRight) {
            xSpd = 200;
            setViewport(AnimationState.RIGHT.getRect());
            animationState = AnimationState.RIGHT;
        } else {
            xSpd = -200;
            setViewport(AnimationState.LEFT.getRect());
            animationState = AnimationState.LEFT;
        }

        yAcc = 5000;
        ySpd = 0;
    }

    public void update(double timeElapsed, Vector<Vector<Block>> world, Protagonist prot1, Protagonist prot2, Vector<PowerupCollect> powerups) {
        Protagonist otherProt;
        setX(xPos);
        setY(yPos);
        if (prot1.equals(owner)) {
            otherProt = prot2;
        } else {
            otherProt = prot1;
        }

        ySpd += yAcc * timeElapsed;
        yPos += ySpd * timeElapsed;
        setY(yPos);
        if (collisionCheck(world)) {
            yPos -= ySpd * timeElapsed;
            ySpd = 0;
        }

        xPos += xSpd * timeElapsed;
        setY(yPos);
        setX(xPos);


        if (collisionCheck(world)) {
            xPos -= xSpd*timeElapsed;
            xSpd *= -1;
            
            if (animationState == AnimationState.LEFT) { 
                animationState = AnimationState.RIGHT;
                setViewport(AnimationState.RIGHT.getRect());

            } else if(animationState == AnimationState.RIGHT) {
                animationState = AnimationState.LEFT;
                setViewport(animationState.LEFT.getRect());

            } 
        }

        if (getBoundsInParent().intersects(otherProt.getBoundsInParent())) {
            otherProt.hitten();
        }

        updateExist(timeElapsed);

    }

    private void updateExist(double timeElapsed) {
        existTimer += timeElapsed;
        if (existTimer > lifeTime) {
            JumpNRun.removeNode(this);
            JumpNRun.removeUpdatable(this);
        } else if (existTimer > lifeTime - 0.2) {
            setOpacity(0.2);
        } else if (existTimer > lifeTime - 0.4) {
            setOpacity(0.4);
        } else if (existTimer > lifeTime - 0.6) {
            setOpacity(0.6);
        } else if (existTimer > lifeTime - 0.8) {
            setOpacity(0.8);
        }

    }

    public boolean collisionCheck(Vector<Vector<Block>> worldVec) {
        for (int i = 0; i < worldVec.size(); i++) {
            for (int j = 0; j < worldVec.get(i).size(); j++) {
                if (worldVec.get(i).get(j) != null) {
                    Block b = worldVec.get(i).get(j);
                    if (b.getIsSolid() && this.getBoundsInParent().intersects(b.getBoundsInParent())) {
                        return true;
                    }
                }
            }
        }
        return false;
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
