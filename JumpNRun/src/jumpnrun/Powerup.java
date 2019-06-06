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
public class Powerup extends ImageView implements OnlineUpdatableObject {

    private final static String imageSource = "sprites/icons.png";
    private IconViewport iconViewport;

    public Powerup(IconViewport viewPort) {
        setImage(new Image(ConfigManager.getFileStream(imageSource)));
        setViewport(viewPort.getRect());
        iconViewport = viewPort;
    }

    public Powerup(int viewPortAsInt) {
        setImage(new Image(ConfigManager.getFileStream(imageSource)));
        setAnimationState(viewPortAsInt);
        setLayoutY(JumpNRun.getHeight() - (JumpNRun.game.language.getFontSize() * 10));
        setLayoutX(50);
        setFitHeight(JumpNRun.game.language.getFontSize() * 10);
        setFitWidth(JumpNRun.game.language.getFontSize() * 10);
    }

    private void setAnimationState(int a) {
        if (a >= 0) {
            iconViewport = IconViewport.values()[a];
            setVisible(true);

        } else {
            iconViewport = IconViewport.DOUBLE_SPEED;
            setVisible(false);
        }
    }

    public IconViewport getIcon() {
        return iconViewport;
    }

    @Override
    public void updatePos(double x, double y, int animationState) {
        setAnimationState(animationState);
    }

    @Override
    public void updateGraphic(double xScroll, double yScroll) {
        setViewport(iconViewport.getRect());
        setLayoutY(JumpNRun.getHeight() - (JumpNRun.game.language.getFontSize() * 12));
        setLayoutX(50);
        setFitHeight(JumpNRun.game.language.getFontSize() * 4);
        setFitWidth(JumpNRun.game.language.getFontSize() * 4);
    }

    public enum IconViewport {

        DOUBLE_SPEED(0, 0, 40, 40),
        MACHINE_PISTOL(41, 0, 40, 40),
        TRUCK(82, 0, 40, 40);
        private Rectangle2D r;

        IconViewport(double x, double y, double w, double h) {
            r = new Rectangle2D(x, y, w, h);
        }

        public Rectangle2D getRect() {
            return r;
        }
    }

}
