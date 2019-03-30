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
import jumpnrun.JumpNRun;
import jumpnrun.SkinChooseMenu;
import net.minortom.davidjumpnrun.netcode.NetworkManager;

public class JoinGameScreen extends VBox {

    JumpNRun game;
    Label nameLabel, skinLbl;
    Button okBt, backBt, skinBt;
    TextField nameField;
    HBox btBox;
    VBox skinBox;
    SkinChooseMenu.Skin skin;
    String skinUrl;

    public JoinGameScreen(JumpNRun game) {
        this.game = game;

        nameLabel = new Label("ERR");
        skinLbl = new Label();

        okBt = new Button("ERR");
        backBt = new Button("ERR");
        skinBt = new Button("ERR");

        backBt.setOnAction((ActionEvent e) -> {
            game.openNetworkScreen();
        });

        okBt.setOnAction((ActionEvent e) -> {
            game.networkManager.openWaitScreen();
            game.networkManager.setWaitScreenText(game.language.WaitServerAnswer);
            String skin = skinUrl.split("/")[skinUrl.split("/").length - 1];
            game.networkManager.serverConnection.out.println(NetworkManager.keyword + NetworkManager.infoSeperator + "OGAME-JOIN" + NetworkManager.infoSeperator + nameField.getText() + NetworkManager.infoSeperator + skin);
        });
        okBt.setDisable(true);

        skinBt.setOnAction((ActionEvent e) -> {
            game.openOnlineSkinChooseJoinGameMenu();
        });

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

        updateStrings();

        setAlignment(Pos.CENTER);
        setSpacing(50);
        setPadding(new Insets(0, 0, 0, 0));
        getChildren().addAll(nameLabel, nameField, skinBox, btBox);
    }

    public void updateStrings() {
        nameLabel.setText(game.language.JoinGNameLabel);
        nameLabel.setFont(game.language.getFont());

        skinLbl.setFont(game.language.getFont());

        okBt.setFont(game.language.getFont());
        backBt.setFont(game.language.getFont());
        skinBt.setFont(game.language.getFont());

        okBt.setText(game.language.JoinGOkBt);
        backBt.setText(game.language.backBt);
        skinBt.setText(game.language.GSkinBt);

        nameField.setFont(game.language.getFont());
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
