/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import java.util.Vector;
import worldeditor.Block;

/**
 *
 * @author Norbert
 */
public class TruckHandler implements Updatable {

    private Vector<Truck> trucks;
    private double spawnTimer;
    private int trucksSummonedAmount;
    private Protagonist owner;
    private static final double spawnTime = 6;

    public TruckHandler(Protagonist owner) {
        spawnTimer = 0;
        trucksSummonedAmount = 0;
        this.owner = owner;
    }

    @Override
    public void update(double timeElapsed, Vector<Vector<Block>> world, Protagonist prot1, Protagonist prot2, Vector<PowerupCollect> powerupCollects) {
        if (owner.isRespawning()) {
            JumpNRun.removeUpdatable(this);
        } else {
            spawnTimer += timeElapsed;
            if (spawnTimer > spawnTime) {
                if (trucksSummonedAmount == 4) {
                    trucksSummonedAmount++;
                    Truck summonTruck = new Truck(owner.getX(), owner.getY() + 20, owner.getFacingRight(), owner);
                    JumpNRun.addUpdatable(summonTruck);
                    JumpNRun.addNode(summonTruck);
                    JumpNRun.removeUpdatable(this);
                }
            } else if (spawnTimer > 0.75 * spawnTime) {
                if (trucksSummonedAmount == 3) {
                    trucksSummonedAmount++;
                    Truck summonTruck = new Truck(owner.getX(), owner.getY(), owner.getFacingRight(), owner);
                    JumpNRun.addUpdatable(summonTruck);
                    JumpNRun.addNode(summonTruck);
                }
            } else if (spawnTimer > 0.5 * spawnTime) {
                if (trucksSummonedAmount == 2) {
                    trucksSummonedAmount++;
                    Truck summonTruck = new Truck(owner.getX(), owner.getY(), owner.getFacingRight(), owner);
                    JumpNRun.addUpdatable(summonTruck);
                    JumpNRun.addNode(summonTruck);
                }
            } else if (spawnTimer > 0.25 * spawnTime) {
                if (trucksSummonedAmount == 1) {
                    trucksSummonedAmount++;
                    Truck summonTruck = new Truck(owner.getX(), owner.getY(), owner.getFacingRight(), owner);
                    JumpNRun.addUpdatable(summonTruck);
                    JumpNRun.addNode(summonTruck);
                }
            } else {
                if (trucksSummonedAmount == 0) {
                    trucksSummonedAmount++;
                    Truck summonTruck = new Truck(owner.getX(), owner.getY(), owner.getFacingRight(), owner);
                    JumpNRun.addUpdatable(summonTruck);
                    JumpNRun.addNode(summonTruck);

                }
            }
        }
    }
}
