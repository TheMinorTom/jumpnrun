/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.netcode;

import net.minortom.davidjumpnrun.netcode.screens.LoginScreen;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jumpnrun.JumpNRun;
import net.minortom.davidjumpnrun.netcode.screens.ChooseMapScreen;
import net.minortom.davidjumpnrun.netcode.screens.CreateGameScreen;
import net.minortom.davidjumpnrun.netcode.screens.JoinGameScreen;
import net.minortom.davidjumpnrun.netcode.screens.WaitScreen;

public class NetworkManager extends VBox {
    
    public static final String infoSeperator = "!";//Character.toString((char) 31);
    public static final String keyword = "JUMPNRUN";
    
    JumpNRun game;
    
    LoginScreen loginScreen;
    JoinGameScreen joinGameScreen;
    CreateGameScreen createGameScreen;
    WaitScreen waitScreen;
    ChooseMapScreen mapSelectionScreen;
    public ServerConnection serverConnection;
    
    Button backBt, loginBt, joinGameBt, createGameBt;
    Font defaultFont;
    
    public NetworkManager(JumpNRun gamearg){
        game = gamearg;
        
        loginScreen = new LoginScreen(game);
        joinGameScreen = new JoinGameScreen(game);
        createGameScreen = new CreateGameScreen(game);
        waitScreen = new WaitScreen(game);
        mapSelectionScreen = new ChooseMapScreen(game);
        
        backBt = new Button("ERR");
        backBt.setOnAction((ActionEvent e) -> {
            game.openMainMenu();
        });
        
        loginBt = new Button("ERR");
        loginBt.setOnAction((ActionEvent e) -> {
            this.openLoginScreen();
        });
        
        joinGameBt = new Button("ERR");
        joinGameBt.setOnAction((ActionEvent e) -> {
            this.openJoinGameScreen();
        });
        joinGameBt.setDisable(true);
        
        createGameBt = new Button("ERR");
        createGameBt.setOnAction((ActionEvent e) -> {
            this.openCreateGameScreen();
        });
        createGameBt.setDisable(true);
        
        updateStrings();
        
        setAlignment(Pos.CENTER);
        setSpacing(50);
        setPadding(new Insets(0, 0, 0, 0));
        getChildren().addAll(loginBt, createGameBt, joinGameBt, backBt);
    }
    
    public void updateStrings(){
        defaultFont = game.language.getFont();
        backBt.setText(game.language.backBt);
        backBt.setFont(defaultFont);
        loginBt.setText(game.language.NetworManagerLoginBt);
        loginBt.setFont(defaultFont);
        joinGameBt.setText(game.language.NetworManagerJoinGameBt);
        joinGameBt.setFont(defaultFont);
        createGameBt.setText(game.language.NetworManagerCreateGameBt);
        createGameBt.setFont(defaultFont);
    }
    
    public void openLoginScreen(){
        JumpNRun.scene.setRoot(loginScreen);
        loginScreen.updateStrings();
    }
    
    public void openWaitScreen(){
        JumpNRun.scene.setRoot(waitScreen);
        waitScreen.updateStrings();
    }
    
    public void openJoinGameScreen(){
        JumpNRun.scene.setRoot(joinGameScreen);
        joinGameScreen.updateStrings();
    }
    
    public void openCreateGameScreen(){
        JumpNRun.scene.setRoot(createGameScreen);
        createGameScreen.updateStrings();
    }
    
    public void openMapSelection(){
        openWaitScreen();
        setWaitScreenText(game.language.WaitServerAnswer);
    }
    
    public void mapSelectionDone(String[] maps){
        JumpNRun.scene.setRoot(mapSelectionScreen);
        mapSelectionScreen.updateStrings(maps);
    }
    
    public void setWaitScreenText(String text){
        waitScreen.setText(text);
    }
    
    public void updateBtnsLoggedIn(){
        joinGameBt.setDisable(false);
        createGameBt.setDisable(false);
    }
    
    public void shutdown() {
        try {
            serverConnection.end();
        } catch (Exception e) {}
    }

    public void creteGameScreenSetMap(String name) {
        createGameScreen.setMapName(name);
    }
}
