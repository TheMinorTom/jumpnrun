/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.netcode;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import net.minortom.davidjumpnrun.netcode.screens.LoginScreen;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jumpnrun.JumpNRun;
import net.minortom.davidjumpnrun.netcode.screens.ChooseMapScreen;
import net.minortom.davidjumpnrun.netcode.screens.CreateGameScreen;
import net.minortom.davidjumpnrun.netcode.screens.JoinGameScreen;
import net.minortom.davidjumpnrun.netcode.screens.WaitScreen;
import net.minortom.davidjumpnrun.netcode.screens.WaitScreen.WaitAnimation;

public class NetworkManager extends VBox {

    public static final String infoSeperator = "!";//Character.toString((char) 31);
    public static final String differentObjectsSeperator = "|";
    public static final String subArgsSeperator = "/";
    public static final String keyword = "JUMPNRUN";

    public java.util.Map<String, String> onlineWaitScreenPlayers; // PubID, name
    public int onlineWaitScreenPlayersNeeded;

    JumpNRun game;

    LoginScreen loginScreen;
    public JoinGameScreen joinGameScreenLoggedIn, joinGameScreenNotLoggedIn;
    public CreateGameScreen createGameScreen, createGameScreenLocal;
    WaitScreen waitScreen;
    public ChooseMapScreen mapSelectionScreen;
    public ServerConnection serverConnection;

    HBox userBox;
    Button backBt, loginBt, joinGameBt, createGameBt, leaderboardBt;
    Image avatarImg;
    ImageView avatar;
    Label userName;
    Font defaultFont;

