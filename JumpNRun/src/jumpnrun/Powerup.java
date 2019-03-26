/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Norbert
 */
public class Powerup extends ImageView{
        private final static String imageSource = "/sprites/icons.png";
        private IconViewport iconViewport;
    public Powerup (IconViewport viewPort) {
        setImage(new Image(imageSource));
        setViewport(viewPort.getRect());
        iconViewport = viewPort;
        
        
    }
    
    public IconViewport getIcon (){
    return iconViewport;
}
    public enum IconViewport{
        DOUBLE_SPEED(0,0,40,40),
        MACHINE_PISTOL(41,0,40,40),
        TRUCK(82,0,40,40);
        private Rectangle2D r;
        
        IconViewport(double x, double y, double w, double h)
        {
            r = new Rectangle2D(x, y, w, h);
        }
        
        public Rectangle2D getRect()
        {
            return r;
        }
    }
    
}
