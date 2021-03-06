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
        
        
        // JumpNRun.java
        this.JNRCfgDirCorrectPopTitle = "Storage location";
        this.JNRCfgDirCorrectPopText1 = "Configuration files will be stored at: \n";
        this.JNRCfgDirCorrectPopText2 = "\nIf this location is incorrect terminate the program now!\nIf the location already was set this message can pop up because of an update.";
        
        // Graphic.java
        this.GraphicRespawnsLeft = " Respawns Left";
        this.GraphicKills = " Kills";
        
        // Main Menu
        this.MainMenuPlayBt = "Play Offline";
        this.MainMenuNetworkBt = "Play Online";
        this.MainMenuPlayLocalBt = "Play Local";
        this.MainMenuExitBt = "Exit Game";
        this.MainMenuFontSizeLabel = "Font Size";
        this.MainMenuFontSizeMBt = "A-";
        this.MainMenuFontSizePBt = "A+";
        this.MainMenuLangLabel = "Language";
        this.MainMenuCreditsBt = "Credits";
        this.MainMenuWorldEditorBt = "Open WorldEditor";
        this.MainMenuSettingsLbl = "Settings";
        
        // Credits Screen
        this.CreditsHeader = "Credits";
        this.CreditsCloseBt = "Close";
        
        // Login Screen
        this.LoginScreenServerLabel = "Server (Format: \"host.name:port\")";
        this.LoginScreenConnecting = "Connecting...";
        this.LoginScreenErrorInvalidCharTitle = "Invalid character entered";
        this.LoginScreenErrorInvalidCharText = "Invalid character entered into one or more of the input fields. Try removing all exclamation marks.";
        this.LoginScreenOpenInWebBt = "Open in Webbrowser";
        this.LoginScreenHeading = "Login";
        
        // Network Play Screen
        this.NetworManagerLoginBt = "Log in";
        this.NetworManagerLoginBtLoggedIn = "Change user";
        this.NetworManagerJoinGameBt = "Join Game";
        this.NetworManagerCreateGameBt = "Create Game";
        this.NetworManagerLeaderboardBt = "Leaderboard";
        this.NetworkManagerConnected = "Connected!";
        this.NetworkManagerInternalError = "Internal Error";
        this.NetworkManagerInvalidHost = "Invalid Host";
        this.NetworkManagerInvalidAuth = "Invalid Username or password";
        this.NetworkManagerNotAuthenthicatedBody = "The user could not be authenthicated. Please log in again.";
        
        // Join Game Screen
        this.JoinGNameLabel = "Game Name";
        this.JoinGOkBt = "Go!";
        this.GSkinBt = "Choose a Skin";
        this.JoinGPlayerNameLabel = "Playing as:";
        this.JoinGameServerName = "Serveradress:";
        
        // Choose Game Mode Menu
        this.ChoGmEndlessBt = "Endless";
        this.ChoGmTimeBt = "Time limit: ";
        this.ChoGmTimeLbl = " Minutes";
        this.ChoGmDeathsBt = "Respawn limit: ";
        this.ChoGmDeathsLbl = " Respawns";
        this.ChoGmOkBt = "Start";
        this.ChoGmErrOnlyNumbers = "Numbers only!";
        this.ChoGmErrOnlyWholeNumbers = "Whole Numbers only!";
        this.ChoGmCMBt = "Select map";
        this.ChoGmDMBt = "Default map";
        
        // Create Game Screen
        this.CreateGChooseMapBt = "Choose a Map";
        this.CreateGNameLbl = "Public Name (Used for joining)";
        this.CreateGPlayersLbl = "Player Count:";
        this.CreateGPlayers2Lbl = " Players";
        
        // Map Choose Menu
        
        // Play Online Screen
        this.plOnlMnPlayWithoutLogin = "Play without login";
        this.plOnlMnPlayAs = "Play as: ";
        
        //Skin Choose Menu
        this.SkinColorBlue = "Blue";
        this.SkinColorGreen = "Green";
        this.SkinColorOrange = "Orange";
        this.SkinColorYellow = "Yellow";
        this.SkinColorRed = "Red";
        this.SkinRobot = "Robot";
        this.SkinChooseDefaultHeading = "Choose your skin!";
        this.SkinChooseOfflinePlayer1Heading = "Left Player: Choose your Skin!";
        this.SkinChooseOfflinePlayer2Heading = "Right Player: Choose your Skin!";
        
        // Wait Screen
        this.WaitServerAnswer = "Waiting for the Server";
        this.WaitOtherPlayersA = "Waiting for other players.\nOnly ";
        this.WaitOtherPlayersB = " players have joined yet:";
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
        this.killsLabelText = "kills: ";
        this.deathLabelText = "deaths:";
        this.respawnLabelText = "respawns remaining: ";
        
        // IO
        this.IOReadErrorTitle = "Read Error";
        this.IOReadErrorTextA = "File at ";
        this.IOReadErrorTextB = " not found. This could be because the program was incorrectly installed. Expected files at: ";
    
        // World Editor
        this.WorldEditTitle = "Hello world!";
        this.WorldEditMainMenu = "Main Menu";
        this.WorldEditFile = "File";
        this.WorldEditSave = "Save";
        this.WorldEditSaveAt = "Save at";
        this.WorldEditOpen = "Open";
        this.WorldEditAddBlock = "Add a block";
        this.WorldEditMainMenuOpen = "Open";
        this.WorldEditErrOnlyNumbers = "Only Numbers";
        
        // Online EndGame
        this.EndGameDeaths = "Deaths";
        this.EndGameKills = "Kills";
        this.EndGameName = "Name";
        this.EndGamePlacement = "Placement";
        
        //Local game screen
        this.LocalEnterNameErr = "Please enter your nickname!";
        this.LocalEnterNameInfo = "Your nickname...";
        this.LocalEnterNameLabel = "Your nickname:";
    }
    
    @Override
    public String getShortName(){
        return "EN";
    }
}
