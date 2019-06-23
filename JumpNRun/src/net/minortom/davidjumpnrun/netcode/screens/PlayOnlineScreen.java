/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minortom.davidjumpnrun.netcode.screens;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jumpnrun.JumpNRun;
import net.minortom.davidjumpnrun.netcode.NetworkManager;
import net.minortom.davidjumpnrun.netcode.ServerConnection;

/**
 *
 * @author DavidPrivat
 */
public class PlayOnlineScreen extends VBox {

    private static JumpNRun game;
    private Button loginBt;
    private Button playWithoutLoginBt;
    private Button backBt;
    private Button playAs;
    private Separator sep;

    public PlayOnlineScreen(JumpNRun game) {
        this.game = game;

        loginBt = new Button("ERR");
        loginBt.setOnAction((ActionEvent e) -> {
            game.networkManager.openLoginScreen();
        });

        playWithoutLoginBt = new Button();
        playWithoutLoginBt.setOnAction((ActionEvent e) -> {
            game.networkManager.openJoinGameNotLoggedInScreen();
        });

        playAs = new Button();
        playAs.setOnAction((ActionEvent e) -> {
            game.openNetworkScreen();
        });

        backBt = new Button("ERR");
        backBt.setOnAction((ActionEvent e) -> {
            game.openMainMenu();
        });

        updateStrings();

        setAlignment(Pos.CENTER_LEFT);
        setSpacing(50);
        setPadding(new Insets(0, 50, 0, 50));

    }

    public void updateStrings() {
        Font defaultFont = game.language.getFont();
        playWithoutLoginBt.setText(game.language.plOnlMnPlayWithoutLogin);
        playWithoutLoginBt.setFont(defaultFont);

        loginBt.setText(game.language.NetworManagerLoginBt);
        loginBt.setFont(defaultFont);

        backBt.setText(game.language.backBt);
        backBt.setFont(defaultFont);
        
        playAs.setFont(defaultFont);

        getChildren().clear();
        if (game.config.networkLoggedIn) {
            updateButtonsLoggedIn();
        } else {
            updateButtonsLoggedOut();
        }

    }

    public void updateButtonsLoggedIn() {

        loginBt.setText(game.language.NetworManagerLoginBtLoggedIn);
        getChildren().clear();
        getChildren().addAll(playWithoutLoginBt, playAs, loginBt, new Separator(), backBt);
        if (game.networkManager.serverConnection == null) {
            game.networkManager.serverConnection = new ServerConnection(game.config.networkUserId, game.config.networkUserToken, game.config.networkHost, game);
            game.networkManager.serverConnection.connect();
            if (null != game.networkManager.serverConnection.currentConnState) {
                switch (game.networkManager.serverConnection.currentConnState) {
                    case CONNECTED:
                        playAs.setText(game.language.plOnlMnPlayAs + game.networkManager.serverConnection.userName);
                        game.networkManager.loadAvatar();
                        break;
                    default:
                        updateButtonsLoggedOut();
                        break;
                }

            }
        }

    }

    public void updateButtonsLoggedOut() {
        getChildren().clear();
        getChildren().addAll(playWithoutLoginBt, loginBt, new Separator(), backBt);
    }
}
