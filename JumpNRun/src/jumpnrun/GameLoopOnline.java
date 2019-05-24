/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import java.util.HashMap;
import java.util.Vector;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import worldeditor.Block;

/**
 *
 * @author DavidPrivat
 */
public class GameLoopOnline extends AnimationTimer {

    private long timeElapsed;
    private double timeElapsedSecond;
    private long oldTime;
    protected double runTime;
    private boolean isInited;

    protected JumpNRun game;

    private double[] xPositions, yPositions;    //index = protagonists id
    private Protagonist.CostumeViewport[] viewports; //-""-
    private ObservableList<OnlineUpdatableObject> updatableObjects, addObjects, removeObjects;
    
    public GameLoopOnline() {
        super();
        updatableObjects = FXCollections.observableArrayList();
        addObjects = FXCollections.observableArrayList();
        removeObjects = FXCollections.observableArrayList();
        game = JumpNRun.game;
    }

    @Override
    public void handle(long now) {
        updatableObjects.forEach((o)->{
            o.updateGraphic();
        });
        if(addObjects.size() != 0) {
            updatableObjects.addAll(addObjects);
            addObjects.clear();
        }
        if(removeObjects.size() != 0) {
            updatableObjects.removeAll(removeObjects);
            removeObjects.clear();
        }
        game.updateWorldScrolling();
    }
    
    public void addObject(OnlineUpdatableObject addObj) {
        addObjects.add(addObj);
    }
    
    public void removeObject (OnlineUpdatableObject remObj) {
        removeObjects.add(remObj);
    }

}
