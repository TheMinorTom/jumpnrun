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
import javafx.scene.text.Font;
import jumpnrun.JumpNRun;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import net.minortom.davidjumpnrun.netcode.ServerConnection;

public class LoginScreen extends VBox {
    
    private static JumpNRun game;
    private TextField login, pass, server;
    private Button doLoginBt, backBt;
    private Label loginLabel, passLabel, serverLabel, connectingLabel;
    private HBox btBox;
    
    public LoginScreen(JumpNRun game) {
        this.game = game;
        
        loginLabel = new Label("ERR");
        
        passLabel = new Label("ERR");
        
        serverLabel = new Label("ERR");
        
        connectingLabel = new Label("");
        
        doLoginBt = new Button("ERR");
        doLoginBt.setOnAction((ActionEvent e) -> {
            connectingLabel.setText(game.language.LoginScreenConnecting);
            if(login.getText().contains("!")||pass.getText().contains("!")){
                ConfigManager.error(game.language.LoginScreenErrorInvalidCharTitle, game.language.LoginScreenErrorInvalidCharText);
                connectingLabel.setText(game.language.NetworkManagerInvalidAuth);
                return;
            }
            game.networkManager.serverConnection = new ServerConnection(login.getText(), pass.getText(), server.getText(), game);
            game.networkManager.serverConnection.connect();
            if(null != game.networkManager.serverConnection.currentConnState) switch (game.networkManager.serverConnection.currentConnState) {
                case CONNECTED:
                    connectingLabel.setText(game.language.NetworkManagerConnected);
                    doLoginBt.setDisable(true);
                    game.config.networkUser = login.getText();
                    game.config.networkPass = pass.getText();
                    game.config.networkHost = server.getText();
                    ConfigManager.saveConfiguration(game.config);
                    game.networkManager.updateBtnsLoggedIn();
                    break;
                case ERROR_INVALID_HOST:
                    connectingLabel.setText(game.language.NetworkManagerInvalidHost);
                    break;
                case ERROR_INVALID_AUTH:
                    connectingLabel.setText(game.language.NetworkManagerInvalidAuth);
                    break;
                default:
                    connectingLabel.setText(game.language.NetworkManagerInternalError);
                    break;
            }
            System.out.println(game.networkManager.serverConnection);
        });
        
        backBt = new Button("ERR");
        backBt.setOnAction((ActionEvent e) -> {
            game.openNetworkScreen();
        });
        
        login = new TextField();
        pass = new TextField();
        server = new TextField();
        
        login.setText(game.config.networkUser);
        pass.setText(game.config.networkPass);
        server.setText(game.config.networkHost);
        
        btBox = new HBox(doLoginBt, backBt);
        btBox.setAlignment(Pos.CENTER_LEFT);
        btBox.setSpacing(100);
        btBox.setPadding(new Insets(0, 0, 0, 100));
        
        updateStrings();
        
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(50);
        setPadding(new Insets(0, 0, 0, 150));
        getChildren().addAll(loginLabel, login, passLabel, pass, serverLabel, server, btBox, connectingLabel);
    }
    
    public void updateStrings(){
        Font defaultFont = game.language.getFont();
        
        loginLabel.setText(game.language.LoginScreenLoginLabel);
        loginLabel.setFont(defaultFont);
        passLabel.setText(game.language.LoginScreenPassLabel);
        passLabel.setFont(defaultFont);
        serverLabel.setText(game.language.LoginScreenServerLabel);
        serverLabel.setFont(defaultFont);
        connectingLabel.setText("");
        connectingLabel.setFont(defaultFont);
        doLoginBt.setText(game.language.LoginScreenDoLoginBt);
        doLoginBt.setFont(defaultFont);
        backBt.setText(game.language.backBt);
        backBt.setFont(defaultFont);
        login.setFont(defaultFont);
        pass.setFont(defaultFont);
        server.setFont(defaultFont);
    }
}
