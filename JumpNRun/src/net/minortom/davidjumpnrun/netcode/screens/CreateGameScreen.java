/*
 * This file is not licensed.
 */
package net.minortom.davidjumpnrun.netcode.screens;

import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import jumpnrun.JumpNRun;
import jumpnrun.SkinChooseMenu;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import net.minortom.davidjumpnrun.netcode.NetworkManager;
import net.minortom.davidjumpnrun.netcode.ServerCommand;
import net.minortom.davidjumpnrun.netcode.screens.WaitScreen.WaitAnimation;

public class CreateGameScreen extends VBox {

    JumpNRun game;

    private RadioButton endlessBt, timeBt, deathsBt;
    private Button backBt, okBt, chooseMapBt, skinBt;
    private TextField timeTF, deathsTF, playersTF, nameTF;
    private Label timeLbl, deathsLbl, playersLbl, nameLbl, mapNameLbl, skinLbl, players2Lbl;
    private HBox timeBox, deathsBox, btBox, playersBox, mapBox, skinBox;
    private String nickname;    // ONLY LOCAL

    SkinChooseMenu.Skin skin;
    String skinUrl;
    String gameMode;

    private SkinChooseMenu skinChooseMenu;

    private double timeLimit; //In seconds
    private double deathLimit;

    private boolean toggleschanged = false;
    private boolean mapselected = false;
    private boolean skinchosen = false;
    private double mapSize;
    private boolean nameTFNotEmpty = false;
    private boolean playersTFNotEmpty = false;

    private boolean isLocal;
    private String worldPath;       // ONLY IF LOCAL

