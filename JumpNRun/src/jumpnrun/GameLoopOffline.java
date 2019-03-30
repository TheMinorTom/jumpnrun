/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import java.util.Vector;
import javafx.animation.AnimationTimer;
import worldeditor.Block;

/**
 *
 * @author DavidPrivat
 */
abstract class GameLoopOffline extends AnimationTimer {

    private long timeElapsed;
    private double timeElapsedSecond;
    private long oldTime;
    protected double runTime;
    private boolean isInited;
    
    protected JumpNRun game;
    
    private Vector<Vector<Block>> worldVector;
    protected Protagonist protagonist1, protagonist2;
    private Vector<PowerupCollect> powerupCollects;
    private Vector<Updatable> updatables;

    public GameLoopOffline(Vector<Vector<Block>> worldVec, Protagonist prot1, Protagonist prot2, Vector<PowerupCollect> collects, Vector<Updatable> updatables) {
        super();
        worldVector = worldVec;
        protagonist1 = prot1;
        protagonist2 = prot2;
        powerupCollects = collects;
        this.updatables = updatables;
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

        for (int i = 0; i < updatables.size(); i++) {
            Updatable u = updatables.get(i);
            u.update(timeElapsedSecond, worldVector, protagonist1, protagonist2, powerupCollects);
        }
        JumpNRun.updateSummons(timeElapsedSecond);
    }
    
    public double getRunTime() {
        return runTime;
    }
    
    static class EndlessLoop extends GameLoopOffline {

        public EndlessLoop(Vector<Vector<Block>> worldVec, Protagonist prot1, Protagonist prot2, Vector<PowerupCollect> collects, Vector<Updatable> updatables, JumpNRun game) {
            super(worldVec, prot1, prot2, collects, updatables);
            this.game = game;
        }
        
        @Override
        public void handle(long now) {
            super.handle(now);
        }
    }

    static class DeathLimitLoop extends GameLoopOffline {

        private int deathLimit;

        public DeathLimitLoop(Vector<Vector<Block>> worldVec, Protagonist prot1, Protagonist prot2, Vector<PowerupCollect> collects, Vector<Updatable> updatables, int deathLimit, JumpNRun game) {
            super(worldVec, prot1, prot2, collects, updatables);
            this.deathLimit = deathLimit;
            this.game = game;
        }

        @Override
        public void handle(long now) {
            super.handle(now);
            if(protagonist1.getDeaths() > deathLimit || protagonist2.getDeaths() > deathLimit) {
                game.endGame();
            }
        }

    }

    static class TimeLimitLoop extends GameLoopOffline {

        private double timeLimit;
        
        public TimeLimitLoop(Vector<Vector<Block>> worldVec, Protagonist prot1, Protagonist prot2, Vector<PowerupCollect> collects, Vector<Updatable> updatables, double timeLimit, JumpNRun game) {
            super(worldVec, prot1, prot2, collects, updatables);
            this.timeLimit = timeLimit;
            this.game = game;
        }
        
        @Override
        public void handle(long now) {
            super.handle(now);
            if(runTime > timeLimit) {
                game.endGame();
            }
        }

    }

    public static EndlessLoop endlessLoop(Vector<Vector<Block>> worldVec, Protagonist prot1, Protagonist prot2, Vector<PowerupCollect> collects, Vector<Updatable> updatables, JumpNRun game) {
        return new EndlessLoop(worldVec, prot1, prot2, collects, updatables, game);
    }

    public static TimeLimitLoop timeLimitLoop(Vector<Vector<Block>> worldVec, Protagonist prot1, Protagonist prot2, Vector<PowerupCollect> collects, Vector<Updatable> updatables, double timeLimit, JumpNRun game) {
        return new TimeLimitLoop(worldVec, prot1, prot2, collects, updatables, timeLimit, game);
    }

    public static DeathLimitLoop deathLimitLoop(Vector<Vector<Block>> worldVec, Protagonist prot1, Protagonist prot2, Vector<PowerupCollect> collects, Vector<Updatable> updatables, int deathLimit, JumpNRun game) {
        return new DeathLimitLoop(worldVec, prot1, prot2, collects, updatables, deathLimit, game);
    }
}
