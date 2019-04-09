/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package worldeditor;

import java.io.File;
import java.util.Vector;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import static worldeditor.WorldEditor.initBlocksArr;

public class InGameSceneWorldEditor extends VBox {
    
    //private static ObservableList<ObservableList<Block>> worldList;
    private static Vector<Vector<Block>> worldVector;
    private static Group world;

    private static double blockSize;
    private static Rectangle r;
    private static MenuBar menuBar;
    private static Menu file;
    private static MenuItem save, saveAt, open, addBlock;
    private static FileChooser fileChooser;
    private static File saveFile;
    private static TextField blockSizeTextField;
    private static BlockChose blockChose;
    private static Block selectedBlock;
    private static boolean dragDoing;
    private static Vector<int[]> dragBlockIndEntered;
    private static Label rx, ry;
    
    private static Scene editScene;
    
    public InGameSceneWorldEditor(){
        initBlocksArr();
    }
}
