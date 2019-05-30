/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import javafx.scene.control.Label;

/**
 *
 * @author DavidPrivat
 */
public class OnlineUpdatableCounterLabel extends Label implements OnlineUpdatableObject {

    private double xPos, yPos;
    private String preText;
    private String text;
    private String number;
    private boolean needsScrolling;

    public OnlineUpdatableCounterLabel(String text, double x, double y, boolean scrolling) {
        preText = text;
        xPos = x;
        yPos = y;
        this.text = "";
        number = "";
        needsScrolling = scrolling;
    }

    @Override
    public void updatePos(double x, double y, int animationState) {
        xPos = x;
        yPos = y;
        number = String.valueOf(animationState);
    }

    public void updateText(String text) {
        this.text = text;
    }

    @Override
    public void updateGraphic(double xScroll, double yScroll) {
        if (needsScrolling) {
            setLayoutX(xPos + JumpNRun.game.getXScroll());
            setLayoutY(yPos + JumpNRun.game.getYScroll());
        } else {
            setLayoutX(xPos);
            setLayoutY(yPos);
        }
        if (text.length() > 0) {
            setText(text);
        } else {
            setText(preText + number);
        }
    }

}
