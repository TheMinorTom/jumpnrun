/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import javafx.beans.property.BooleanProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import worldeditor.IO;

/**
 *
 * @author DavidPrivat
 */
public class Pitchfork extends ImageView implements OnlineUpdatableObject{
    
    private final static String imageSource = "sprites/pitchforkGes.png";
    private boolean facingLeft;
    private AnimationState currAnimationState;
    private double xPos, yPos;
    Pitchfork()
    {
        setImage(new Image(ConfigManager.getFileStream(imageSource)));
        setVisible(false);
        facingLeft = true;
        currAnimationState = AnimationState.LEFT;
        setViewport(currAnimationState.getRect());
        
       
        
    }
    
    public void updateHit (double hitTimer, Protagonist otherProt)
    {
        if(this.getBoundsInParent().intersects(otherProt.getBoundsInParent()))
        {
            otherProt.hitten();
            
        }
    }
    
    public void setFacingLeft(boolean b)
    {
        facingLeft = b;
        if(facingLeft)
        {
            setViewport(AnimationState.LEFT.getRect());
        }
        else
        {
            setViewport(AnimationState.RIGHT.getRect());
        }
    }
    
    public boolean getFacingLeft()
    {
        return facingLeft;
    }
    
    @Override
    public void updatePos(double x, double y, int animationState) {
        xPos = x;
        yPos = y;
        currAnimationState = AnimationState.values()[animationState];
    }

    @Override
    public void updateGraphic(double scrollX, double scrollY) {
        setX(xPos + scrollX);
        setY(yPos + scrollY);
        setViewport(currAnimationState.getRect());
    }
    
    public enum AnimationState{
        LEFT(0,0,60,14),
        RIGHT(61,0,60,14);
        private Rectangle2D r;
        
        AnimationState(double x, double y, double w, double h)
        {
            r = new Rectangle2D(x, y, w, h);
        }
        
        public Rectangle2D getRect()
        {
            return r;
        }
    }
    
}
