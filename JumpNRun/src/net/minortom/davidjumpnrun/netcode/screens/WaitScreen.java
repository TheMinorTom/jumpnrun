/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.netcode.screens;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import jumpnrun.JumpNRun;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import worldeditor.IO;

public class WaitScreen extends VBox {
    
    JumpNRun game;
    
    Label textLabel;
    Image nothingImage, waitImage, loadImage;
    ImageView imageView;
    
    public WaitScreen (JumpNRun game) {
        this.game = game;
        
        textLabel = new Label("Please Wait");
        nothingImage = new Image(ConfigManager.getFileStream("sprites/images/nothing.png"));
        waitImage = new Image(ConfigManager.getFileStream("sprites/images/waiting.gif"));
        loadImage = new Image(ConfigManager.getFileStream("sprites/images/loading.gif"));
        setImage(WaitAnimation.NONE);
        
        updateStrings();
        
        setAlignment(Pos.CENTER);
        setSpacing(50);
        setPadding(new Insets(0, 0, 0, 0));
        getChildren().addAll(textLabel, imageView);
    }
    
    public void setImage(WaitAnimation anim){
        if(null == anim){
            imageView.setImage(nothingImage);
        } else switch (anim) {
            case LOADING:
                imageView = new ImageView(loadImage);
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(game.language.getFontSize()*3);
                break;
            case WAITING:
                imageView = new ImageView(waitImage);
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(game.language.getFontSize()*5);
                break;
            default:
                imageView = new ImageView(nothingImage);
                break;
        }
    }
    
    public void updateStrings(){
        textLabel.setFont(game.language.getFont());
    }

    public void setText(String text) {
        setText(text, WaitAnimation.NONE);
    }
    
    public void setText(String text, WaitAnimation waitanim) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textLabel.setText(text);
                setImage(waitanim);
            }
        });
    }
    
    public enum WaitAnimation {
        LOADING,
        WAITING,
        NONE;
    }
}