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

    private ObservableList<OnlineUpdatableObject> objects;
    private double[] xPositions, yPositions;    //index = protagonists id
    private Protagonist.CostumeViewport[] viewports; //-""-
    
    public GameLoopOnline(ObservableList<OnlineUpdatableObject> objects) {
        super();
        this.objects = objects;
    }

    @Override
    public void handle(long now) {
        objects.forEach((o)->{
            o.updateGraphic();
        });
    }

}
