/*
 * This file is not licensed.
 */
package net.minortom.davidjumpnrun.netcode.screens;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jumpnrun.JumpNRun;

public class CreateGameScreen extends VBox {
    
    JumpNRun game;
    
    private RadioButton endlessBt, timeBt, deathsBt;
    private Button backBt, okBt, chooseMapBt, skinBt;
    private TextField timeTF, deathsTF;
    private Label timeLbl, deathsLbl;
    private HBox timeBox, deathsBox, btBox;
    private double timeLimit;   //In seconds
    private double deathLimit;
    
    
    private boolean toggleschanged = false;
    private boolean mapselected = false;
    private boolean skinchosen = false;
    
    public CreateGameScreen (JumpNRun game) {
        this.game = game;
        
        timeLimit = 5;
        deathLimit = 10;

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                toggleschanged = true;
                unlockOk();
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

        backBt = new Button("Zurück");
        backBt.setOnAction((ActionEvent e) -> {
            game.openMainMenu();
        });

        okBt = new Button("Ok");
        okBt.setDisable(true);
        
        chooseMapBt = new Button("ERR");
        chooseMapBt.setOnAction((ActionEvent e) -> {
            game.networkManager.openMapSelection();
            mapselected = true;
            unlockOk();
        });
        
        skinBt = new Button("ERR");
        skinBt.setOnAction((ActionEvent e) -> {
            skinchosen = true;
            unlockOk();
        });
        
        btBox = new HBox(backBt, okBt);
        btBox.setAlignment(Pos.CENTER_LEFT);
        btBox.setSpacing(100);
        btBox.setPadding(new Insets(0, 0, 0, 100));
        
        updateStrings();
        
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(50);
        setPadding(new Insets(0, 0, 0, 150));
        getChildren().addAll(timeBox, deathsBox, endlessBt, chooseMapBt, skinBt, btBox);
    }
    
    public void updateStrings(){
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
        chooseMapBt.setFont(defaultFont);
        skinBt.setFont(defaultFont);
        
        backBt.setText(game.language.backBt);
        endlessBt.setText(game.language.ChoGmEndlessBt);
        timeBt.setText(game.language.ChoGmTimeBt);
        timeLbl.setText(game.language.ChoGmTimeLbl);
        deathsBt.setText(game.language.ChoGmDeathsBt);
        deathsLbl.setText(game.language.ChoGmDeathsLbl);
        okBt.setText(game.language.ChoGmOkBt);
        chooseMapBt.setText(game.language.CreateGChooseMapBt);
        skinBt.setText(game.language.GSkinBt);
        
        okBt.setOnAction((ActionEvent e) -> {
            if (timeBt.isSelected()) {
                try {
                    double x = Double.parseDouble(timeTF.getText());
                } catch (NumberFormatException n) {
                    timeTF.setText(game.language.ChoGmErrOnlyNumbers);
                }
            } else if (deathsBt.isSelected()) {
                try {
                    int x = Integer.parseInt(deathsTF.getText());
                } catch (NumberFormatException n) {
                    deathsTF.setText(game.language.ChoGmErrOnlyWholeNumbers);
                }
            } else if (endlessBt.isSelected()) {
                
            }
            
            game.networkManager.openWaitScreen();
            game.networkManager.setWaitScreenText(game.language.WaitServerAnswer);
            
        });
    }
    
    private void unlockOk(){
        if (toggleschanged&&mapselected&&skinchosen){
            okBt.setDisable(false);
        }
    }
    
}