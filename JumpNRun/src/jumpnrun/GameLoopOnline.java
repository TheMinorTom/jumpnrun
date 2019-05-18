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

    private HashMap<String, ProtagonistOnlineClient> protagonists;
    private double[] xPositions, yPositions;    //index = protagonists id
    private Protagonist.CostumeViewport[] viewports; //-""-
    private final int playerAmount;
    
    public GameLoopOnline(HashMap<String, ProtagonistOnlineClient> prots) {
        super();
        playerAmount = prots.size();
        protagonists = prots;
    }

    @Override
    public void handle(long now) {
        protagonists.forEach((String id, ProtagonistOnlineClient p)->{
            System.out.println("update");
            p.updatePos();
        });
    }

}
