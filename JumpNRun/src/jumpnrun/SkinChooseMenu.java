/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import net.minortom.davidjumpnrun.i18n.Language;

/**
 *
 * @author DavidPrivat
 */
public class SkinChooseMenu extends VBox {
    private final String[] skinPaths = new String[] {"/sprites/protagonist/SpielfigurBlau.png", 
        "/sprites/protagonist/SpielfigurGruen.png",
    "/sprites/protagonist/SpielfigurOrange.png",
    "/sprites/protagonist/SpielfigurGelb.png",
    "/sprites/protagonist/SpielfigurRot.png"};
    private final Rectangle2D viewPort = Protagonist.getMidViewport();
    private Button backBt, okBt;
    private Label headingLbl;
    private ImageView blueIV, greenIV, orangeIV, yellowIV, redIV;
    private RadioButton blueRB, greenRB, orangeRB, yellowRB, redRB;
    private ToggleGroup toggle;
    private GridPane choicePane;
    private final JumpNRun game;

    public SkinChooseMenu(JumpNRun game) {
        this.game = game;
        blueIV = new ImageView(new Image(skinPaths[0]));
        blueIV.setViewport(viewPort);
        greenIV = new ImageView(new Image(skinPaths[1]));
        greenIV.setViewport(viewPort);
        orangeIV = new ImageView(new Image(skinPaths[2]));
        orangeIV.setViewport(viewPort);
        yellowIV = new ImageView(new Image(skinPaths[3]));
        yellowIV.setViewport(viewPort);
        redIV = new ImageView(new Image(skinPaths[4]));
        redIV.setViewport(viewPort);
        
        toggle = new ToggleGroup();
        
        blueRB = new RadioButton();
        greenRB = new RadioButton();
        orangeRB = new RadioButton();
        yellowRB = new RadioButton();
        redRB = new RadioButton();
        toggle.getToggles().addAll(blueRB, greenRB, orangeRB, yellowRB, redRB);
        
        choicePane = new GridPane();
        
        choicePane.add(blueRB, 1, 0);
        choicePane.add(blueIV, 0, 0);
        choicePane.add(greenRB, 1, 1);
        choicePane.add(greenIV, 0, 1);
        choicePane.add(orangeRB, 1, 2);
        choicePane.add(orangeIV, 0, 2);
        choicePane.add(yellowRB, 1, 3);
        choicePane.add(yellowIV, 0, 3);
        choicePane.add(redRB, 1, 4);
        choicePane.add(redIV, 0, 4);
        choicePane.setAlignment(Pos.CENTER_LEFT);
        choicePane.setHgap(20);
        
        headingLbl = new Label(game.language.SkinChooseDefaultHeading);
        
        backBt = new Button(game.language.backBt);
        backBt.setOnAction((ActionEvent e)->{
            
        });
        okBt = new Button(game.language.okBt);
        HBox buttonBox = new HBox(backBt, okBt);
        buttonBox.setSpacing(50);
        buttonBox.setPadding(new Insets(0,0,0,70));
        
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(0, 0, 0, 150));
        setSpacing(50);
        getChildren().addAll(headingLbl, choicePane, buttonBox);

    }
    
    public void updateStrings() {
        greenRB.setText(game.language.SkinColorGreen);
        blueRB.setText(game.language.SkinColorBlue);
        orangeRB.setText(game.language.SkinColorOrange);
        yellowRB.setText(game.language.SkinColorYellow);
        redRB.setText(game.language.SkinColorRed);
        
        greenRB.setFont(game.language.getFont());
        blueRB.setFont(game.language.getFont());
        orangeRB.setFont(game.language.getFont());
        yellowRB.setFont(game.language.getFont());
        redRB.setFont(game.language.getFont());
        okBt.setFont(game.language.getFont());
        backBt.setFont(game.language.getFont());
        
        headingLbl.setFont(new Font(game.language.getFontName(), game.language.getFontSize()*2));
    }
    
    public void setHeading(String h) {
        headingLbl.setText(h);
    }
}
