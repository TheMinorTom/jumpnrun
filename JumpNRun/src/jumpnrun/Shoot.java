/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import java.util.Vector;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import worldeditor.Block;
import worldeditor.IO;

/**
 *
 * @author Norbert
 */
public class Shoot extends ImageView implements Updatable, OnlineUpdatableObject {

    double xPos, yPos, xSpd, ySpd, breakAccX, gravAccY, lastXPos, lastYPos;

    private final static String imageSource = "sprites/ShootGes.png";

    public Shoot(double x, double y, double xSpeed, double ySpeed, boolean facingRight) {
        Image image = new Image(ConfigManager.getFileStream(imageSource));
        setImage(image);
        getTransforms().add(new Rotate());
        setViewport(AnimationState.RIGHT.getRect());
        if (facingRight) {

            xSpd = xSpeed;
        } else {
            xSpd = -1 * xSpeed;
        }
        ySpd = ySpeed;

        xPos = x;
        yPos = y;
        breakAccX = 200;
        gravAccY = 200;

    }

    public Shoot() {
        Image image = new Image(ConfigManager.getFileStream(imageSource));
        setImage(image);
        getTransforms().add(new Rotate(0, AnimationState.RIGHT.getRect().getWidth()/2, AnimationState.RIGHT.getRect().getHeight()/2));
        breakAccX = 200;
        gravAccY = 200;
        setViewport(AnimationState.RIGHT.getRect());

    }

    public void updateRotation() {
        ((Rotate) getTransforms().get(0)).setAngle(calcRotation(xPos - lastXPos, yPos - lastYPos));

        lastXPos = xPos;
        lastYPos = yPos;
    }
    
    @Override
    public void update(double timeElapsedSeconds, Vector<Vector<Block>> worldVec, Protagonist protOne, Protagonist protTwo, Vector<PowerupCollect> powerupCollects) {
        double xSpdAdd = breakAccX * timeElapsedSeconds;
        double ySpdAdd = gravAccY * timeElapsedSeconds;

        if (xSpd > xSpdAdd) {
            xSpd -= xSpdAdd;
        } else if (xSpd < -1 * xSpdAdd) {
            xSpd += xSpdAdd;
        } else {
            JumpNRun.removeNode(this);
            JumpNRun.removeUpdatable(this);
        }
        updateRotation();
        ySpd += ySpdAdd;
        xPos += xSpd * timeElapsedSeconds;
        yPos += ySpd * timeElapsedSeconds;
        setLayoutX(xPos);
        setLayoutY(yPos);

        if (collisionCheck(worldVec, protOne, protTwo)) {
            JumpNRun.removeNode(this);
            JumpNRun.removeUpdatable(this);
        }
    }

    @Override
    public void updatePos(double x, double y, int animationState) {
        xPos = x;
        yPos = y;

    }

    @Override
    public void updateGraphic(double scrollX, double scrollY) {
        setLayoutX(xPos + scrollX);
        setLayoutY(yPos + scrollY);
        updateRotation();
    }

    private double calcRotation(double deltaX, double deltaY) {
        /*
         double tot1 = Math.sqrt(x1*x1+y1*y1);
         double tot2 = Math.sqrt(x2*x2+y2*y2);
         double cosAng = (x1*x2+y1*y2)/(tot1*tot2);
         double ang = Math.acos(cosAng);
         return ang;
         */
        double tot = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double cosAng = (deltaX) / tot;
        double ang = (Math.toDegrees(Math.acos(cosAng)));
        return ang;
    }

    public boolean collisionCheck(Vector<Vector<Block>> worldVec, Protagonist protOne, Protagonist protTwo) {
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
        if (getBoundsInParent().intersects(protOne.getBoundsInParent())) {
            protOne.hitten();
            return true;
        }
        if (getBoundsInParent().intersects(protTwo.getBoundsInParent())) {
            protTwo.hitten();
            return true;
        }
        return false;
    }

    public void setRight() {
        setViewport(AnimationState.RIGHT.getRect());
    }

    public void setLeft() {
        setViewport(AnimationState.LEFT.getRect());
    }

    public enum AnimationState {

        LEFT(0, 0, 18, 12),
        RIGHT(19, 0, 37, 12);
        private Rectangle2D r;

        AnimationState(double x, double y, double w, double h) {
            r = new Rectangle2D(x, y, w, h);
        }

        public Rectangle2D getRect() {
            return r;
        }

    }
}
