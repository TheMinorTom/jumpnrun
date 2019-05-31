/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.i18n;

import jumpnrun.JumpNRun;

public class LanguageGerman extends Language{

    public LanguageGerman(JumpNRun newGame) {
        super(newGame);
        
        // Start variables
        
        // Multiple uses
        this.backBt = "Zurück";
        this.okBt = "Ok";
        
        // Language names
        this.langEN = "Englisch";
        this.langDE = "Deutsch";
        
        // Global
        
        // JumpNRun.java
        this.JNRCfgDirCorrectPopTitle = "Speicherort";
        this.JNRCfgDirCorrectPopText1 = "Konfigurationsdatei wird gespeichert unter: \n";
        this.JNRCfgDirCorrectPopText2 = "\nFalls dieser Ort falsch ist, schließe das Programm sofort!\nWenn der Ort schon festgelegt wurde, kann diese Nachricht wegen eines Updates auftreten.";
        
        // Graphic.java
        this.GraphicRespawnsLeft = " Leben übrig";
        this.GraphicKills = " Kills";
        
        // Main Menu
        this.MainMenuPlayBt = "Lokal spielen";
        this.MainMenuNetworkBt = "Online spielen";
        this.MainMenuExitBt = "Spiel verlassen";
        this.MainMenuFontSizeLabel = "Schriftgröße";
        this.MainMenuFontSizeMBt = "A-";
        this.MainMenuFontSizePBt = "A+";
        this.MainMenuLangLabel = "Sprache";
        this.MainMenuCreditsBt = "Credits";
        this.MainMenuWorldEditorBt = "WorldEditor Öffnen";
        this.MainMenuSettingsLbl = "Einstellungen";
        
        // Credits Screen
        this.CreditsHeader = "Credits";
        this.CreditsCloseBt = "Zurück";
        
        // Login Screen
        this.LoginScreenServerLabel = "Server (Format: \"host.name:port\")";
        this.LoginScreenConnecting = "Verbinden...";
        this.LoginScreenErrorInvalidCharTitle = "Falsche Ziffer eingegeben";
        this.LoginScreenErrorInvalidCharText = "Falsche Ziffer in eine oder mehr der Felder eingegeben. Versuche, alle Ausrufezeichen zu löschen.";
        
        // Network Play Screen
        this.NetworManagerLoginBt = "Log in";
        this.NetworManagerLoginBtLoggedIn = "Nutzer wechseln";
        this.NetworManagerJoinGameBt = "Einem Spiel beitreten";
        this.NetworManagerCreateGameBt = "Spiel ertellen";
        this.NetworkManagerConnected = "Verbunden!";
        this.NetworkManagerInternalError = "Interner Fehler";
        this.NetworkManagerInvalidHost = "Ungültiger Host";
        this.NetworkManagerInvalidAuth = "Ungültiger Benutzername oder Passwort";
        this.NetworkManagerNotAuthenthicatedBody = "Bei der Anmeldung ist ein Fehler aufgetreten, der Nutzer konnte nicht eingeloggt werden. Bitte erneut versuchen.";
        
        // Join Game Screen
        this.JoinGNameLabel = "Name des Spiels";
        this.JoinGOkBt = "Los!";
        
        this.GSkinBt = "Wähle einen Skin!";
        
        // Choose Game Mode Menu
        this.ChoGmEndlessBt = "Unendlich";
        this.ChoGmTimeBt = "Auf Zeit: ";
        this.ChoGmTimeLbl = " Minuten";
        this.ChoGmDeathsBt = "Nach Respawns: ";
        this.ChoGmDeathsLbl = " Leben";
        this.ChoGmOkBt = "Start";
        this.ChoGmErrOnlyNumbers = "Bitte nur Zahlen!";
        this.ChoGmErrOnlyWholeNumbers = "Bitte nur ganze Zahlen!";
        this.ChoGmCMBt = "Karte wählen";
        this.ChoGmDMBt = "Standard";
        
        // Create Game Screen
        this.CreateGChooseMapBt = "Wähle eine Karte";
        this.CreateGNameLbl = "Öffentlicher Name (Für das Beitreten)";
        this.CreateGPlayersLbl = "Spieleranzahl:";
        this.CreateGPlayers2Lbl = " Spieler";
        
        // Map Choose Menu
        
        //Skin Choose Menu
        this.SkinColorBlue = "Blau";
        this.SkinColorGreen = "Grün";
        this.SkinColorOrange = "Orange";
        this.SkinColorYellow = "Gelb";
        this.SkinColorRed = "Rot";
        this.SkinChooseDefaultHeading = "Wähle deinen Skin!!";
        this.SkinChooseOfflinePlayer1Heading = "Spieler 1: Wähle deinen Skin!!";
        this.SkinChooseOfflinePlayer2Heading = "Spieler 2: Wähle deinen Skin!";
        
        // Wait Screen
        this.WaitServerAnswer = "Auf den Server warten...";
        this.WaitOtherPlayersA = "Auf andere Spieler warten.\nOnly ";
        this.WaitOtherPlayersB = " Bereits beigetreten";
        this.WaitGameStart = "Bitte auf den Start des Spiels warten.";
        
        // Online Errors
        this.ErrorTitle = "Fehler";
        this.ErrorUnknown = "Unbekannter Fehler-Code";
        this.ErrorFatalTitle = "Schwerwiegender Fehler";
        this.ErrorNameAlreadyExists = "Dieser Name ist bereits vergeben";
        this.ErrorNameDoesntExist = "Dieses Siel existiert nicht.";
        
        // Config Manager
        this.CfgManErrorStorageLocationTitle = "Die Konfiguration könnte nicht richtig gespeichert/geladen werden.";
        this.CfgManErrorStorageLocationText = "Das Betriebssystem konnte nicht identifiziert werden..";
        this.CfgManErrorLoadTitle = "Konfiguration konnte nicht geladen werden.";
        this.CfgManErrorLoadText = ""
                + "Dies könnte sein, da das SPiel geupdated wurde. \n\n"
                + "Stack Trace: \n";
        this.CfgManErrorSaveTitle = "Konfiguration konnte nicht gespeichert werden";
        this.CfgManErrorSaveText = "Stack Trace: \n";
        
        // Gameplay
        this.playerNameLocalPlayer = "Du";
        this.killsLabelText = "Kills: ";
        this.deathLabelText = "Tode:";
        this.respawnLabelText = "Respawns übrig: ";
        
        // IO
        this.IOReadErrorTitle = "Lesefehler";
        this.IOReadErrorTextA = "Datei unter ";
        this.IOReadErrorTextB = " wurde nicht gefunden. Dies könnte sein, da das Programm nicht korrekt installiert wurde. Erwartete Dateien unter: : ";
    
        // World Editor
        this.WorldEditTitle = "WorldEditor!";
        this.WorldEditMainMenu = "Hauptmenü";
        this.WorldEditFile = "Datei";
        this.WorldEditSave = "Speichern";
        this.WorldEditSaveAt = "SPeichern unter";
        this.WorldEditOpen = "Öffnen";
        this.WorldEditAddBlock = "Block hinzufügen";
        this.WorldEditMainMenuOpen = "Öffnen";
        this.WorldEditErrOnlyNumbers = "Nur Zahlen bitte";
    }
    
    @Override
    public String getShortName(){
        return "DE";
    }
}
