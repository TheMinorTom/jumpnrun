/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.netcode.screens;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jumpnrun.JumpNRun;

public class JoinGameScreen extends VBox {
    
    JumpNRun game;
    Label nameLabel;
    Button okBt, backBt, skinBt;
    TextField nameField;
    HBox btBox;
    
    public JoinGameScreen (JumpNRun game) {
        this.game = game;
        
        nameLabel = new Label("ERR");
        
        okBt = new Button("ERR");
        backBt = new Button("ERR");
        skinBt = new Button("ERR");
        
        backBt.setOnAction((ActionEvent e) -> {
            game.openNetworkScreen();
        });
        
        okBt.setOnAction((ActionEvent e) -> {
            game.networkManager.openWaitScreen();
            game.networkManager.setWaitScreenText(game.language.WaitServerAnswer);
        });
        okBt.setDisable(true);
        
        skinBt.setOnAction((ActionEvent e) -> {
            game.openOnlineSkinChooseJoinGameMenu();
        });
        
        nameField = new TextField();
        // nameField.getStyleClass().add("-fx-alignment: center");
        
        btBox = new HBox(okBt, backBt);
        btBox.setAlignment(Pos.CENTER);
        btBox.setSpacing(100);
        btBox.setPadding(new Insets(0, 0, 0, 0));
        
        updateStrings();
        
        setAlignment(Pos.CENTER);
        setSpacing(50);
        setPadding(new Insets(0, 0, 0, 0));
        getChildren().addAll(nameLabel, nameField, skinBt, btBox);
    }
    
    public void updateStrings(){
        nameLabel.setText(game.language.JoinGNameLabel);
        nameLabel.setFont(game.language.getFont());
        
        okBt.setFont(game.language.getFont());
        backBt.setFont(game.language.getFont());
        skinBt.setFont(game.language.getFont());
        
        okBt.setText(game.language.JoinGOkBt);
        backBt.setText(game.language.backBt);
        skinBt.setText(game.language.GSkinBt);
        
        nameField.setFont(game.language.getFont());
    }
    
    public Button getOkBt() {
        return okBt;
    }
}