/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import net.minortom.davidjumpnrun.configstore.ConfigManager;

/**
 *
 * @author DavidPrivat
 */
public class ChooseGamemodeMenu extends VBox {

    private RadioButton endlessBt, timeBt, deathsBt,
            customMapBt, defaultMapBt;
    private Button backBt, okBt;
    private TextField timeTF, deathsTF;
    private Label timeLbl, deathsLbl;
    private HBox timeBox, deathsBox, btBox,
            mapBox;
    private double timeLimit;   //In sekunden
    private double deathLimit;
    private static JumpNRun game;

    private String customPath = "";

    public ChooseGamemodeMenu(JumpNRun game) {
        this.game = game;

        timeLimit = 5;
        deathLimit = 10;

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                okBt.setDisable(false);
                if (toggleGroup.getSelectedToggle().equals(endlessBt)) {
                    timeTF.setDisable(true);
                    deathsTF.setDisable(true);
                } else if (toggleGroup.getSelectedToggle().equals(timeBt)) {
                    timeTF.setDisable(false);
                    deathsTF.setDisable(true);
                } else if (toggleGroup.getSelectedToggle().equals(deathsBt)) {
                    timeTF.setDisable(true);
                    deathsTF.setDisable(false);
                }
            }

        });

        endlessBt = new RadioButton("Unendlich");
        endlessBt.setToggleGroup(toggleGroup);

        timeBt = new RadioButton("Zeitbegrenzung: ");
        timeBt.setToggleGroup(toggleGroup);

        timeTF = new TextField("5.0");
        timeTF.setDisable(true);
        timeTF.setPrefWidth(120);

        timeLbl = new Label(" Minuten");

        timeBox = new HBox(timeBt, timeTF, timeLbl);

        deathsBt = new RadioButton("Respawnbegrenzung: ");
        deathsBt.setToggleGroup(toggleGroup);

        deathsTF = new TextField("10");
        deathsTF.setDisable(true);
        deathsTF.setPrefWidth(120);

        deathsLbl = new Label(" Respawns");

        deathsBox = new HBox(deathsBt, deathsTF, deathsLbl);

        customMapBt = new RadioButton("Map auswählen");
        customMapBt.setOnAction((ActionEvent e) -> {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(JumpNRun.sourcePath + "worlds/"));
            customPath = (fc.showOpenDialog(game.getPrimStage())).getPath();
            if (!customPath.endsWith(".david")) {
                customPath = "";
                ConfigManager.error("Game", "Selected file is not a valid world (.david)");
                defaultMapBt.setSelected(true);
            }
        });
        defaultMapBt = new RadioButton("Standard map");
        defaultMapBt.setSelected(true);
        defaultMapBt.setOnAction((ActionEvent e) -> {
            customPath = "";
        });
        ToggleGroup mapToggle = new ToggleGroup();
        mapToggle.getToggles().addAll(defaultMapBt, customMapBt);
        mapBox = new HBox(customMapBt, defaultMapBt);
        mapBox.setSpacing(50);

        backBt = new Button("Zurück");
        backBt.setOnAction((ActionEvent e) -> {
            game.openMainMenu();
        });

        okBt = new Button("Ok");
        okBt.setDisable(true);

        btBox = new HBox(backBt, okBt);
        btBox.setAlignment(Pos.CENTER_LEFT);
        btBox.setSpacing(100);
        btBox.setPadding(new Insets(0, 0, 0, 100));

        updateStrings();

        setAlignment(Pos.CENTER_LEFT);
        setSpacing(50);
        setPadding(new Insets(0, 0, 0, 150));
        getChildren().addAll(timeBox, deathsBox, endlessBt, new Separator(), mapBox, btBox);
    }

    public void updateStrings() {
        Font defaultFont = game.language.getFont();
        endlessBt.setFont(defaultFont);
        timeBt.setFont(defaultFont);
        timeTF.setFont(defaultFont);
        timeLbl.setFont(defaultFont);
        deathsBt.setFont(defaultFont);
        deathsTF.setFont(defaultFont);
        deathsLbl.setFont(defaultFont);
        backBt.setFont(defaultFont);
        okBt.setFont(defaultFont);
        customMapBt.setFont(defaultFont);
        defaultMapBt.setFont(defaultFont);

        backBt.setText(game.language.backBt);
        endlessBt.setText(game.language.ChoGmEndlessBt);
        timeBt.setText(game.language.ChoGmTimeBt);
        timeLbl.setText(game.language.ChoGmTimeLbl);
        deathsBt.setText(game.language.ChoGmDeathsBt);
        deathsLbl.setText(game.language.ChoGmDeathsLbl);
        okBt.setText(game.language.ChoGmOkBt);
        customMapBt.setText(game.language.ChoGmCMBt);
        defaultMapBt.setText(game.language.ChoGmDMBt);

        okBt.setOnAction((ActionEvent e) -> {
            game.setWorldPath(customPath);
            if (timeBt.isSelected()) {
                try {
                    game.setTimeLimit((Double.parseDouble(timeTF.getText())) * 60);
                    game.setCurrGamemode(JumpNRun.Gamemode.TIME);
                    game.openOfflineSkinChooseMenu1();
                } catch (NumberFormatException n) {
                    timeTF.setText(game.language.ChoGmErrOnlyNumbers);
                    game.setTimeLimit(timeLimit * 60);
                }

            } else if (deathsBt.isSelected()) {
                try {
                    game.setDeathLimit(Integer.parseInt(deathsTF.getText()));
                    game.setCurrGamemode(JumpNRun.Gamemode.DEATHS);
                    game.openOfflineSkinChooseMenu1();
                } catch (NumberFormatException n) {
                    deathsTF.setText(game.language.ChoGmErrOnlyWholeNumbers);
                }
            } else if (endlessBt.isSelected()) {
                game.setCurrGamemode(JumpNRun.Gamemode.ENDLESS);
                game.openOfflineSkinChooseMenu1();
            }

        });
        
        setSpacing(game.language.getFontSize());
    }

}
