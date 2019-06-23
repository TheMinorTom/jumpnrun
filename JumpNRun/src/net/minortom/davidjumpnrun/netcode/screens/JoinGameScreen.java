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
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jumpnrun.JumpNRun;
import jumpnrun.SkinChooseMenu;
import net.minortom.davidjumpnrun.netcode.NetworkManager;
import net.minortom.davidjumpnrun.netcode.ServerCommand;
import net.minortom.davidjumpnrun.netcode.ServerConnection;
import net.minortom.davidjumpnrun.netcode.screens.WaitScreen.WaitAnimation;

public class JoinGameScreen extends VBox {

    JumpNRun game;
    Label nameLabel, skinLbl, headingLbl, playerNameLbl, serverNameLbl, kantConnectsToServerLbl;
    Button okBt, backBt, skinBt;
    TextField nameField, playerNameTF, serverNameTF;
    HBox btBox;
    VBox skinBox;
    SkinChooseMenu.Skin skin;
    String skinUrl;
    private boolean isLoggedIn;

    public JoinGameScreen(JumpNRun game, boolean isLoggedIn) {
        this.game = game;
        this.isLoggedIn = isLoggedIn;

        nameLabel = new Label("ERR");
        skinLbl = new Label();
        headingLbl = new Label();
        okBt = new Button("ERR");
        backBt = new Button("ERR");
        skinBt = new Button("ERR");

        okBt.setDisable(true);

        nameField = new TextField();
        // nameField.getStyleClass().add("-fx-alignment: center");

        btBox = new HBox(backBt, okBt);
        btBox.setAlignment(Pos.CENTER);
        btBox.setSpacing(100);
        btBox.setPadding(new Insets(0, 0, 0, 0));

        skinBox = new VBox(skinBt, skinLbl);
        skinBox.setAlignment(Pos.CENTER);
        skinBox.setSpacing(15);
        skinBox.setPadding(new Insets(0, 0, 0, 0));

        playerNameLbl = new Label(game.language.PlayerNameLabel);
        playerNameTF = new TextField();
        serverNameLbl = new Label();
        serverNameTF = new TextField();
        kantConnectsToServerLbl = new Label();

        updateStrings();

        setAlignment(Pos.CENTER);
        setSpacing(50);
        setPadding(new Insets(0, 0, 0, 0));

    }

    public void updateStrings() {
        getChildren().clear();
        if (isLoggedIn) {

            okBt.setOnAction((ActionEvent e) -> {
                game.networkManager.openWaitScreen();
                game.networkManager.setWaitScreenText(game.language.WaitServerAnswer, WaitAnimation.LOADING);
                String skin = skinUrl.split("/")[skinUrl.split("/").length - 1];
                // game.networkManager.serverConnection.out.println(NetworkManager.keyword + NetworkManager.infoSeperator + "OGAME-JOIN" + NetworkManager.infoSeperator + nameField.getText() + NetworkManager.infoSeperator + skin);
                game.networkManager.serverConnection.getCommandHandler().sendCommand(ServerCommand.OGAME_JOIN, new String[]{nameField.getText(), skin});
            });
            backBt.setOnAction((ActionEvent e) -> {
                game.openNetworkScreen();
            });

            skinBt.setOnAction((ActionEvent e) -> {
                game.openOnlineSkinChooseJoinGameMenuLoggedIn();
            });

            getChildren().addAll(headingLbl, new Separator(), nameLabel, nameField, new Separator(), skinBox, new Separator(), btBox);
        } else {

            getChildren().addAll(headingLbl, new Separator(), serverNameLbl, serverNameTF, kantConnectsToServerLbl, new Separator(), playerNameLbl, playerNameTF, new Separator(), nameLabel, nameField, new Separator(), skinBox, new Separator(), btBox);

            backBt.setOnAction((ActionEvent e) -> {
                game.openPlayOnlineScreen();

            });

            skinBt.setOnAction((ActionEvent e) -> {
               game.openOnlineSkinChooseJoinGameNotMenuLoggedIn();
            });

            okBt.setOnAction((ActionEvent e) -> {

                if (game.networkManager.serverConnection == null) {
                    game.networkManager.serverConnection = new ServerConnection("-1", playerNameTF.getText(), serverNameTF.getText(), game);
                    game.networkManager.serverConnection.connect();
                    if (null != game.networkManager.serverConnection.currentConnState) {
                        switch (game.networkManager.serverConnection.currentConnState) {
                            case CONNECTED:
                                //playAs.setText(game.language.plOnlMnPlayAs + game.networkManager.serverConnection.userName);
                                game.networkManager.loadAvatar();
                                kantConnectsToServerLbl.setText(game.language.NetworkManagerConnected);
                                break;
                            default:
                                kantConnectsToServerLbl.setText(game.language.NetworkManagerInvalidHost);
                                //updateButtonsLoggedOut();
                                break;
                        }

                    }
                }

                game.networkManager.openWaitScreen();
                game.networkManager.setWaitScreenText(game.language.WaitServerAnswer, WaitAnimation.LOADING);
                String skin = skinUrl.split("/")[skinUrl.split("/").length - 1];
                // game.networkManager.serverConnection.out.println(NetworkManager.keyword + NetworkManager.infoSeperator + "OGAME-JOIN" + NetworkManager.infoSeperator + nameField.getText() + NetworkManager.infoSeperator + skin);
                game.networkManager.serverConnection.getCommandHandler().sendCommand(ServerCommand.OGAME_JOIN, new String[]{nameField.getText(), skin});

            });

        }
        playerNameLbl.setFont(game.language.getFont());
        playerNameLbl.setText(game.language.PlayerNameLabel);

        playerNameTF.setFont(game.language.getFont());

        nameLabel.setText(game.language.JoinGNameLabel);
        nameLabel.setFont(game.language.getFont());

        headingLbl.setFont(game.language.getHeadingFont());
        headingLbl.setText(game.language.NetworManagerJoinGameBt);

        skinLbl.setFont(game.language.getFont());
        serverNameTF.setFont(game.language.getFont());
        serverNameLbl.setFont(game.language.getFont());
        serverNameLbl.setText(game.language.JoinGameServerName);

        okBt.setFont(game.language.getFont());
        backBt.setFont(game.language.getFont());
        skinBt.setFont(game.language.getFont());

        okBt.setText(game.language.JoinGOkBt);
        backBt.setText(game.language.backBt);
        skinBt.setText(game.language.GSkinBt);

        nameField.setFont(game.language.getFont());

        kantConnectsToServerLbl.setFont(game.language.getFont());

        setSpacing(game.language.getFontSize());

    }

    public Button getOkBt() {
        return okBt;
    }

    public void setSkinChosen(boolean b, SkinChooseMenu.Skin skin, String text) {
        this.skin = skin;
        okBt.setDisable(!b);
        skinUrl = skin.path;
        skinLbl.setText(text);

    }
}
