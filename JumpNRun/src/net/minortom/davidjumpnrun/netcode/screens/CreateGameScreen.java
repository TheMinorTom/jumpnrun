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
import jumpnrun.SkinChooseMenu;
import net.minortom.davidjumpnrun.configstore.ConfigManager;

public class CreateGameScreen extends VBox {
    
    JumpNRun game;
    
    private RadioButton endlessBt, timeBt, deathsBt;
    private Button backBt, okBt, chooseMapBt, skinBt;
    private TextField timeTF, deathsTF, playersTF, nameTF;
    private Label timeLbl, deathsLbl, playersLbl, nameLbl, mapNameLbl, skinLbl, players2Lbl;
    private HBox timeBox, deathsBox, btBox, playersBox, mapBox, skinBox;
    
    String skinUrl;
    
    private SkinChooseMenu skinChooseMenu;
    
    private double timeLimit; //In seconds
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
            game.openNetworkScreen();
        });

        okBt = new Button("Ok");
        okBt.setDisable(true);
        
        // The following sections are licensed under the MIT License. You should have already received a copy located at ../net/minortom/LICENSE.txt
        // Copyright 2019 MinorTom <mail in license file>
        
        chooseMapBt = new Button("ERR");
        chooseMapBt.setOnAction((ActionEvent e) -> {
            game.networkManager.openMapSelection();
            game.networkManager.mapSelectionScreen.userRequest();
            mapselected = true;
            unlockOk();
        });
        
        skinBt = new Button("ERR");
        skinBt.setOnAction((ActionEvent e) -> {
            game.openOnlineSkinChooseCreateGameMenu();
        });
        
        playersTF = new TextField();
        
        nameTF = new TextField();
        
        mapNameLbl = new Label();
        
        skinLbl = new Label();
        
        playersLbl = new Label("ERR");
        
        players2Lbl = new Label("ERR");
        
        nameLbl = new Label("ERR");
        
        playersBox = new HBox(playersLbl, playersTF, players2Lbl);
        playersBox.setAlignment(Pos.CENTER_LEFT);
        playersBox.setSpacing(20);
        playersBox.setPadding(new Insets(0, 0, 0, 0));
        
        mapBox = new HBox(chooseMapBt, mapNameLbl);
        mapBox.setAlignment(Pos.CENTER_LEFT);
        mapBox.setSpacing(20);
        mapBox.setPadding(new Insets(0, 0, 0, 0));
        
        skinBox = new HBox(skinBt, skinLbl);
        skinBox.setAlignment(Pos.CENTER_LEFT);
        skinBox.setSpacing(20);
        skinBox.setPadding(new Insets(0, 0, 0, 0));
        
        // End licensed sections
        
        btBox = new HBox(backBt, okBt);
        btBox.setAlignment(Pos.CENTER_LEFT);
        btBox.setSpacing(100);
        btBox.setPadding(new Insets(0, 0, 0, 100));
        
        updateStrings();
        
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(50);
        setPadding(new Insets(0, 0, 0, 150));
        getChildren().addAll(nameLbl, nameTF, playersBox, timeBox, deathsBox, endlessBt, mapBox, skinBox, btBox);
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
        
        backBt.setText(game.language.backBt);
        endlessBt.setText(game.language.ChoGmEndlessBt);
        timeBt.setText(game.language.ChoGmTimeBt);
        timeLbl.setText(game.language.ChoGmTimeLbl);
        deathsBt.setText(game.language.ChoGmDeathsBt);
        deathsLbl.setText(game.language.ChoGmDeathsLbl);
        okBt.setText(game.language.ChoGmOkBt);
        
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
            try {
                int z = Integer.parseInt(playersTF.getText());
            } catch (NumberFormatException n) {
                deathsTF.setText(game.language.ChoGmErrOnlyWholeNumbers);
            }
            game.networkManager.openWaitScreen();
            game.networkManager.setWaitScreenText(game.language.WaitServerAnswer);
            
        });
        
        // The following sections are licensed under the MIT License. You should have already received a copy located at ../net/minortom/LICENSE.txt
        // Copyright 2019 MinorTom <mail in license file>
        
        chooseMapBt.setFont(defaultFont);
        skinBt.setFont(defaultFont);
        playersTF.setFont(defaultFont);
        nameTF.setFont(defaultFont);
        mapNameLbl.setFont(defaultFont);
        skinLbl.setFont(defaultFont);
        playersLbl.setFont(defaultFont);
        players2Lbl.setFont(defaultFont);
        nameLbl.setFont(defaultFont);
        
        chooseMapBt.setText(game.language.CreateGChooseMapBt);
        skinBt.setText(game.language.GSkinBt);
        playersLbl.setText(game.language.CreateGPlayersLbl);
        players2Lbl.setText(game.language.CreateGPlayers2Lbl);
        nameLbl.setText(game.language.CreateGNameLbl);
        
        // End licensed sections
    }
    
    // The following sections are licensed under the MIT License. You should have already received a copy located at ../net/minortom/LICENSE.txt
    // Copyright 2019 MinorTom <mail in license file>
    
    public void unlockOk(){
        if (toggleschanged&&mapselected&&skinchosen){
            okBt.setDisable(false);
        }
    }

    public void setMapName(String name) {
        mapNameLbl.setText(name);
    }
  
    public void setSkinChosen(boolean b) {
        skinchosen = b;
    }
    
    // End licensed sections
}