/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import net.minortom.davidjumpnrun.i18n.Language;
import net.minortom.davidjumpnrun.i18n.LanguageEnglish;
import net.minortom.davidjumpnrun.i18n.LanguageGerman;

/**
 *
 * @author DavidPrivat
 */
public class MainMenu extends VBox {

    private Button playBt, exitBt, onlineBt, fontSizePBt, fontSizeMBt, creditsBt;
    private Label fontSizeLabel, langLabel;
    private HBox fontSizeBox;
    RadioButton langSelectEN;
    RadioButton langSelectDE;
    private VBox fontSizeVBox, langBox;
    
    JumpNRun game;

    public MainMenu(JumpNRun stgame) {
        game = stgame;
        
        playBt = new Button("Spielen");
        playBt.setOnAction((ActionEvent e) -> {
            game.openChooseGamemodeMenu();
        });

        onlineBt = new Button("ERR");
        onlineBt.setOnAction((ActionEvent e) -> {
            game.openNetworkScreen();
        });
        
        exitBt = new Button("Spiel verlassen");
        exitBt.setOnAction((ActionEvent e) -> {

            try {
                game.stop();
            } catch (Exception ex) {
                System.out.println("Couldn't stop");
            }

        });
        
        // The following sections are licensed under the MIT License. You should have already received a copy located at ../net/minortom/LICENSE.txt
        // Copyright 2019 MinorTom <mail in license file>
        
        creditsBt = new Button("ERR");
        creditsBt.setOnAction((ActionEvent e) -> {
            game.openCreditsScreen();
        });
        
        fontSizeLabel = new Label("ERR");
        langLabel = new Label("ERR");
        
        fontSizePBt = new Button("A+");
        fontSizePBt.setOnAction((ActionEvent e) -> {
            if(game.language.getFontSize()<100){
                game.language.setFontSize(game.language.getFontSize()+2);
            }
            updateStrings();
        });
        
        fontSizeMBt = new Button("A-");
        fontSizeMBt.setOnAction((ActionEvent e) -> {
            if(game.language.getFontSize()>2){
                game.language.setFontSize(game.language.getFontSize()-2);
            }
            updateStrings();
        });
        
        fontSizeBox = new HBox(fontSizePBt, fontSizeMBt);
        fontSizeBox.setAlignment(Pos.CENTER);
        fontSizeBox.setSpacing(25);
        fontSizeBox.setPadding(new Insets(0, 0, 0, 0));
        
        fontSizeVBox = new VBox(fontSizeLabel, fontSizeBox);
        fontSizeVBox.setAlignment(Pos.CENTER);
        fontSizeVBox.setSpacing(17);
        fontSizeVBox.setPadding(new Insets(0, 0, 0, 0));
        
        
        
        
        ToggleGroup langGroup = new ToggleGroup();
        langSelectDE = new RadioButton("ERR");
        langSelectEN = new RadioButton("ERR");
        langSelectDE.setToggleGroup(langGroup);
        langSelectEN.setToggleGroup(langGroup);
        langGroup.selectedToggleProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (langGroup.getSelectedToggle().equals(langSelectDE)) {
                    game.language = Language.setNewLang(game.language, new LanguageGerman(game));
                    updateStrings();
                } else if (langGroup.getSelectedToggle().equals(langSelectEN)) {
                    game.language = Language.setNewLang(game.language, new LanguageEnglish(game));
                    updateStrings();
                }
            }

        });
        langBox = new VBox(langLabel, langSelectDE, langSelectEN);
        langBox.setAlignment(Pos.CENTER);
        langBox.setSpacing(17);
        langBox.setPadding(new Insets(0, 0, 0, 0));
        
        //Language Selection default
        if(null == game.language.getShortName()){
            
        } else 
        switch (game.language.getShortName()) {
            case "EN":
                langSelectEN.setSelected(true);
                break;
            case "DE":
                langSelectDE.setSelected(true);
                break;
            default:
                break;
        }
        
        
        
        updateStrings();
        
        // End licensed sections
        
        setSpacing(game.language.getFontSize());
        
        getChildren().addAll(playBt, onlineBt, exitBt, fontSizeVBox, langBox, creditsBt);
        setAlignment(Pos.CENTER);
    }
    
    public void updateStrings(){
        Font btFont = game.language.getFont();
        
        playBt.setFont(btFont);
        playBt.setText(game.language.MainMenuPlayBt);
        onlineBt.setFont(btFont);
        onlineBt.setText(game.language.MainMenuNetworkBt);
        exitBt.setFont(btFont);
        exitBt.setText(game.language.MainMenuExitBt);
        fontSizeLabel.setText(game.language.MainMenuFontSizeLabel);
        fontSizeLabel.setFont(btFont);
        langLabel.setText(game.language.MainMenuLangLabel);
        langLabel.setFont(btFont);
        fontSizePBt.setFont(btFont);
        fontSizePBt.setText(game.language.MainMenuFontSizePBt);
        fontSizeMBt.setFont(btFont);
        fontSizeMBt.setText(game.language.MainMenuFontSizeMBt);
        langSelectDE.setFont(btFont);
        langSelectEN.setFont(btFont);
        langSelectDE.setText(game.language.langDE);
        langSelectEN.setText(game.language.langEN);
        creditsBt.setFont(btFont);
        creditsBt.setText(game.language.MainMenuCreditsBt);
    }

}
