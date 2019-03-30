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

    private ProtagonistOnlineClient[] protagonists;
    private double[] xPositions, yPositions;    //index = protagonists id
    private Protagonist.CostumeViewport[] viewports; //-""-
    private final int playerAmount;
    
    public GameLoopOnline(ProtagonistOnlineClient[] prots) {
        super();
        playerAmount = prots.length;
        xPositions = new double[playerAmount];
        yPositions = new double[playerAmount];
        viewports = new Protagonist.CostumeViewport[playerAmount];
        protagonists = prots;
        isInited = false;
        oldTime = 0;
        runTime = 0;
    }

    @Override
    public void handle(long now) {
        if (!isInited) {
            oldTime = now;
            isInited = true;
        }
        timeElapsed = now - oldTime;
        oldTime = now;
        timeElapsedSecond = timeElapsed / (1000.0d * 1000.0d * 1000.0d);
        runTime += timeElapsedSecond;
        
        /*
        
            Get x, y and viewport from server and fill it to arrays
        Example: Protagonist with id=0 is at x=20 and y=40
        => xPositions[0] = 20; yPositions[0] = 40;
                               
        */

        for(int i = 0; i < protagonists.length; i++) {
            protagonists[i].update(xPositions[i], yPositions[i], viewports[i]);
        }
    }

}
