/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import jumpnrun.SkinChooseMenu.Skin;

/**
 *
 * @author DavidPrivat
 */
public class ProtagonistOnlineClient extends Protagonist{
    private Label nameLbl;
    public ProtagonistOnlineClient(int id, KeyCode left, KeyCode right, KeyCode jump, KeyCode hit, KeyCode shoot, KeyCode use, double x, double y, Skin skin, String name) {
        super(id, left, right, jump, hit, shoot, use, x , y, skin);
        nameLbl = new Label(name);
        nameLbl.setTextFill(skin.color);
        nameLbl.setFont(new Font(JumpNRun.game.language.getFontName(), 20));
    
    }
    
    public void update (double x, double y, CostumeViewport viewPort) {
        xPos = x;
        yPos = y;
        setX(xPos);
        setY(yPos);
        setViewport(viewPort.getRect());
        nameLbl.setLayoutX((getLayoutX()+getWidth()/2)-(nameLbl.getWidth()/2));
        nameLbl.setLayoutY(getLayoutY()-20);
    }
}
