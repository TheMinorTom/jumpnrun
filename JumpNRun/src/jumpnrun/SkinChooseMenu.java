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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import net.minortom.davidjumpnrun.i18n.Language;
import worldeditor.IO;

/**
 *
 * @author DavidPrivat
 */
public class SkinChooseMenu extends VBox {

    private final String[] skinPaths = new String[]{"sprites/protagonist/SpielfigurBlau.png",
        "sprites/protagonist/SpielfigurGruen.png",
        "sprites/protagonist/SpielfigurOrange.png",
        "sprites/protagonist/SpielfigurGelb.png",
        "sprites/protagonist/SpielfigurRot.png"};
    private final Rectangle2D viewPort = Protagonist.getMidViewport();
    private Button backBt, okBt;
    private Label headingLbl;
    private ImageView blueIV, greenIV, orangeIV, yellowIV, redIV;
    private RadioButton blueRB, greenRB, orangeRB, yellowRB, redRB;
    private ToggleGroup toggle;
    private GridPane choicePane;
    private final JumpNRun game;
    private final SkinChooseMode mode;

    private Skin skin;
    public SkinChooseMenu(JumpNRun game, SkinChooseMode mode) {
        this.game = game;
        this.mode = mode;
        blueIV = new ImageView(new Image(ConfigManager.getFileStream(skinPaths[0])));
        blueIV.setViewport(viewPort);
        greenIV = new ImageView(new Image(ConfigManager.getFileStream(skinPaths[1])));
        greenIV.setViewport(viewPort);
        orangeIV = new ImageView(new Image(ConfigManager.getFileStream(skinPaths[2])));
        orangeIV.setViewport(viewPort);
        yellowIV = new ImageView(new Image(ConfigManager.getFileStream(skinPaths[3])));
        yellowIV.setViewport(viewPort);
        redIV = new ImageView(new Image(ConfigManager.getFileStream(skinPaths[4])));
        redIV.setViewport(viewPort);

        toggle = new ToggleGroup();

        blueRB = new RadioButton();
        greenRB = new RadioButton();
        greenRB.setSelected(true);
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
        backBt.setOnAction((ActionEvent e) -> {
            switch (mode) {
                case OFFLINE_PLAYER_1:
                    game.openChooseGamemodeMenu();
                    break;
                case OFFLINE_PLAYER_2:
                    game.openOfflineSkinChooseMenu1();
                    break;
                case ONLINE_CREATE_GAME:
                    game.networkManager.openCreateGameScreen();
                    break;
                case ONLINE_JOIN_GAME:
                    game.networkManager.openJoinGameScreen();
                    break;
            }
        });
        okBt = new Button(game.language.okBt);
        okBt.setOnAction((ActionEvent e) -> {
            switch (mode) {
                case OFFLINE_PLAYER_1:
                    game.setSkinProt1(getSkin());
                    game.openOfflineSkinChooseMenu2();
                    break;
                case OFFLINE_PLAYER_2:
                    game.setSkinProt2(getSkin());
                    game.initGame();
                    break;
                case ONLINE_CREATE_GAME:
                    game.networkManager.createGameScreen.setSkinChosen(true, getSkin(), ((RadioButton) toggle.getSelectedToggle()).getText());
                    game.networkManager.openCreateGameScreen();
                    break;
                case ONLINE_JOIN_GAME:
                    game.networkManager.joinGameScreen.setSkinChosen(true, getSkin(), ((RadioButton) toggle.getSelectedToggle()).getText());
                    game.networkManager.openJoinGameScreen();
                    break;
            }
        });

        HBox buttonBox = new HBox(backBt, okBt);
        buttonBox.setSpacing(50);
        buttonBox.setPadding(new Insets(0, 0, 0, 70));

        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(0, 0, 0, 150));
        setSpacing(50);
        getChildren().addAll(headingLbl, choicePane, buttonBox);

    }

    private Skin getSkin() {
        if (blueRB.isSelected()) {
            return Skin.BLUE;
        } else if (greenRB.isSelected()) {
            return Skin.GREEN;
        } else if (orangeRB.isSelected()) {
            return Skin.ORANGE;
        } else if (yellowRB.isSelected()) {
            return Skin.YELLOW;
        } else if (redRB.isSelected()) {
            return Skin.RED;
        } else {
            return null;
        }
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

        headingLbl.setFont(game.language.getHeadingFont());
        switch (mode) {
            case OFFLINE_PLAYER_1:
                headingLbl.setText(game.language.SkinChooseOfflinePlayer1Heading);
                break;
            case OFFLINE_PLAYER_2:
                headingLbl.setText(game.language.SkinChooseOfflinePlayer2Heading);
                break;
            case ONLINE_CREATE_GAME:

                break;
            case ONLINE_JOIN_GAME:

                break;
            default:
                headingLbl.setText(game.language.SkinChooseDefaultHeading);
        }
    }

    public void setHeading(String h) {
        headingLbl.setText(h);
    }

    public enum SkinChooseMode {

        OFFLINE_PLAYER_1,
        OFFLINE_PLAYER_2,
        ONLINE_CREATE_GAME,
        ONLINE_JOIN_GAME;
    }

    public enum Skin {

        RED("sprites/protagonist/SpielfigurRot.png", Color.RED, "red"),
        BLUE("sprites/protagonist/SpielfigurBlau.png", Color.BLUE, "blue"),
        GREEN("sprites/protagonist/SpielfigurGruen.png", Color.GREEN, "green"),
        ORANGE("sprites/protagonist/SpielfigurOrange.png", Color.ORANGE, "orange"),
        YELLOW("sprites/protagonist/SpielfigurGelb.png", Color.YELLOW, "yellow");
        public final String path;
        public final Color color;
        public final String skinName;
        Skin(String path, Color c, String name) {
            this.path = path;
            color = c;
            skinName = name;
        }
    }
}
