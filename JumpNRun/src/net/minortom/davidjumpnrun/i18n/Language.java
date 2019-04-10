/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.i18n;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.text.Font;
import jumpnrun.JumpNRun;
import static jumpnrun.JumpNRun.sourcePath;
import net.minortom.davidjumpnrun.configstore.ConfigManager;

public class Language implements Serializable {
    // Font Info
    private int fontSize;
    private String fontName, fontUrl;
    transient private Font standardFont;
    transient private Font headerFont;
    
    // Start variables
    
    //Multiple uses:
    public transient String backBt;
    public transient String okBt;
    
    // Language names
    public transient String langEN;
    public transient String langDE;
    
    // Global
    public transient String GWindowName;
    
    // JumpNRun.java
    public transient String JNRCfgDirCorrectPopTitle;
    public transient String JNRCfgDirCorrectPopText1;
    public transient String JNRCfgDirCorrectPopText2;
    
    // Graphic.java
    public transient String GraphicRespawnsLeft;
    public transient String GraphicKills;
    
    // Main Menu
    public transient String MainMenuPlayBt;
    public transient String MainMenuNetworkBt;
    public transient String MainMenuExitBt;
    public transient String MainMenuFontSizeLabel;
    public transient String MainMenuFontSizePBt;
    public transient String MainMenuFontSizeMBt;
    public transient String MainMenuLangLabel;
    public transient String MainMenuCreditsBt;
    public transient String MainMenuWorldEditorBt;
    public transient String MainMenuSettingsLbl;
    
    // Credits Screen
    public transient String CreditsHeader;
    public transient String CreditsCloseBt;
    
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
    public transient String ChoGmCMBt;
    public transient String ChoGmDMBt;
    
    // Create Game Screen
    public transient String CreateGChooseMapBt;
    public transient String CreateGNameLbl;
    public transient String CreateGPlayersLbl;
    public transient String CreateGPlayers2Lbl;
    
    // Map Choose Menu
    
    // Skin Schoose Menu
    public transient String SkinColorBlue;
    public transient String SkinColorGreen;
    public transient String SkinColorOrange;
    public transient String SkinColorYellow;
    public transient String SkinColorRed;
    public transient String SkinChooseDefaultHeading;
    public transient String SkinChooseOfflinePlayer1Heading;
    public transient String SkinChooseOfflinePlayer2Heading;
    
    // Wait Screen
    public transient String WaitServerAnswer;
    public transient String WaitOtherPlayersA;
    public transient String WaitOtherPlayersB;
    public transient String WaitGameStart;
    
    // Online Errors
    public transient String ErrorTitle;
    public transient String ErrorUnknown;
    public transient String ErrorFatalTitle;
    public transient String ErrorNameAlreadyExists;
    public transient String ErrorNameDoesntExist;
    
    // ConfigManager
    public transient String CfgManErrorStorageLocationTitle;
    public transient String CfgManErrorStorageLocationText;
    public transient String CfgManErrorLoadTitle;
    public transient String CfgManErrorLoadText;
    public transient String CfgManErrorSaveTitle;
    public transient String CfgManErrorSaveText;
    
    // Gameplay
    public transient String playerNameLocalPlayer;
    
    // IO
    public transient String IOReadErrorTitle;
    public transient String IOReadErrorTextA;
    public transient String IOReadErrorTextB;
    
    // World Editor
    public transient String WorldEditTitle;
    public transient String WorldEditMainMenu;
    public transient String WorldEditFile;
    public transient String WorldEditSave;
    public transient String WorldEditSaveAt;
    public transient String WorldEditOpen;
    public transient String WorldEditAddBlock;
    public transient String WorldEditMainMenuOpen;
    public transient String WorldEditErrOnlyNumbers;
    
    
    public transient int changeB;
    
    public transient JumpNRun game;
    
    public Language (JumpNRun newGame){
        fontName = "Maiandra GD";
        fontUrl = "sprites/font/font.ttf";
        fontSize = 30;
        game = newGame;
        
        setFontFileNC(fontUrl, fontName);
    }
    
    public void setFontSize(int size){
        setFontSizeNC(size);
        saveChanges();
    }
    
    public void setFontSizeNC(int size){
        fontSize = size;
        setFontFile();
    }
    
    public int getFontSize(){
        return fontSize;
    }
    
    public int getHeaderSize(){
        return fontSize * 2;
    }
    
    @Deprecated
    public void setFontName(String newf){
        setFontNameNC(newf);
        saveChanges();
    }
    
    @Deprecated
    public void setFontNameNC(String newf){
        fontName = newf;
    }
    
    public void setFontFile(String newf, String newfname){
        setFontFileNC(newf, newfname);
        saveChanges();
    }
    
    public void setFontFile(){
        setFontFileNC();
        saveChanges();
    }
    
    public void setFontFileNC(String newf, String newfname){
        fontName = newfname;
        fontUrl = newf;
        setFontFileNC();
    }
    
    public void setFontFileNC(){
        if(true){
            standardFont = new Font(fontName, getFontSize());
            headerFont = new Font(fontName, getHeaderSize());
        } else {
            try {
                standardFont = Font.loadFont(new File(sourcePath + fontUrl).toURI().toURL().toString(), getFontSize());
                headerFont = Font.loadFont(new File(sourcePath + fontUrl).toURI().toURL().toString(), getHeaderSize());
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public String getFontName(){
        return fontName;
    }
    
    public String getFontUrl(){
        return fontUrl;
    }
    
    public Font getFont(){
        return standardFont;
    }
    
    public Font getHeadingFont(){
        return headerFont;
    }
    
    public String getShortName(){
        return "ERR";
    }
    
    public static String defaultLang(){
        return "EN";
    }
    
    public static Language setNewLang(Language oldLang, Language newLang){
        newLang.setFontSizeNC(oldLang.getFontSize());
        newLang.setFontFileNC(oldLang.getFontUrl(), oldLang.getFontName());
        newLang.saveChanges();
        return newLang;
    }
    
    public static Language setNewLangNC(Language oldLang, Language newLang){
        newLang.setFontSizeNC(oldLang.getFontSize());
        newLang.setFontFileNC(oldLang.getFontUrl(), oldLang.getFontName());
        return newLang;
    }
    
    public void saveChanges(){
        game.config.gameLanguage = this;
        ConfigManager.saveConfiguration(game.config);
    }
}