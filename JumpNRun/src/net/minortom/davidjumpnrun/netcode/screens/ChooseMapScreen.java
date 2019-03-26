/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minortom.davidjumpnrun.netcode.screens;

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
        
        updateStrings();
        
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(50);
        setPadding(new Insets(0, 0, 0, 150));
        getChildren().addAll(backBt);
    }
    
    public void updateStrings(){
        backBt.setFont(game.language.getFont());
        backBt.setText(game.language.backBt);
    }
}
