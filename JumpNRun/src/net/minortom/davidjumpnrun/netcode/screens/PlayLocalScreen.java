/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minortom.davidjumpnrun.netcode.screens;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jumpnrun.JumpNRun;

/**
 *
 * @author d.betko
 */
public class PlayLocalScreen extends VBox {

    private static JumpNRun game;
    private Button createGameButton, joinGameButton, backBt;
    private TextField nameInput;
    private Label nicknameLbl;

    private boolean isNickname;

    public PlayLocalScreen(JumpNRun game) {
        this.game = game;
        isNickname = false;

        createGameButton = new Button("ERR");
        createGameButton.setDisable(true);
        createGameButton.setOnAction((ActionEvent e)->{
            game.networkManager.openCreateGameScreenLocal();
        });

        joinGameButton = new Button("ERR");
        joinGameButton.setDisable(true);
        joinGameButton.setOnAction((ActionEvent e)->{
            game.networkManager.openJoinGameNotLoggedInScreen();
        });

        backBt = new Button("ERR");
        backBt.setOnAction((ActionEvent e) -> {
            game.openMainMenu();
        });

        nicknameLbl = new Label();

        nameInput = new TextField();
        nameInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setButtonsDisabled(newValue.equals("") || (newValue == null));
                game.networkManager.updateNickName(newValue);
            }

        });

        getChildren().addAll(nicknameLbl, nameInput, new Separator(Orientation.HORIZONTAL), createGameButton, joinGameButton, new Separator(Orientation.HORIZONTAL), backBt);
        updateStrings();

        setAlignment(Pos.CENTER_LEFT);
        setSpacing(50);
        setPadding(new Insets(0, 50, 0, 50));

    }

    public void updateStrings() {
        Font defaultFont = game.language.getFont();
        createGameButton.setText(game.language.NetworManagerCreateGameBt);
        createGameButton.setFont(defaultFont);

        joinGameButton.setText(game.language.NetworManagerJoinGameBt);
        joinGameButton.setFont(defaultFont);

        backBt.setText(game.language.backBt);
        backBt.setFont(defaultFont);

        nicknameLbl.setText(game.language.LocalEnterNameLabel);
        nicknameLbl.setFont(defaultFont);

        nameInput.setPromptText(game.language.LocalEnterNameInfo);
        nameInput.setFont(defaultFont);

        setButtonsDisabled(!isNickname);

    }

    private void setButtonsDisabled(boolean bool) {
        joinGameButton.setDisable(bool);
        createGameButton.setDisable(bool);
        isNickname = !bool;
    }

}
