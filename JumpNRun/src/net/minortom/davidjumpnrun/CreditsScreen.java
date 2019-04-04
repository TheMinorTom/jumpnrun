/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import jumpnrun.JumpNRun;
import static jumpnrun.JumpNRun.sourcePath;

public class CreditsScreen extends VBox {
    
    JumpNRun game;
    
    Label headerLabel;
    WebView textView;
    WebEngine webEngine;
    
    Button backBt, closeBt;
    
    boolean loadedother;
    
    public CreditsScreen (JumpNRun game) {
        this.game = game;
        
        backBt = new Button("ERR");
        backBt.setOnAction((ActionEvent e) -> {
            updateStrings();
        });
        
        closeBt = new Button("ERR");
        closeBt.setOnAction((ActionEvent e) -> {
            game.openMainMenu();
        });
        
        textView = new WebView();
        webEngine = textView.getEngine();
        
        headerLabel = new Label("Please Wait");
        
        updateStrings();
        
        setAlignment(Pos.CENTER);
        setSpacing(50);
        setPadding(new Insets(0, 0, 0, 0));
        getChildren().addAll(headerLabel, textView, backBt, closeBt);
    }
    
    public void updateStrings(){
        backBt.setFont(game.language.getFont());
        backBt.setText(game.language.backBt);
        closeBt.setFont(game.language.getFont());
        closeBt.setText(game.language.CreditsCloseBt);
        headerLabel.setFont(game.language.getHeadingFont());
        headerLabel.setText(game.language.CreditsHeader);
        
        loadedother = true;
        try {
            webEngine.load(new File(sourcePath + "sprites/webpages/credits-" + game.language.getShortName() + ".html").toURI().toURL().toString());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

}
