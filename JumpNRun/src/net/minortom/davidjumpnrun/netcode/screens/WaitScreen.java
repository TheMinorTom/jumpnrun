/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.netcode.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jumpnrun.JumpNRun;

public class WaitScreen extends VBox {
    
    JumpNRun game;
    
    Label textLabel;
    
    public WaitScreen (JumpNRun game) {
        this.game = game;
        
        textLabel = new Label("Please Wait");
        
        updateStrings();
        
        setAlignment(Pos.CENTER);
        setSpacing(50);
        setPadding(new Insets(0, 0, 0, 0));
        getChildren().addAll(textLabel);
    }
    
    public void updateStrings(){
        textLabel.setFont(game.language.getFont());
    }

    public void setText(String text) {
        textLabel.setText(text);
    }
}