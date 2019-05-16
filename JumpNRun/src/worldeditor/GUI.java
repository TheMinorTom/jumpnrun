/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldeditor;

import java.io.File;
import java.util.Vector;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.BoundingBox;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jumpnrun.JumpNRun;

/**
 *
 * @author User
 */
public class GUI extends Application {

    //private static ObservableList<ObservableList<Block>> worldList;
    private static Vector<Vector<Block>> worldVector;
    private static Group world;

    private static double blockSize;
    private static Rectangle r;
    private static MenuBar menuBar;
    private static Menu file;
    private static Menu mainMenu;
    private static MenuItem mainMenuOpen;
    private static MenuItem save, saveAt, open, addBlock;
    private static FileChooser fileChooser;
    private static File saveFile;
    private static TextField blockSizeTextField;
    private static BlockChose blockChose;
    private static Block selectedBlock;
    private static boolean dragDoing;
    private static Vector<int[]> dragBlockIndEntered;
    private static Label rx, ry;

    public static Scene scene;
    public static JumpNRun jumpnrun;
    
    @Override
    public void start(Stage primaryStage) {

        blockSize = 60;
        dragBlockIndEntered = new Vector<int[]>();
        dragDoing = false;

        rx = new Label();
        rx.setLayoutX(100);
        ry = new Label();
        ry.setLayoutX(200);

        if((jumpnrun==null)) saveFile = new File("D:\\David\\Gespeichert.txt");
        if(!(jumpnrun==null)) saveFile = new File(JumpNRun.sourcePath + "worlds/");
        world = new Group();
        blockChose = new BlockChose();
        worldVector = new Vector<Vector<Block>>();

        mainMenu = new Menu("Hauptmenü");
        if(!(jumpnrun==null)) mainMenu.setText(jumpnrun.language.WorldEditMainMenu);
        
        file = new Menu("Datei");
        if(!(jumpnrun==null)) file.setText(jumpnrun.language.WorldEditFile);

        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(saveFile);

        selectedBlock = WorldEditor.blocks[0];

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(file);
        if(!(jumpnrun==null)) {
            menuBar.getMenus().addAll(mainMenu);
        }

        save = new MenuItem("Speichern");
        if(!(jumpnrun==null)) save.setText(jumpnrun.language.WorldEditSave);
        
        save.setOnAction((ActionEvent e) -> {
            IO.saveWorld(worldVector, saveFile, blockSize);
        });

        saveAt = new MenuItem("Speichern unter");
        if(!(jumpnrun==null)) saveAt.setText(jumpnrun.language.WorldEditSaveAt);

        saveAt.setOnAction((ActionEvent e) -> {
            File newSaveFile = fileChooser.showSaveDialog(primaryStage);
            if (newSaveFile != null) {
                saveFile = newSaveFile;
                IO.saveWorld(worldVector, saveFile, blockSize);
            }

        });

        open = new MenuItem("Öffnen");
        if(!(jumpnrun==null)) open.setText(jumpnrun.language.WorldEditOpen);

        open.setOnAction(((event) -> {
            File openFile = fileChooser.showOpenDialog(primaryStage);
            if (openFile != null) {
                worldVector = IO.openWorld(openFile);
                drawWorld();
            }
        }));

        addBlock = new MenuItem("Neuen Block hinzufügen");
        if(!(jumpnrun==null)) addBlock.setText(jumpnrun.language.WorldEditAddBlock);

        addBlock.setOnAction((ActionEvent e) -> {

        });
        
        mainMenuOpen = new MenuItem("Öffnen");
        if(!(jumpnrun==null)) mainMenuOpen.setText(jumpnrun.language.WorldEditMainMenuOpen);
        
        if(!(jumpnrun==null)) {
            mainMenu.getItems().addAll(mainMenuOpen);
            
            mainMenuOpen.setOnAction(((event) -> {
                jumpnrun.openMainMenu();
            }));
        }

        file.getItems().addAll(save, saveAt, open);
        
        menuBar.setVisible(true);

        r = new Rectangle();
        r.setWidth(blockSize);
        r.setHeight(blockSize);
        r.setFill(Color.TRANSPARENT);
        r.setStroke(Color.RED);

        blockSizeTextField = new TextField(Double.toString(blockSize));
        blockSizeTextField.setLayoutX(0);
        blockSizeTextField.setLayoutY(menuBar.getHeight());

        blockSizeTextField.setOnAction((ActionEvent) -> {
            try {
                blockSize = Double.parseDouble(blockSizeTextField.getText());
                drawWorld();
                r.setWidth(blockSize);
                r.setHeight(blockSize);
            } catch (NumberFormatException e) {
                blockSizeTextField.setText("Bitte nur Zahlen!");
                if(!(jumpnrun==null)) blockSizeTextField.setText(jumpnrun.language.WorldEditErrOnlyNumbers);
            }
        });

        world.getChildren().addAll(r);
        ScrollPane scroll = new ScrollPane(world);
        scroll.setLayoutY(menuBar.getHeight() + blockSizeTextField.getHeight());
        scroll.setViewportBounds(new BoundingBox(0, 0, 100, 100));
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        Group ultraRoot = new Group(menuBar, blockSizeTextField, scroll);
        Scene scene = new Scene(ultraRoot);

        primaryStage.setTitle("Hello World!");
        if(!(jumpnrun==null)) primaryStage.setTitle(jumpnrun.language.WorldEditTitle);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);

