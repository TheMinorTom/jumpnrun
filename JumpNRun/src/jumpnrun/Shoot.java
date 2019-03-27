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
import worldeditor.Block;
import worldeditor.IO;

/**
 *
 * @author Norbert
 */
public class Shoot extends ImageView implements Updatable{

    double xPos, yPos, xSpd, ySpd, breakAccX, gravAccY;

    private final static String imageSource = JumpNRun.sourcePath + "sprites/ShootGes.png";

    public Shoot(double x, double y, double xSpeed, double ySpeed, boolean facingRight) {
        Image image = new Image(IO.getFileStream(imageSource));
        setImage(image);
        setRotationAxis(new Point3D(xPos, yPos, 0));
        setRotate(calcRotation(xSpd, ySpd));
        if (facingRight) {
            setViewport(AnimationState.RIGHT.getRect());
            xSpd = xSpeed;
        } else {
            setViewport(AnimationState.LEFT.getRect());
            xSpd = -1*xSpeed;
        }
        ySpeed = ySpeed;
        
        xPos =x;
        yPos =y;
        breakAccX = 200;
        gravAccY = 200;
        
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
        setRotate(calcRotation(xSpd, ySpd)); 
        ySpd += ySpdAdd;
        xPos += xSpd * timeElapsedSeconds;
        yPos += ySpd * timeElapsedSeconds;
        setLayoutX(xPos);
        setLayoutY(yPos);
        
        if(collisionCheck(worldVec, protOne, protTwo)) {
            JumpNRun.removeNode(this);
            JumpNRun.removeUpdatable(this);
        }
    }
    
    private double calcRotation (double deltaX, double deltaY) {
        /*
        double tot1 = Math.sqrt(x1*x1+y1*y1);
        double tot2 = Math.sqrt(x2*x2+y2*y2);
        double cosAng = (x1*x2+y1*y2)/(tot1*tot2);
        double ang = Math.acos(cosAng);
        return ang;
*/
        double tot = Math.sqrt(deltaX*deltaX+deltaY*deltaY);
        double cosAng = deltaX/tot;
        double ang = Math.acos(cosAng);
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
