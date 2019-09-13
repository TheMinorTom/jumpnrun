/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.minortom.davidjumpnrun.netcode.ServerCommand;
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
    private double lastScrollX, lastScrollY;

    protected JumpNRun game;

    private double[] xPositions, yPositions;    //index = protagonists id
    private Protagonist.CostumeViewport[] viewports; //-""-
    private ObservableList<OnlineUpdatableObject> updatableObjects, addObjects, removeObjects;
    private ObservableList<PowerupCollect> powerupCollects, addPowerups, removePowerups;
    private double updateWohleWorldTimer;
    private String currentObjectUpdateString, newObjectUpdateString;
    private static GameLoopOnline gameLoopOnline;

    private int beforeWait, afterWait, allWaitTime, lastNotify, allNotifyTime, notifyCounter, waitCounter;

    private Lock lock;
    private Condition condition;
    private boolean isReadyToRead, isReadyToWrite;

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
        updateWohleWorldTimer = 0;
        lastScrollX = 0;
        lastScrollY = 0;
        currentObjectUpdateString = "";
        gameLoopOnline = this;
        beforeWait = 0;
        afterWait = 0;
        lastNotify = 0;
        notifyCounter = 0;
        waitCounter = 0;
        allNotifyTime = 0;
        allWaitTime = 0;
        lock = new ReentrantLock();
        condition = lock.newCondition();
        isReadyToRead = false;
        isReadyToWrite = true;

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
        updateWohleWorldTimer += timeElapsed;
        synchronized (this) {
            try {
                /*
                 lock.lock();

                 beforeWait = getTimeInMillis();
                 condition.await();
                 afterWait = getTimeInMillis();
                 */
                //game.networkManager.serverConnection.getCommandHandler().sendCommand(ServerCommand.OGAME_NEXTFRAME, new String[]{game.getLocalProt().pubId, game.gameName});
                //this.wait();    //Waiting for new update String
                if (currentObjectUpdateString != newObjectUpdateString) {
                    JumpNRun.getGraphic().addGraphicFPS();
                }
                currentObjectUpdateString = newObjectUpdateString;
                JumpNRun.game.updateOnlineObjects(currentObjectUpdateString);

                /*
                 if(updateWohleWorldTimer > 0.5) {
                 JumpNRun.getGraphic().updateWholeWorld();
                 updateWohleWorldTimer = 0;
                 }
                 */
                JumpNRun.getGraphic().updateScrolling();

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
                JumpNRun.getGraphic().updateFPS(timeElapsedSecond);
                if (lastScrollX != game.getXScroll() || lastScrollY != game.getYScroll()) {
                    JumpNRun.getGraphic().addScrollDelay(true);
                    lastScrollX = game.getXScroll();
                    lastScrollY = game.getYScroll();
                } else {
                    JumpNRun.getGraphic().addScrollDelay(false);
                }
                waitCounter++;
                allWaitTime += (afterWait - beforeWait);
                if (getTimeInMillis() % 50 == 0) {
                    System.out.println("Average wait time: " + ((double) allWaitTime) / ((double) waitCounter));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private int getTimeInMillis() {
        return (int) (System.nanoTime() / (1000.0d * 1000.0d));
    }

    public void setCurrentUpdateString(String s) {
        synchronized (this) {
            if (lastNotify == 0) {
                lastNotify = getTimeInMillis();
            }

            notifyCounter++;
            JumpNRun.getGraphic().addServerFPS();
            int now = getTimeInMillis();
            allNotifyTime += (now - lastNotify);
            lastNotify = now;
            if (getTimeInMillis() % 50 == 0) {
                System.out.println("Average notify time: " + (int) (((double) allNotifyTime) / ((double) notifyCounter)));
            }

            newObjectUpdateString = s;
            //this.notify();

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

    public static GameLoopOnline getGameLoopOnline() {
        return gameLoopOnline;
    }

    private synchronized void setReadyToWrite(boolean b) {
        isReadyToWrite = b;
    }

}
