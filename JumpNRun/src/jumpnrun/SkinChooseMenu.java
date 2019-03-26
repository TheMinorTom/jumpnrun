/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import java.io.File;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author DavidPrivat
 */
public class SkinChooseMenu extends VBox {

    private HBox hBox;
    private Label headingLbl;
    private ChoiceBox leftCB, rightCB;
    private String[] paths;
    private File protDir;
    private File[] imageFiles;

    public SkinChooseMenu(JumpNRun game) {
        protDir = new File("./src/sprites/protagonist");
        imageFiles = protDir.listFiles();
        paths = new String[imageFiles.length];

        leftCB = new ChoiceBox();
        rightCB = new ChoiceBox();
        
        
        for (int i = 0; i < imageFiles.length; i++) {
            paths[i] = imageFiles[i].getPath();
            
            //leftCB.getItems().add(new Ch)
        }
        boolean temp = protDir.exists();

    }
}
