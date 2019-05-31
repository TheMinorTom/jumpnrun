/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldeditor;

import java.io.File;
import java.util.Vector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jumpnrun.JumpNRun;

/**
 *
 * @author User
 */
public class GUI extends Group {

    //private static ObservableList<ObservableList<Block>> worldList;
    private static Vector<Vector<Block>> worldVector;

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
    public static JumpNRun game;

    private double xScroll, yScroll;

    private Line topLine, leftLine;

    public GUI(JumpNRun game) {

        this.game = game;
        blockSize = 60;
        dragBlockIndEntered = new Vector<int[]>();
        dragDoing = false;

        rx = new Label();
        rx.setLayoutX(100);
        ry = new Label();
        ry.setLayoutX(200);

        saveFile = new File(JumpNRun.sourcePath + "worlds/");
        blockChose = new BlockChose();
        worldVector = new Vector<Vector<Block>>();

        mainMenu = new Menu("Hauptmenü");

        mainMenu.setText(game.language.WorldEditMainMenu);

        file = new Menu("Datei");
        file.setText(game.language.WorldEditFile);

        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(saveFile);

        selectedBlock = WorldEditor.blocks[0];

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(file);

        menuBar.getMenus().addAll(mainMenu);

        save = new MenuItem("Speichern");

        save.setText(game.language.WorldEditSave);

        save.setOnAction((ActionEvent e) -> {
            IO.saveWorld(worldVector, saveFile, blockSize);
        });

        saveAt = new MenuItem("Speichern unter");
        saveAt.setText(game.language.WorldEditSaveAt);

        saveAt.setOnAction((ActionEvent e) -> {
            File newSaveFile = fileChooser.showSaveDialog(game.getPrimStage());
            if (newSaveFile != null) {
                saveFile = newSaveFile;
                IO.saveWorld(worldVector, saveFile, blockSize);
            }

        });

        open = new MenuItem("Öffnen");
        open.setText(game.language.WorldEditOpen);

        open.setOnAction(((event) -> {
            File openFile = fileChooser.showOpenDialog(game.getPrimStage());
            saveFile = openFile;
            if (openFile != null) {
                worldVector = IO.openWorld(openFile);
                drawWorld();
            }
        }));

        addBlock = new MenuItem("Neuen Block hinzufügen");
        addBlock.setText(game.language.WorldEditAddBlock);

        addBlock.setOnAction((ActionEvent e) -> {

        });

        mainMenuOpen = new MenuItem("Öffnen");
        mainMenuOpen.setText(game.language.WorldEditMainMenuOpen);

        mainMenu.getItems().addAll(mainMenuOpen);

        mainMenuOpen.setOnAction(((event) -> {
            game.openMainMenu();
        }));

        file.getItems().addAll(save, saveAt, open);

        menuBar.setVisible(true);

        r = new Rectangle();
        r.setWidth(blockSize);
        r.setHeight(blockSize);
        r.setFill(Color.TRANSPARENT);
        r.setStroke(Color.RED);

        blockSizeTextField = new TextField(Double.toString(blockSize));

        blockSizeTextField.setOnAction((ActionEvent) -> {
            try {
                blockSize = Double.parseDouble(blockSizeTextField.getText());
                drawWorld();
                r.setWidth(blockSize);
                r.setHeight(blockSize);
            } catch (NumberFormatException e) {
                blockSizeTextField.setText("Bitte nur Zahlen!");

                blockSizeTextField.setText(game.language.WorldEditErrOnlyNumbers);

            }
            requestFocus();
        });

        topLine = new Line();
        leftLine = new Line();

        getChildren().addAll(leftLine, menuBar, blockSizeTextField, r, topLine);

        game.getPrimStage().setTitle("Hello World!");

        blockSizeTextField.setLayoutY(menuBar.getHeight());

        updateLines();
        topLine.setStrokeWidth(10);
        leftLine.setStrokeWidth(10);
    }

