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

/**
 *
 * @author DavidPrivat
 */
public class Pitchfork extends ImageView{
    
    private final static String imageSource = "/sprites/pitchforkGes.png";
    private boolean facingLeft;
    Pitchfork()
    {
        setImage(new Image(imageSource));
        setVisible(false);
        facingLeft = true;
        setViewport(AnimationState.LEFT.getRect());
        
       
        
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
    
    private enum AnimationState{
        LEFT(0,0,60,12),
        RIGHT(61,0,60,12);
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
