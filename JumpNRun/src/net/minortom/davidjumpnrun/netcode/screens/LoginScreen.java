/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.netcode.screens;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import jumpnrun.JumpNRun;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import net.minortom.davidjumpnrun.netcode.ServerConnection;

public class LoginScreen extends VBox {
    
    private static JumpNRun game;
    private Button backBt;
    private Label connectingLabel;
    private WebView loginView;
    
    public LoginScreen(JumpNRun game) {
        this.game = game;
        
        loginView = new WebView();
        loginView.getEngine().setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent<String> ev) {
                System.out.print(ev.getData());
                String[] params = ev.getData().split("&");
                String userIdTmp, userTokenTmp, serverTmp;
                userIdTmp = params[0];
                userTokenTmp = params[1];
                serverTmp = params[2];
                connectingLabel.setText(game.language.LoginScreenConnecting);
                game.networkManager.serverConnection = new ServerConnection(userIdTmp, userTokenTmp, serverTmp, game);
                game.networkManager.serverConnection.connect();
                if(null != game.networkManager.serverConnection.currentConnState) switch (game.networkManager.serverConnection.currentConnState) {
                    case CONNECTED:
                        connectingLabel.setText(game.language.NetworkManagerConnected);
                        game.config.networkUserId = userIdTmp;
                        game.config.networkUserToken = userTokenTmp;
                        game.config.networkHost = serverTmp;
                        game.config.networkLoggedIn = true;
                        ConfigManager.saveConfiguration(game.config);
                        game.openNetworkScreen();
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
            }
        });
        
        connectingLabel = new Label("");
        
        backBt = new Button("ERR");
        backBt.setOnAction((ActionEvent e) -> {
            game.openNetworkScreen();
        });
        
        updateStrings();
        
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(50);
        setPadding(new Insets(0, 50, 0, 50));
        setFillWidth(false);
                
        getChildren().addAll(loginView, backBt, connectingLabel);
    }
    
    public void updateStrings(){
        Font defaultFont = game.language.getFont();
        
        connectingLabel.setText("");
        connectingLabel.setFont(defaultFont);
        backBt.setText(game.language.backBt);
        backBt.setFont(defaultFont);
        
        // setSpacing(game.language.getFontSize());
        
        if(game.config.networkLoggedIn){
            loginView.getEngine().load("https://v1.api.minortom.net/sso/logout.php?noredir=1");
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {}
        }
        loginView.getEngine().load("https://v1.api.minortom.net/sso/start_flow.php?auth_client=1&auth_redir-id=1");
    }
}