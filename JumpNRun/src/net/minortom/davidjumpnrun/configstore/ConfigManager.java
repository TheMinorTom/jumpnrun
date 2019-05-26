/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.configstore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import jumpnrun.JumpNRun;
import net.minortom.davidjumpnrun.server.OperationFailedException;
import net.minortom.davidjumpnrun.server.Server;

public class ConfigManager {

    public static JumpNRun game;

    public static boolean osiderrorshown = false;

    public static final String configFolderName = "tompkg/3883801c-d419-416d-b3e8-0c881044693d";
    public static final String configFileName = "config.ser";

    public static Configuration loadConfiguration() {
        Configuration config = null;
        try {
            FileInputStream fileIn = new FileInputStream(getAppdataLocation() + configFileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            config = (Configuration) in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception i) {
            if (!i.getMessage().contains("o such file")) {
                i.printStackTrace();
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                i.printStackTrace(pw);
                String sStackTrace = sw.toString();
                //error(game.language.CfgManErrorLoadTitle, ""
                //        + game.language.CfgManErrorLoadText + sStackTrace);
            }
        }
        return config;
    }

    public static void saveConfiguration(Configuration config) {
        try {
            new File(getAppdataLocation()).mkdirs();
            FileOutputStream fileOut = new FileOutputStream(getAppdataLocation() + configFileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(config);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            i.printStackTrace(pw);
            String sStackTrace = sw.toString();
            error(game.language.CfgManErrorSaveTitle, game.language.CfgManErrorSaveText + sStackTrace);
        }
    }

    public static void alert(AlertType type, String title, String body, boolean wait) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(type);
                alert.setTitle(game.language.GWindowName);
                alert.setHeaderText(title);
                alert.setContentText(body);
                alert.setResizable(true);
                alert.getDialogPane().setPrefSize(480, 320);
                if (wait) {
                    alert.showAndWait();
                } else {
                    alert.show();
                }
            }
        });

    }

    public static void alert(AlertType type, String title, String body) {
        alert(type, title, body, true);
    }

    public static void info(String title, String body) {
        alert(AlertType.INFORMATION, title, body);
    }

    public static void error(String title, String body) {
        alert(AlertType.ERROR, title, body);
    }

    public static String getStorageLocation() {
        if (game == null || game.config == null || game.config.customPath.isEmpty()) {
            return getAppdataLocation();
        } else {
            return game.config.customPath;
        }
    }

    public static String getAppdataLocation() {
        String OS = (System.getProperty("os.name")).toUpperCase();
        if (OS.contains("WIN")) {
            return System.getenv("AppData") + "/" + configFolderName + "/";
        } else if (OS.contains("LIN")) {
            return System.getenv("HOME") + "/.local/share/" + configFolderName + "/";
        } else if (OS.contains("MAC")) {
            return System.getProperty("user.home") + "/Library/Application Support/" + configFolderName + "/";
        } else {
            if (!osiderrorshown) {
                error(game.language.CfgManErrorStorageLocationTitle, game.language.CfgManErrorStorageLocationText);
                osiderrorshown = true;
            }
            return "";
        }
    }

    public static InputStream getFileStream(String spezUri) {
        if ((game == null)&&(!(Server.server==null))) {
            try {
                InputStream in = new FileInputStream(spezUri);
                return in;
            } catch (FileNotFoundException e) {
                System.err.println("Invalid Storage Location");
               throw new OperationFailedException();
            }
        } else if ((!(game == null))&&((Server.server==null))) {
            String appdataUri = game.config.customPath;
            if (appdataUri.isEmpty()) {
                appdataUri = JumpNRun.sourcePath;
            }
            try {
                InputStream in = new FileInputStream(appdataUri + spezUri);
                return in;
            } catch (FileNotFoundException e) {
                System.err.println("File at " + appdataUri + spezUri + " not found.");
                error("World not Found!", "File at " + appdataUri + spezUri + " not found.");
                DirectoryChooser dirChooser = new DirectoryChooser();
                dirChooser.setTitle("Select a file source path, the default one didn't work");
                File file = dirChooser.showDialog(game.getPrimStage());
                game.config.customPath = file.getPath() + "/";
                saveConfiguration(game.config);
                InputStream in;
                try {
                    in = new FileInputStream(game.config.customPath + spezUri);
                } catch (FileNotFoundException ex) {
                    return null;
                }
                
                return in;
            }
        } else {
            throw new UnsupportedOperationException("Invalid configuration");
        }
    }

    public static InputStream getFileStreamAbsPath(String uri) {
        try {
            InputStream in = new FileInputStream(uri);
            return in;
        } catch (FileNotFoundException e) {
            return null; 
        }
    }
}