    public CreateGameScreen(JumpNRun game, boolean isLocal) {
        nickname = "";
        this.isLocal = isLocal;
        this.game = game;

        worldPath = "";

        timeLimit = 5;
        deathLimit = 10;
        mapSize = 0;
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                toggleschanged = true;
                unlockOk();
                if (toggleGroup.getSelectedToggle().equals(endlessBt)) {
                    timeTF.setDisable(true);
                    deathsTF.setDisable(true);
                    gameMode = "ENDLESS";
                } else if (toggleGroup.getSelectedToggle().equals(timeBt)) {
                    timeTF.setDisable(false);
                    deathsTF.setDisable(true);
                    gameMode = "TIME";
                } else if (toggleGroup.getSelectedToggle().equals(deathsBt)) {
                    timeTF.setDisable(true);
                    deathsTF.setDisable(false);
                    gameMode = "DEATHS";
                }
            }

        });

        endlessBt = new RadioButton("Unendlich");
        endlessBt.setToggleGroup(toggleGroup);

        timeBt = new RadioButton("Zeitbegrenzung: ");
        timeBt.setToggleGroup(toggleGroup);

        timeTF = new TextField("5.0");
        timeTF.setDisable(true);
        timeTF.setPrefWidth(120);

        timeLbl = new Label(" Minuten");

        timeBox = new HBox(timeBt, timeTF, timeLbl);

        deathsBt = new RadioButton("Respawnbegrenzung: ");
        deathsBt.setToggleGroup(toggleGroup);

        deathsTF = new TextField("10");
        deathsTF.setDisable(true);
        deathsTF.setPrefWidth(120);

        deathsLbl = new Label(" Respawns");

        deathsBox = new HBox(deathsBt, deathsTF, deathsLbl);

        backBt = new Button("Zurück");
        chooseMapBt = new Button("ERR");
        skinBt = new Button("ERR");

        if (isLocal) {
            backBt.setOnAction((ActionEvent e) -> {
                game.openPlayLocalScreen();
            });

            chooseMapBt.setOnAction((ActionEvent e) -> {
                // TO DO
                unlockOk();
            });

            skinBt.setOnAction((ActionEvent e) -> {
                game.openLocalSkinChooseCreateGame();
            });

            chooseMapBt.setOnAction((ActionEvent e) -> {
                FileChooser fc = new FileChooser();
                fc.setInitialDirectory(new File(JumpNRun.sourcePath + "worlds/"));
                worldPath = (fc.showOpenDialog(game.getPrimStage())).getPath();
                if (!worldPath.endsWith(".david")) {
                    worldPath = "";
                    ConfigManager.error("Game", "Selected file is not a valid world (.david)");
                    chooseMapBt.fire();
                }
                String[] splittedPath = worldPath.split("\\" + File.separator);
                setMapName(splittedPath[splittedPath.length - 1]);
                mapselected = true;
                unlockOk();
            });
        } else {

            backBt.setOnAction((ActionEvent e) -> {
                game.openNetworkScreen();
            });

            chooseMapBt.setOnAction((ActionEvent e) -> {
                game.networkManager.openMapSelection();
                game.networkManager.mapSelectionScreen.userRequest();
                mapselected = true;
                unlockOk();
            });

            skinBt.setOnAction((ActionEvent e) -> {
                game.openOnlineSkinChooseCreateGameMenu();
            });

        }

        okBt = new Button("Ok");
        okBt.setDisable(true);

        playersTF = new TextField();

        nameTF = new TextField();

        nameTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.isEmpty()) {
                    nameTFNotEmpty = false;
                } else {
                    nameTFNotEmpty = true;
                }
                unlockOk();
            }
        });

        playersTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.isEmpty()) {
                    playersTFNotEmpty = false;
                } else {
                    playersTFNotEmpty = true;
                }

                unlockOk();
            }
        });

        mapNameLbl = new Label();

        skinLbl = new Label();

        playersLbl = new Label("ERR");

        players2Lbl = new Label("ERR");

        nameLbl = new Label("ERR");

        playersBox = new HBox(playersLbl, playersTF, players2Lbl);
        playersBox.setAlignment(Pos.CENTER_LEFT);
        playersBox.setSpacing(20);
        playersBox.setPadding(new Insets(0, 0, 0, 0));

        mapBox = new HBox(chooseMapBt, mapNameLbl);
        mapBox.setAlignment(Pos.CENTER_LEFT);
        mapBox.setSpacing(20);
        mapBox.setPadding(new Insets(0, 0, 0, 0));

        skinBox = new HBox(skinBt, skinLbl);
        skinBox.setAlignment(Pos.CENTER_LEFT);
        skinBox.setSpacing(20);
        skinBox.setPadding(new Insets(0, 0, 0, 0));

        // End licensed sections
        btBox = new HBox(backBt, okBt);
        btBox.setAlignment(Pos.CENTER_LEFT);
        btBox.setSpacing(100);
        btBox.setPadding(new Insets(0, 0, 0, 100));

        updateStrings();

        setAlignment(Pos.CENTER_LEFT);
        setSpacing(50);
        setPadding(new Insets(0, 0, 0, 150));
        getChildren().addAll(nameLbl, nameTF, playersBox, timeBox, deathsBox, endlessBt, mapBox, skinBox, btBox);
    }

    public void updateStrings() {
        Font defaultFont = game.language.getFont();
        endlessBt.setFont(defaultFont);
        timeBt.setFont(defaultFont);
        timeTF.setFont(defaultFont);
        timeLbl.setFont(defaultFont);
        deathsBt.setFont(defaultFont);
        deathsTF.setFont(defaultFont);
        deathsLbl.setFont(defaultFont);
        backBt.setFont(defaultFont);
        okBt.setFont(defaultFont);

        backBt.setText(game.language.backBt);
        endlessBt.setText(game.language.ChoGmEndlessBt);
        timeBt.setText(game.language.ChoGmTimeBt);
        timeLbl.setText(game.language.ChoGmTimeLbl);
        deathsBt.setText(game.language.ChoGmDeathsBt);
        deathsLbl.setText(game.language.ChoGmDeathsLbl);
        okBt.setText(game.language.ChoGmOkBt);

        okBt.setOnAction((ActionEvent e) -> {
            if (timeBt.isSelected()) {
                try {
                    double x = Double.parseDouble(timeTF.getText());
                } catch (NumberFormatException n) {
                    timeTF.setText(game.language.ChoGmErrOnlyNumbers);
                }
            } else if (deathsBt.isSelected()) {
                try {
                    int x = Integer.parseInt(deathsTF.getText());
                } catch (NumberFormatException n) {
                    deathsTF.setText(game.language.ChoGmErrOnlyWholeNumbers);
                }
            } else if (endlessBt.isSelected()) {

            }
            try {
                int z = Integer.parseInt(playersTF.getText());
            } catch (NumberFormatException n) {
                deathsTF.setText(game.language.ChoGmErrOnlyWholeNumbers);
            }

            String limit = "0";
            if ("DEATHS".equals(gameMode)) {
                limit = deathsTF.getText();
            } else if ("TIME".equals(gameMode)) {
                limit = timeTF.getText();
            }
            String skin = skinUrl.split("/")[skinUrl.split("/").length - 1];
            // game.networkManager.serverConnection.out.println(NetworkManager.keyword + NetworkManager.infoSeperator + "OGAME-CREATE" + NetworkManager.infoSeperator + nameTF.getText() + NetworkManager.infoSeperator + playersTF.getText() + NetworkManager.infoSeperator + gameMode + NetworkManager.infoSeperator + limit + NetworkManager.infoSeperator + mapNameLbl.getText() + NetworkManager.infoSeperator + skin);

            if (isLocal) {
                game.networkManager.openWaitScreen();
                game.networkManager.setWaitScreenText(game.language.WaitServerAnswer, WaitAnimation.LOADING);
                game.launchLocalSever();

                game.networkManager.connectToLocalHost(nickname);
                game.networkManager.serverConnection.getCommandHandler().sendCommand(ServerCommand.OGAME_CREATE, new String[]{nameTF.getText(), playersTF.getText(), gameMode, limit, mapNameLbl.getText(), skin});
            } else {
                game.networkManager.openWaitScreen();
                game.networkManager.setWaitScreenText(game.language.WaitServerAnswer, WaitAnimation.LOADING);
                game.networkManager.serverConnection.getCommandHandler().sendCommand(ServerCommand.OGAME_CREATE, new String[]{nameTF.getText(), playersTF.getText(), gameMode, limit, mapNameLbl.getText(), skin});

            }
        });

        // The following sections are licensed under the MIT License. You should have already received a copy located at ../net/minortom/LICENSE.txt
        // Copyright 2019 MinorTom <mail in license file>
        chooseMapBt.setFont(defaultFont);
        skinBt.setFont(defaultFont);
        playersTF.setFont(defaultFont);
        nameTF.setFont(defaultFont);
        mapNameLbl.setFont(defaultFont);
        skinLbl.setFont(defaultFont);
        playersLbl.setFont(defaultFont);
        players2Lbl.setFont(defaultFont);
        nameLbl.setFont(defaultFont);

        chooseMapBt.setText(game.language.CreateGChooseMapBt);
        skinBt.setText(game.language.GSkinBt);
        playersLbl.setText(game.language.CreateGPlayersLbl);
        players2Lbl.setText(game.language.CreateGPlayers2Lbl);
        nameLbl.setText(game.language.CreateGNameLbl);

        setSpacing(game.language.getFontSize());

        // End licensed sections
    }

    // The following sections are licensed under the MIT License. You should have already received a copy located at ../net/minortom/LICENSE.txt
    // Copyright 2019 MinorTom <mail in license file>
    public void setNickname (String name) {
        nickname = name;
    }
    
    public void unlockOk() {
        if (toggleschanged && mapselected && skinchosen && nameTFNotEmpty && playersTFNotEmpty) {
            okBt.setDisable(false);
        } else {
            okBt.setDisable(true);
        }
    }

    public void setMapName(String name) {
        mapNameLbl.setText(name);
    }

    public void setMapSize(double m) {
        mapSize = m;
    }

    public void setSkinChosen(boolean b, SkinChooseMenu.Skin skin, String skinName) {
        this.skin = skin;
        skinchosen = b;
        unlockOk();
        this.skinUrl = skin.path;
        skinLbl.setText(skinName);
    }

    // End licensed sections
}