    public NetworkManager(JumpNRun gamearg) {
        game = gamearg;

        loginScreen = new LoginScreen(game);
        joinGameScreenLoggedIn = new JoinGameScreen(game, true);
        joinGameScreenNotLoggedIn = new JoinGameScreen(game, false);
        createGameScreen = new CreateGameScreen(game, false);
        createGameScreenLocal = new CreateGameScreen(game, true);
        waitScreen = new WaitScreen(game);
        mapSelectionScreen = new ChooseMapScreen(game);

        backBt = new Button("ERR");
        backBt.setOnAction((ActionEvent e) -> {
            game.openPlayOnlineScreen();
        });

        loginBt = new Button("ERR");
        loginBt.setOnAction((ActionEvent e) -> {
            this.openLoginScreen();
        });

        joinGameBt = new Button("ERR");
        joinGameBt.setOnAction((ActionEvent e) -> {
            this.openJoinGameLoggedInScreen();
        });
        joinGameBt.setDisable(true);

        createGameBt = new Button("ERR");
        createGameBt.setOnAction((ActionEvent e) -> {
            this.openCreateGameScreen();
        });
        createGameBt.setDisable(true);

        leaderboardBt = new Button("ERR");
        leaderboardBt.setOnAction((ActionEvent e) -> {
            try {
                Desktop.getDesktop().browse(URI.create("https://v1.api.minortom.net/jnr/leaderboard.php"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        userBox = new HBox();
        userBox.setAlignment(Pos.CENTER);
        userBox.setSpacing(50);
        userBox.setPadding(new Insets(0, 0, 0, 0));

        //avatarImg = new Image("");
        avatar = new ImageView();
        avatar.setImage(avatarImg);
        avatar.setFitWidth(game.language.getFontSize() * 4);
        avatar.setPreserveRatio(true);
        avatar.setSmooth(true);

        userName = new Label("Please wait");

        userBox.getChildren().addAll(avatar, userName);

        //updateStrings();
        setAlignment(Pos.CENTER);
        setSpacing(50);
        setPadding(new Insets(0, 0, 0, 0));
    }

    public void updateStrings() {
        defaultFont = game.language.getFont();
        backBt.setText(game.language.backBt);
        backBt.setFont(defaultFont);
        loginBt.setText(game.language.NetworManagerLoginBt);
        loginBt.setFont(defaultFont);
        joinGameBt.setText(game.language.NetworManagerJoinGameBt);
        joinGameBt.setFont(defaultFont);
        createGameBt.setText(game.language.NetworManagerCreateGameBt);
        createGameBt.setFont(defaultFont);
        leaderboardBt.setText(game.language.NetworManagerLeaderboardBt);
        leaderboardBt.setFont(defaultFont);
        userName.setFont(defaultFont);
        userBox.setSpacing(game.language.getFontSize());
        setSpacing(game.language.getFontSize());

        if (game.config.networkLoggedIn) {
            updateBtnsLoggedIn();
        } else {
            updateBtnsLoggedOut();
        }
    }

    public void openLoginScreen() {
        shutdown();
        JumpNRun.scene.setRoot(loginScreen);
        loginScreen.updateStrings();
    }

    public void openWaitScreen() {
        JumpNRun.scene.setRoot(waitScreen);
        waitScreen.updateStrings();
    }

    public void openJoinGameLoggedInScreen() {
        JumpNRun.scene.setRoot(joinGameScreenLoggedIn);
        joinGameScreenLoggedIn.updateStrings();
    }

    public void openJoinGameNotLoggedInScreen() {
        JumpNRun.scene.setRoot(joinGameScreenNotLoggedIn);
        joinGameScreenNotLoggedIn.updateStrings();
    }

    public void openCreateGameScreen() {
        JumpNRun.scene.setRoot(createGameScreen);
        createGameScreen.updateStrings();
    }

    public void openCreateGameScreenLocal() {
        JumpNRun.scene.setRoot(createGameScreenLocal);
        createGameScreenLocal.updateStrings();
    }

    public void openMapSelection() {
        openWaitScreen();
        setWaitScreenText(game.language.WaitServerAnswer, WaitAnimation.LOADING);
    }

    public void mapSelectionDone(String[] maps) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                JumpNRun.scene.setRoot(mapSelectionScreen);
                mapSelectionScreen.updateStrings(maps);
            }
        });
    }

    public void setWaitScreenText(String text) {
        waitScreen.setText(text);
    }

    public void setWaitScreenText(String text, WaitAnimation waitanim) {
        waitScreen.setText(text, waitanim);
    }

    public void updateBtnsLoggedIn() {
        joinGameBt.setDisable(false);
        createGameBt.setDisable(false);
        loginBt.setText(game.language.NetworManagerLoginBtLoggedIn);
        getChildren().clear();
        getChildren().addAll(userBox, loginBt, createGameBt, joinGameBt, leaderboardBt, backBt);
        if (game.networkManager.serverConnection == null) {
            game.networkManager.serverConnection = new ServerConnection(game.config.networkUserId, game.config.networkUserToken, game.config.networkHost, game);
            game.networkManager.serverConnection.connect();
            if (null != game.networkManager.serverConnection.currentConnState) {
                switch (game.networkManager.serverConnection.currentConnState) {
                    case CONNECTED:
                        loadAvatar();
                        break;
                    default:
                        updateBtnsLoggedOut();
                        break;
                }
            }
        }
    }

    public void connectToLocalHost(String nickname) {
        
        game.networkManager.serverConnection = new ServerConnection(nickname, "", "127.0.0.1:26656", game);
        game.networkManager.serverConnection.connect();
    }

    public void loadAvatar() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                avatarImg = new Image("https://v1.api.minortom.net/do/avatar.php?user=" + game.config.networkUserId);
                avatar.setImage(avatarImg);
            }
        });
    }

    public void updateBtnsLoggedOut() {
        joinGameBt.setDisable(true);
        createGameBt.setDisable(true);
        loginBt.setText(game.language.NetworManagerLoginBt);
        getChildren().clear();
        getChildren().addAll(loginBt, leaderboardBt, backBt);
    }

    public void shutdown() {
        try {
            serverConnection.end();
        } catch (Exception e) {
        }
    }

    public void createGameScreenSetMap(String name) {
        createGameScreen.setMapName(name);
    }

    public void setUserName(String name) {
        userName.setText(name);
    }

    public void sendKeyPress(String id, String gameName, String action) {
        serverConnection.getCommandHandler().sendCommand(ServerCommand.OGAME_KEYPRESS, new String[]{id, gameName, action});
        //serverConnection.out.println(keyword + infoSeperator + "OGAME-KEYPRESS" + infoSeperator + id + infoSeperator + gameName + infoSeperator + action);

    }

    public void sendKeyRelease(String id, String gameName, String action) {
        serverConnection.getCommandHandler().sendCommand(ServerCommand.OGAME_KEYRELEASE, new String[]{id, gameName, action});
        // serverConnection.out.println(keyword + infoSeperator + "OGAME-KEYRELEASE" + infoSeperator + id + infoSeperator + gameName + infoSeperator + action);

    }
}
