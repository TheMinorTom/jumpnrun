/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.netcode.screens;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import jumpnrun.JumpNRun;

public class ChooseMapScreen extends VBox {
    
    JumpNRun game;
    Button backBt;
    
    public ChooseMapScreen (JumpNRun game) {
        this.game = game;
        
        backBt = new Button("ERR");
        backBt.setOnAction((ActionEvent e) -> {
            game.networkManager.openCreateGameScreen();
            game.networkManager.creteGameScreenSetMap("Test");
        });
        
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(50);
        setPadding(new Insets(0, 0, 0, 150));
        getChildren().addAll(backBt);
    }
    
    public void updateStrings(String[] maps){
        backBt.setFont(game.language.getFont());
        backBt.setText(game.language.backBt);
    }
}
