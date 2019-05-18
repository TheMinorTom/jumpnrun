/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.netcode.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import jumpnrun.JumpNRun;
import net.minortom.davidjumpnrun.netcode.ServerCommand;

public class ChooseMapScreen extends VBox {
    
    JumpNRun game;
    Button backBt;
    Map<RadioButton, String> mapBtns;
    ToggleGroup mapBtnGroup;
    
    public ChooseMapScreen (JumpNRun game) {
        this.game = game;
        
        backBt = new Button("ERR");
        backBt.setOnAction((ActionEvent e) -> {
            callback(mapBtns.get(mapBtnGroup.getSelectedToggle()));
        });
        
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(50);
        setPadding(new Insets(0, 0, 0, 150));
        //getChildren().addAll(backBt);
    }
    
    public void updateStrings(String[] maps){
        backBt.setFont(game.language.getFont());
        backBt.setText(game.language.backBt);
        
        getChildren().clear();
        
        mapBtns = new HashMap<>();
        mapBtnGroup = new ToggleGroup();
        
        for(String iMap : maps){
            RadioButton btn = new RadioButton(iMap);
            btn.setToggleGroup(mapBtnGroup);
            mapBtns.put(btn, iMap);
            btn.setFont(game.language.getFont());
            getChildren().add(btn);
        }
        
        mapBtnGroup.selectedToggleProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                backBt.setDisable(false);
            }

        });
        
        backBt.setDisable(true);
        
        setSpacing(game.language.getFontSize());
        
        getChildren().addAll(backBt);
    }
    
    private void callback(String map){
        game.networkManager.createGameScreenSetMap(map);
        game.networkManager.openCreateGameScreen();
    }
    
    public void userRequest(){
        //game.networkManager.serverConnection.out.println(game.networkManager.keyword + game.networkManager.infoSeperator + "MAP-LISTREQ");
        game.networkManager.serverConnection.getCommandHandler().sendCommand(ServerCommand.MAP_LISTREQ, new String[]{});
        System.out.println("Map Request Sent"); ////////!!!!!!!!!!!!!!
    }
}