        //primaryStage.show();

        scene.setCursor(Cursor.HAND);


        scroll.setOnMouseMoved((MouseEvent e) -> {
            updateRect(e.getX(), e.getY());

        });

        scene.setOnMouseClicked((MouseEvent e) -> {
            switch (e.getButton()) {
                case SECONDARY:
                    if (e.getSceneY() > (menuBar.getHeight() + blockSizeTextField.getHeight())) {
                        blockChose.show(r, e.getX(), e.getY() + menuBar.getHeight() + blockSizeTextField.getHeight());
                    }
                    break;
                case PRIMARY:
                    addBlock(Block.createBlock(selectedBlock, r.getX(), r.getY()), r.getX(), r.getY());
                    break;
            }

        });

        scroll.setOnMouseDragged((MouseEvent e) -> {
            dragDoing = true;
            updateRect(e.getX(), e.getY());

            int[] currBlockInd = new int[]{(int) (r.getX() / blockSize), (int) (r.getY() / blockSize)};
            boolean exists = false;
            for (int i = 0; i < dragBlockIndEntered.size(); i++) {
                if (currBlockInd[0] == dragBlockIndEntered.get(i)[0] && currBlockInd[1] == dragBlockIndEntered.get(i)[1]) {
                    exists = true;
                }
            }
            if (!exists) {
                dragBlockIndEntered.add(currBlockInd);
                addBlock(Block.createBlock(selectedBlock), r.getX(), r.getY());
                rx.setText(Double.toString(r.getX()));
                ry.setText(Double.toString(r.getY()));
            }

        });

        scroll.setOnMouseReleased(
                (MouseEvent e) -> {
                    if (dragDoing) {
                        dragDoing = false;
                        dragBlockIndEntered.clear();
                    }
                });
        scroll.requestFocus();
    }
    
    public GUI(JumpNRun game) {
        this.jumpnrun = game;
        //start(new Stage());
    }

    public static int test(int i) {
        i++;
        i++;
        return i;
    }

    public static void addBlock(Block b, double xPos, double yPos) {

        int xIndex = (int) (xPos / blockSize);
        int yIndex = (int) (yPos / blockSize);

        b.setX(xPos);
        b.setY(yPos);

        for (; xIndex >= worldVector.size();) {
            worldVector.add(new Vector<Block>());
        }

        for (; yIndex >= worldVector.get(xIndex).size();) {
            worldVector.get(xIndex).add(Block.createBlock(WorldEditor.blocks[0]));

        }
        Vector<Block> yList = worldVector.get(xIndex);

        for (int i = 0; i < world.getChildren().size(); i++) {
            Node testNode = world.getChildren().get(i);
            if (testNode.getClass().equals(Block.class)) {
                Block testBlock = (Block) world.getChildren().get(i);
                if ((testBlock.getX() == b.getX() && (testBlock.getY() == b.getY()))) {
                    world.getChildren().remove(i);
                    i = world.getChildren().size();
                }
            }
        }
        world.getChildren().add(b);

        yList.set(yIndex, b);

    }

    public static void drawWorld() {
        //world = new Group();
        world.getChildren().clear();
        world.getChildren().addAll(r, blockSizeTextField, menuBar);
        for (int i = 0; i < worldVector.size(); i++) {
            for (int j = 0; j < worldVector.get(i).size(); j++) {
                if (worldVector.get(i).get(j) != null) {
                    Block b = Block.createBlock(worldVector.get(i).get(j));
                    b.setLayoutX(i * blockSize);
                    b.setLayoutY(j * blockSize);

                    world.getChildren().add(b);

                }
            }
        }
    }

    public static Group drawWorld(Vector<Vector<Block>> worldVec, double blockSize) {
        Group returnWorld = new Group();
        for (int i = 0; i < worldVec.size(); i++) {
            for (int j = 0; j < worldVec.get(i).size(); j++) {
                if (worldVec.get(i).get(j) != null) {
                    Block b = worldVec.get(i).get(j);
                    b.setX(i * blockSize);
                    b.setY(j * blockSize);
                    returnWorld.getChildren().add(b);

                }
            }
        }
        return returnWorld;
    }

    private void updateRect(double xPos, double yPos) {
        if ((yPos > (menuBar.getHeight() + blockSizeTextField.getHeight())) && !blockChose.isShowing()) {
            r.setX(blockSize * (Math.floor(xPos / blockSize)));
            r.setY(blockSize * (Math.floor(yPos / blockSize)));

        }
    }

    class SlideListener implements ChangeListener {

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            blockSize = (double) observable.getValue();
            r.setWidth(blockSize);
            r.setHeight(blockSize);
            drawWorld();
        }
    }

    public static double getBlockSize() {
        return blockSize;
    }

    public static void setSelectedBlock(Block b) {
        selectedBlock = b;
    }

    /*
     private Block [][] addBlock (ObservableList<ObservableList<Block>> w, Block block, int x, int y)
     {
     if(x > w.size())
     {
           
     }
     }
     */
    /**
     * @param args the command line arguments
     */
}