    public void setUpHandlers(Scene scene) {

        scene.setOnMouseMoved((MouseEvent e) -> {
            updateRect(e.getX(), e.getY());

        });
        scene.setOnMouseClicked((MouseEvent e) -> {
            switch (e.getButton()) {
                case SECONDARY:
                    if ((e.getSceneY() > (menuBar.getHeight() + blockSizeTextField.getHeight())) && e.getSceneY() > yScroll) {
                        blockChose.show(r, e.getSceneX(), e.getSceneY());
                    }
                    break;
                case PRIMARY:
                    if (e.getSceneY() > yScroll) {
                        {
                            addBlock(Block.createBlock(selectedBlock, r.getX(), r.getY()), r.getX(), r.getY());
                        }
                        break;
                    }
            }

        });

        scene.setOnMouseDragged((MouseEvent e) -> {
            if (e.getButton().equals(MouseButton.PRIMARY) && (e.getSceneY() > yScroll)) {
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
            }

        });

        scene.setOnMouseReleased(
                (MouseEvent e) -> {
                    if (e.getButton().equals(MouseButton.PRIMARY)) {
                        if (dragDoing) {
                            dragDoing = false;
                            dragBlockIndEntered.clear();
                        }
                    }
                });

        setOnKeyPressed((KeyEvent e) -> {
            switch (e.getCode()) {
                case UP:
                    yScroll += blockSize;
                    updateBlockPositions();
                    updateLines();
                    break;
                case DOWN:
                    yScroll -= blockSize;
                    updateBlockPositions();
                    updateLines();
                    break;
                case RIGHT:
                    xScroll -= blockSize;
                    updateBlockPositions();
                    updateLines();
                    break;
                case LEFT:
                    xScroll += blockSize;
                    updateBlockPositions();
                    updateLines();
                    break;
            }
        });
        requestFocus();

    }

    public void updateLines() {

        topLine.setStartX(0);
        topLine.setStartY(yScroll);
        topLine.setEndX(game.getWidth());
        topLine.setEndY(yScroll);

        leftLine.setStartX(xScroll);
        leftLine.setStartY(0);
        leftLine.setEndX(xScroll);
        leftLine.setEndY(game.getHeight());

    }

    public static int test(int i) {
        i++;
        i++;
        return i;
    }

    public void addBlock(Block b, double xPos, double yPos) {

        int xIndex = (int) ((xPos - xScroll) / blockSize);
        int yIndex = (int) ((yPos - yScroll) / blockSize);

        b.setX(xPos);
        b.setY(yPos);
        b.setFitWidth(blockSize);
        b.setFitHeight(blockSize);

        for (; xIndex >= worldVector.size();) {
            worldVector.add(new Vector<Block>());
        }

        for (; yIndex >= worldVector.get(xIndex).size();) {
            worldVector.get(xIndex).add(Block.createBlock(WorldEditor.blocks[0]));

        }
        Vector<Block> yList = worldVector.get(xIndex);

        for (int i = 0; i < getChildren().size(); i++) {
            Node testNode = getChildren().get(i);
            if (testNode.getClass().equals(Block.class)) {
                Block testBlock = (Block) getChildren().get(i);
                if ((testBlock.getX() == b.getX() && (testBlock.getY() == b.getY()))) {
                    getChildren().remove(i);
                    i = getChildren().size();
                }
            }
        }
        getChildren().add(b);
        yList.set(yIndex, null);
        yList.set(yIndex, b);

    }

    public void updateBlockPositions() {
        Block currBlock;
        for (int i = 0; i < worldVector.size(); i++) {
            for (int j = 0; j < worldVector.get(i).size(); j++) {
                currBlock = worldVector.get(i).get(j);
                
                currBlock.setX((i * blockSize) + xScroll);
                currBlock.setY((j * blockSize) + yScroll);
                
                
                
            }
        }
        
    }

    public void refreshPositions() {
        blockSizeTextField.setLayoutY(30);

    }

    public void drawWorld() {
        refreshPositions();
        //world = new Group();
        getChildren().clear();
        getChildren().addAll(r, blockSizeTextField, menuBar, topLine, leftLine);
        for (int i = 0; i < worldVector.size(); i++) {
            for (int j = 0; j < worldVector.get(i).size(); j++) {
                if (worldVector.get(i).get(j) != null) {
                    addBlock(Block.createBlock(worldVector.get(i).get(j)), i * blockSize, j * blockSize);
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

            r.setX(blockSize * (Math.floor((xPos) / blockSize)));
            r.setY(blockSize * (Math.floor((yPos) / blockSize)));

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
