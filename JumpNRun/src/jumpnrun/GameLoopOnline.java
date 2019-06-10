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
    private ObservableList<PowerupCollect> powerupCollects, addPowerups, removePowerups;

    public GameLoopOnline() {
        super();
        updatableObjects = FXCollections.observableArrayList();
        addObjects = FXCollections.observableArrayList();
        removeObjects = FXCollections.observableArrayList();
        powerupCollects = FXCollections.observableArrayList();
        addPowerups = FXCollections.observableArrayList();
        removePowerups = FXCollections.observableArrayList();
        game = JumpNRun.game;
        isInited = false;

    }

    @Override
    public void handle(long now) {
        try {
            if (!isInited) {
                oldTime = now;
                isInited = true;
            }
            timeElapsed = now - oldTime;
            oldTime = now;
            timeElapsedSecond = timeElapsed / (1000.0d * 1000.0d * 1000.0d);

            game.updateScrolling();
            updatableObjects.forEach((o) -> {
                o.updateGraphic(game.getXScroll(), game.getYScroll());
            });

            powerupCollects.forEach((PowerupCollect p) -> {
                p.updateViewportOnline(timeElapsedSecond);
            });

            if (addObjects.size() != 0) {
                updatableObjects.addAll(addObjects);
                addObjects.clear();
            }
            if (removeObjects.size() != 0) {
                updatableObjects.removeAll(removeObjects);
                removeObjects.clear();
            }
            if (addPowerups.size() != 0) {
                updatableObjects.addAll(addPowerups);
                powerupCollects.addAll(addPowerups);
                addPowerups.clear();
            }
            if (removePowerups.size() != 0) {
                updatableObjects.removeAll(removePowerups);
                powerupCollects.removeAll(removePowerups);
                removePowerups.clear();
            }
            JumpNRun.getGraphic().addGraphicFPS();
            JumpNRun.getGraphic().updateFPS(timeElapsedSecond);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addObject(OnlineUpdatableObject addObj) {
        addObjects.add(addObj);
    }

    public void removeObject(OnlineUpdatableObject remObj) {
        removeObjects.add(remObj);
    }

    public void addPowerupCollect(PowerupCollect p) {
        addPowerups.add(p);
    }

    public void removePowerupCollect(PowerupCollect p) {
        removePowerups.add(p);
    }

}
