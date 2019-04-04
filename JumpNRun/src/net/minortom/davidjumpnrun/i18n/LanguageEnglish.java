/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.i18n;

import jumpnrun.JumpNRun;

public class LanguageEnglish extends Language{

    public LanguageEnglish(JumpNRun newGame) {
        super(newGame);
        
        // Start variables
        
        // Multiple uses
        this.backBt = "Back";
        this.okBt = "Ok";
        
        // Language names
        this.langEN = "English";
        this.langDE = "German";
        
        // Global
        this.GWindowName = "Jump-N-Run";
        
        // JumpNRun.java
        this.JNRCfgDirCorrectPopTitle = "Storage location";
        this.JNRCfgDirCorrectPopText1 = "Configuration files will be stored at: \n";
        this.JNRCfgDirCorrectPopText2 = "\nIf this location is incorrect terminate the program now!\nIf the location already was set this message can pop up because of an update.";
        
        // Main Menu
        this.MainMenuPlayBt = "Play Offline";
        this.MainMenuNetworkBt = "Play Online";
        this.MainMenuExitBt = "Exit Game";
        this.MainMenuFontSizeLabel = "Font Size";
        this.MainMenuFontSizeMBt = "A-";
        this.MainMenuFontSizePBt = "A+";
        this.MainMenuLangLabel = "Language";
        this.MainMenuCreditsBt = "Credits";
        
        // Credits Screen
        this.CreditsHeader = "Credits";
        
        // Login Screen
        this.LoginScreenLoginLabel = "Username";
        this.LoginScreenPassLabel = "Password";
        this.LoginScreenServerLabel = "Server (Format: \"host.name:port\")";
        this.LoginScreenDoLoginBt = "Log in!";
        this.LoginScreenConnecting = "Connecting...";
        this.LoginScreenErrorInvalidCharTitle = "Invalid character entered";
        this.LoginScreenErrorInvalidCharText = "Invalid character entered into one or more of the input fields. Try removing all exclamation marks.";
        
        // Network Play Screen
        this.NetworManagerLoginBt = "Log in";
        this.NetworManagerJoinGameBt = "Join Game";
        this.NetworManagerCreateGameBt = "Create Game";
        this.NetworkManagerConnected = "Connected!";
        this.NetworkManagerInternalError = "Internal Error";
        this.NetworkManagerInvalidHost = "Invalid Host";
        this.NetworkManagerInvalidAuth = "Invalid Username or password";
        
        // Join Game Screen
        this.JoinGNameLabel = "Game Name";
        this.JoinGOkBt = "Go!";
        
        this.GSkinBt = "Choose a Skin";
        
        // Choose Game Mode Menu
        this.ChoGmEndlessBt = "Endless";
        this.ChoGmTimeBt = "Time limit: ";
        this.ChoGmTimeLbl = " Minutes";
        this.ChoGmDeathsBt = "Respawn limit: ";
        this.ChoGmDeathsLbl = " Respawns";
        this.ChoGmOkBt = "Start";
        this.ChoGmErrOnlyNumbers = "Numbers only!";
        this.ChoGmErrOnlyWholeNumbers = "Whole Numbers only!";
        
        // Create Game Screen
        this.CreateGChooseMapBt = "Choose a Map";
        this.CreateGNameLbl = "Public Name (Used for joining)";
        this.CreateGPlayersLbl = "Player Count:";
        this.CreateGPlayers2Lbl = " Players";
        
        // Map Choose Menu
        
        //Skin Choose Menu
        this.SkinColorBlue = "Blue";
        this.SkinColorGreen = "Green";
        this.SkinColorOrange = "Orange";
        this.SkinColorYellow = "Yellow";
        this.SkinColorRed = "Red";
        this.SkinChooseDefaultHeading = "Choose your skin!";
        this.SkinChooseOfflinePlayer1Heading = "Left Player: Choose your Skin!";
        this.SkinChooseOfflinePlayer2Heading = "Right Player: Choose your Skin!";
        
        // Wait Screen
        this.WaitServerAnswer = "Waiting for the Server";
        this.WaitOtherPlayersA = "Waiting for other players.\nOnly ";
        this.WaitOtherPlayersB = " players have joined yet:\n";
        this.WaitGameStart = "Waiting for the Game to start.";
        
        // Online Errors
        this.ErrorTitle = "Error";
        this.ErrorUnknown = "Unknown Error Code";
        this.ErrorFatalTitle = "Fatal Error";
        this.ErrorNameAlreadyExists = "The Name you have chosen is already in use.";
        this.ErrorNameDoesntExist = "The Game you want to join doesn't exist.";
        
        // Config Manager
        this.CfgManErrorStorageLocationTitle = "The configuration might not be loaded/saved correctly";
        this.CfgManErrorStorageLocationText = "The Operating System could not be identified.";
        this.CfgManErrorLoadTitle = "Config could not be loaded";
        this.CfgManErrorLoadText = ""
                + "This could be because the game has been updated. \n\n"
                + "Stack Trace: \n";
        this.CfgManErrorSaveTitle = "Config could not be saved";
        this.CfgManErrorSaveText = "Stack Trace: \n";
        
        // Gameplay
        this.playerNameLocalPlayer = "You";
    }
    
    @Override
    public String getShortName(){
        return "EN";
    }
}
