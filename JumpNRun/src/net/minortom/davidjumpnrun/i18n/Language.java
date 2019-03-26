/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.i18n;

import java.io.Serializable;
import javafx.scene.text.Font;
import jumpnrun.JumpNRun;
import net.minortom.davidjumpnrun.configstore.ConfigManager;

public class Language implements Serializable {
    // Font Info
    private int fontSize;
    private String fontName;
    
    // Start variables
    
    //Multiple uses:
    public transient String backBt;
    
    // Language names
    public transient String langEN;
    public transient String langDE;
    
    // Global
    public transient String GWindowName;
    
    // JumpNRun.java
    public transient String JNRCfgDirCorrectPopTitle;
    public transient String JNRCfgDirCorrectPopText1;
    public transient String JNRCfgDirCorrectPopText2;
    
    // Main Menu
    public transient String MainMenuPlayBt;
    public transient String MainMenuNetworkBt;
    public transient String MainMenuExitBt;
    public transient String MainMenuFontSizeLabel;
    public transient String MainMenuFontSizePBt;
    public transient String MainMenuFontSizeMBt;
    public transient String MainMenuLangLabel;
    
    // Login Screen
    public transient String LoginScreenLoginLabel;
    public transient String LoginScreenPassLabel;
    public transient String LoginScreenServerLabel;
    public transient String LoginScreenDoLoginBt;
    public transient String LoginScreenConnecting;
    public transient String LoginScreenErrorInvalidCharTitle;
    public transient String LoginScreenErrorInvalidCharText;
    
    // Network Play Screen
    public transient String NetworManagerLoginBt;
    public transient String NetworManagerJoinGameBt;
    public transient String NetworManagerCreateGameBt;
    public transient String NetworkManagerConnected;
    public transient String NetworkManagerInternalError;
    public transient String NetworkManagerInvalidHost;
    public transient String NetworkManagerInvalidAuth;
    
    // Join Game Screen
    public transient String JoinGNameLabel;
    public transient String JoinGOkBt;
    
    public transient String GSkinBt;
    
    // Choose Game Mode Menu
    public transient String ChoGmEndlessBt;
    public transient String ChoGmTimeBt;
    public transient String ChoGmTimeLbl;
    public transient String ChoGmDeathsBt;
    public transient String ChoGmDeathsLbl;
    public transient String ChoGmOkBt;
    public transient String ChoGmErrOnlyNumbers;
    public transient String ChoGmErrOnlyWholeNumbers;
    
    // Create Game Screen
    public transient String CreateGChooseMapBt;
    public transient String CreateGNameLbl;
    public transient String CreateGPlayersLbl;
    public transient String CreateGPlayers2Lbl;
    
    // Map Choose Menu
    
    // Wait Screen
    public transient String WaitServerAnswer;
    public transient String WaitOtherPlayersA;
    public transient String WaitOtherPlayersB;
    public transient String WaitGameStart;
    
    // ConfigManager
    public transient String CfgManErrorStorageLocationTitle;
    public transient String CfgManErrorStorageLocationText;
    public transient String CfgManErrorLoadTitle;
    public transient String CfgManErrorLoadText;
    public transient String CfgManErrorSaveTitle;
    public transient String CfgManErrorSaveText;
    
    public transient JumpNRun game;
    
    public Language (JumpNRun newGame){
        fontName = "Maiandra GD";
        fontSize = 30;
        game = newGame;
    }
    
    public void setFontSize(int size){
        setFontSizeNC(size);
        saveChanges();
    }
    
    public void setFontSizeNC(int size){
        fontSize = size;
    }
    
    public int getFontSize(){
        return fontSize;
    }
    
    public void setFontName(String newf){
        setFontNameNC(newf);
        saveChanges();
    }
    
    public void setFontNameNC(String newf){
        fontName = newf;
    }
    
    public String getFontName(){
        return fontName;
    }
    
    public Font getFont(){
        return new Font(fontName, fontSize);
    }
    
    public String getShortName(){
        return "ERR";
    }
    
    public static String defaultLang(){
        return "EN";
    }
    
    public static Language setNewLang(Language oldLang, Language newLang){
        newLang.setFontNameNC(oldLang.getFontName());
        newLang.setFontSizeNC(oldLang.getFontSize());
        newLang.saveChanges();
        return newLang;
    }
    
    public static Language setNewLangNC(Language oldLang, Language newLang){
        newLang.setFontNameNC(oldLang.getFontName());
        newLang.setFontSizeNC(oldLang.getFontSize());
        return newLang;
    }
    
    public void saveChanges(){
        game.config.gameLanguage = this;
        ConfigManager.saveConfiguration(game.config);
    }
}
