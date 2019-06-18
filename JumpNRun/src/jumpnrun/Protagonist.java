/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import java.util.Vector;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import worldeditor.Block;
import worldeditor.IO;

/**
 *
 * @author Norbert
 */
public class Protagonist extends ImageView implements Updatable {

    private Image image;
    private String spritePath;
    private SkinChooseMenu.Skin skin;
    protected double xPos, yPos; 
    protected double xSpeed, ySpeed, xSpawn, ySpawn;
    protected boolean intersects;
    protected boolean goesRight;
    protected boolean goesLeft;
    protected double accPerSec;
    protected boolean jumpDone;
    protected boolean oldJumpDone;
    protected boolean hitDoing, shootDoing, respawnDoing;
    protected CostumeViewport currCostume;
    private double animationStateDouble;
    protected static double stepsPerSecond;
    protected double jumpTimer, hitTimer, shootTimer, respawnTimer, machinePistolTimer;   //Timer
    protected double forkAnimationXPosAdd, forkAnimationYPosAdd;
    public final static double width = 50;
    public final static double height = 100;
    protected final static double defaultXSpeed = 60;
    private KeyCode[] protControlls;
    public double spdFactor;
    protected final double defaultSpdFactor = 2;
    private Pitchfork pitchfork;
    private Gun gun;
    private Label respawnTimerLabel;
    private final int protId;
    protected boolean isDoubleSpeed, isMachinePistol;  //Powerup booleans
    protected Powerup powerup;
    protected boolean isFacingLeft;
    protected int deaths;
    


    public Protagonist(int indexId, double x, double y) {
        protId = indexId;
        resetAnimation();
        
        setFitWidth(width);
        setFitHeight(height);
        
        jumpTimer = 0;
        hitTimer = 0;
        shootTimer = 0;
        respawnTimer = 3;
        machinePistolTimer = 0;
        
        hitDoing = false;
        shootDoing = false;
        jumpDone = false;
        respawnDoing = false;
        isMachinePistol = false;
        
        setX(x);
        setY(y);
        xPos = x;
        yPos = y;
        xSpawn = xPos;
        ySpawn = yPos;
        setVisible(true);
        stepsPerSecond = 5;
        
        spdFactor = defaultSpdFactor;

    }
    
    public Protagonist(int id, KeyCode left, KeyCode right, KeyCode jump, KeyCode hit, KeyCode shoot, KeyCode use, double x, double y, SkinChooseMenu.Skin skin) {
        super();
        this.skin = skin;
        spritePath = skin.path;
        protId = id;
        isFacingLeft = false;
        xSpeed = 0;
        isDoubleSpeed = false;
        isMachinePistol = false;
        forkAnimationXPosAdd = 0;
        forkAnimationYPosAdd = 0;
        protControlls = new KeyCode[]{left, right, jump, hit, shoot, use};
        accPerSec = 1000;
        stepsPerSecond = 5;
        spdFactor = defaultSpdFactor;
        pitchfork = new Pitchfork();
        gun = new Gun(this);

        deaths = 0;

        powerup = null;

        image = new Image(ConfigManager.getFileStream(spritePath));

        setImage(image);
        resetAnimation();
        ySpeed = 0;

        jumpTimer = 0;
        hitTimer = 0;
        machinePistolTimer = 0;
        respawnTimer = 3;

        respawnTimerLabel = new Label("3");
        respawnTimerLabel.setLayoutX(x);
        respawnTimerLabel.setLayoutY(y);
        respawnTimerLabel.setFont(new Font("Arial Black", 80));
        respawnTimerLabel.setTextFill(Color.RED);
        respawnTimerLabel.setVisible(false);

        xPos = x;
        yPos = y;
        xSpawn = xPos;
        ySpawn = yPos;
        setX(xPos);
        setY(yPos);

        setFitWidth(width);
        setFitHeight(height);
        intersects = false;
        goesLeft = false;
        goesRight = false;
        jumpDone = false;
        oldJumpDone = false;
        hitDoing = false;
        shootDoing = false;
        respawnDoing = true;

        setOnKeyPressed((KeyEvent e) -> {

            if (e.getCode().equals(protControlls[0])) {
                goesLeft = true;

            } else if (e.getCode().equals(protControlls[1])) {
                goesRight = true;

            } else if (e.getCode().equals(protControlls[2])) {
                if (ySpeed == 0) {
                    jumpDone = true;
                }
            }

        }
        );

        /*

         */
    }

    @Override
    public void update(double timeElapsedSeconds, Vector<Vector<Block>> worldVec, Protagonist prot1, Protagonist prot2, Vector<PowerupCollect> powerupCollects) {
        Protagonist otherProt;
        if (protId == 1) {
            otherProt = prot2;
        } else {
            otherProt = prot1;
        }

        if (yPos > 5000) {
            hitten();
        }


        if (!respawnDoing) {
            checkCollects(powerupCollects);
            updateJump(timeElapsedSeconds);
            if (hitDoing) {
                updateHit(timeElapsedSeconds, otherProt);
            } else if (isMachinePistol) {
                updateMachinePistol(timeElapsedSeconds);
            } else if (shootDoing) {
                goesRight = false;
                goesLeft = false;
                updateShoot(timeElapsedSeconds, otherProt);
            }

            if (((!goesRight) && (!goesLeft)) && (ySpeed == 0)) {
                if (xSpeed != 0) {

                    // xSpeed -= (xSpeed / 10);
                    xSpeed = 0;
                    resetAnimation();
                }
            }

            if (!shootDoing) {
                if (goesRight && (xSpeed < defaultXSpeed)) {
                    //xSpeed += (defaultXSpeed - xSpeed) / 5;
                    xSpeed = defaultXSpeed;
                }
                if (goesLeft && (xSpeed > (-1 * defaultXSpeed))) {
                    //xSpeed -= (defaultXSpeed - xSpeed) / 5;
                    xSpeed = -1 * defaultXSpeed;
                }
            }

            intersects = false;
            ySpeed += timeElapsedSeconds * accPerSec;
            yPos += ySpeed * timeElapsedSeconds;
            setX(xPos);
            setY(yPos);

            intersects = collisionCheck(worldVec, otherProt);

            if (intersects) {
                yPos -= timeElapsedSeconds * ySpeed;
                ySpeed = 0;
                if (!isMachinePistol) {
                    updateAnimation(timeElapsedSeconds);
                }
            }
            xPos += xSpeed * spdFactor * timeElapsedSeconds;
            setX(xPos);
            setY(yPos);

            intersects = collisionCheck(worldVec, otherProt);
            if (intersects) {
                xPos -= xSpeed * spdFactor * timeElapsedSeconds;
                xSpeed = 0;
                //resetAnimation();
            }
            setX(xPos);
            setY(yPos);

            intersects = intersectsPlayer(otherProt);
            if (intersects) {
                yPos -= 20;
            }

            setX(xPos);
            setY(yPos);

        } else {
            updateRespawn(timeElapsedSeconds);

        }

    }

    public boolean collisionCheck(Vector<Vector<Block>> worldVec, Protagonist otherProt) {
        for (int i = 0; i < worldVec.size(); i++) {
            for (int j = 0; j < worldVec.get(i).size(); j++) {
                if (worldVec.get(i).get(j) != null) {
                    Block b = worldVec.get(i).get(j);
                    if (b.getIsSolid() && this.getBoundsInParent().intersects(b.getBoundsInParent())) {
                        return true;
                    }
                }
            }
        }
        if (intersectsPlayer(otherProt)) {
            return true;
        }
        return false;
    }

    public boolean intersectsPlayer(Protagonist p) {
        if (getBoundsInParent().intersects(p.getBoundsInParent())) {
            if (!p.isRespawning()) {

                return true;
            }
        } 
            return false;

    }

    public void checkCollects(Vector<PowerupCollect> collects) {
        for (int i = 0; i < collects.size(); i++) {
            PowerupCollect collect = collects.get(i);
            if (this.getBoundsInParent().intersects(collect.getBoundsInParent())) {
                Powerup powerupOld = powerup;
                powerup = new Powerup(collect.getIcon());
                JumpNRun.doCollect(collect, powerup, powerupOld, protId);

            }
        }
    }

    public void updateJump(double timeElapsedSeconds) {
        if (!jumpDone) {
            jumpTimer = 0;
        } else {
            if (pitchfork.getFacingLeft()) {
                forkAnimationXPosAdd = -5;
            } else {
                forkAnimationXPosAdd = -10;
            }

            jumpTimer += timeElapsedSeconds;
            if (jumpTimer < 0.2) {
                if (goesLeft) {
                    setAnimationState(CostumeViewport.LEFT_JUMP);
                } else if (goesRight) {
                    setAnimationState(CostumeViewport.RIGHT_JUMP);
                } else {
                    setAnimationState(CostumeViewport.MID_JUMP);
                }
            } else {
                if (currCostume == CostumeViewport.LEFT_JUMP) {
                    setAnimationState(CostumeViewport.LEFT_0);
                } else if (currCostume == CostumeViewport.RIGHT_JUMP) {
                    setAnimationState(CostumeViewport.RIGHT_0);
                } else {
                    setAnimationState(CostumeViewport.MID);
                }
                jumpDone = false;
                ySpeed = -1 * (Graphic.getBlockSize() * 10.5);
            }
        }
    }

    public void updateHit(double timeElapsedSeconds, Protagonist otherProt) {
        hitTimer += timeElapsedSeconds;
        pitchfork.setVisible(true);
        if (pitchfork.getFacingLeft()) {
            pitchfork.setX(getX() - 40); //- forkAnimationXPosAdd);
            setAnimationState(CostumeViewport.LEFT_HIT);
        } else {
            pitchfork.setX(getX() + 30); //+ forkAnimationXPosAdd);
            setAnimationState(CostumeViewport.RIGHT_HIT);
        }
        pitchfork.setY(getY() + 55 + forkAnimationYPosAdd);
        pitchfork.updateHit(timeElapsedSeconds, otherProt);

        if (hitTimer > 0.3) {
            hitDoing = false;
            pitchfork.setVisible(false);
            hitTimer = 0;
            setAnimationState(CostumeViewport.MID);

        }
    }

    public void updateShoot(double timeElapsedSeconds, Protagonist otherProt) {
        shootTimer += timeElapsedSeconds;
        gun.setVisible(true);
        if (gun.getFacingLeft()) {
            gun.setX(getX() - 20); //- forkAnimationXPosAdd);
            setAnimationState(CostumeViewport.LEFT_SHOOT);
        } else {
            gun.setX(getX() + 5); //+ forkAnimationXPosAdd);
            setAnimationState(CostumeViewport.RIGHT_SHOOT);
        }
        gun.setY(getY() + 22); ///
        gun.updateShoot(shootTimer);

        if (shootTimer > 2) {
            shootDoing = false;
            gun.setVisible(false);
            shootTimer = 0;
            setAnimationState(CostumeViewport.MID);

        }
    }

    public void updateMachinePistol(double timeElapsed) {
        machinePistolTimer += timeElapsed;
        gun.setVisible(true);
        if (gun.getFacingLeft()) {
            gun.setX(getX() - 20); //- forkAnimationXPosAdd);
            setAnimationState(CostumeViewport.LEFT_SHOOT);
        } else {
            gun.setX(getX() + 5); //+ forkAnimationXPosAdd);
            setAnimationState(CostumeViewport.RIGHT_SHOOT);
        }
        gun.setY(getY() + 22);
        gun.updateMachinePistol(machinePistolTimer);
        if (machinePistolTimer > 5) {
            isMachinePistol = false;
            gun.setVisible(false);
            machinePistolTimer = 0;
            setAnimationState(CostumeViewport.MID);

        }
    }

    public void updateRespawn(double timeElapsedSeconds) {
        setVisible(false);
        respawnTimerLabel.setVisible(true);
        respawnTimer -= timeElapsedSeconds;
        respawnTimerLabel.setText(String.valueOf((int) respawnTimer + 1));
        if (respawnTimer < 0) {
            respawnDoing = false;
            respawnTimer = 3;
            respawnTimerLabel.setVisible(false);
            setVisible(true);

        }

    }

    public void resetAnimation() {
        animationStateDouble = 0;
        setAnimationState(CostumeViewport.MID);
    }

    public void updateAnimation(double timeElapsedSeconds) {

        if ((goesLeft || goesRight) && (!jumpDone) && (!hitDoing) && (!shootDoing)) {
            if (goesLeft) {
                animationStateDouble -= timeElapsedSeconds * stepsPerSecond * spdFactor;
                if (animationStateDouble > -1) {
                    animationStateDouble--;
                }
            } else if (goesRight) {
                animationStateDouble += timeElapsedSeconds * stepsPerSecond * spdFactor;
                if (animationStateDouble < 1) {
                    animationStateDouble++;
                }
            }
            if (animationStateDouble <= -9) {
                animationStateDouble += 8;
            } else if (animationStateDouble >= 9) {
                animationStateDouble -= 8;
            }
            switch ((int) animationStateDouble) {
                case -8:
                case -4:
                    setAnimationState(CostumeViewport.LEFT_0);
                    forkAnimationXPosAdd = -10;
                    forkAnimationYPosAdd = 5;
                    break;
                case -7:
                case -5:
                    setAnimationState(CostumeViewport.LEFT_2);
                    forkAnimationXPosAdd = -19;
                    forkAnimationYPosAdd = 1;
                    break;
                case -6:
                    setAnimationState(CostumeViewport.LEFT_1);
                    forkAnimationXPosAdd = -21;
                    forkAnimationYPosAdd = 0;
                    break;
                case -3:
                case -1:
                    setAnimationState(CostumeViewport.LEFT_3);
                    forkAnimationXPosAdd = 3;
                    forkAnimationYPosAdd = 1;
                    break;
                case -2:
                    setAnimationState(CostumeViewport.LEFT_4);
                    forkAnimationXPosAdd = 5;
                    forkAnimationYPosAdd = 0;
                    break;
                case 0:
                    setAnimationState(CostumeViewport.MID);
                    forkAnimationXPosAdd = 0;
                    break;
                case 1:
                case 3:
                    setAnimationState(CostumeViewport.RIGHT_3);
                    forkAnimationXPosAdd = 0;
                    forkAnimationYPosAdd = 1;
                    break;
                case 2:
                    setAnimationState(CostumeViewport.RIGHT_4);
                    forkAnimationXPosAdd = 5;
                    forkAnimationYPosAdd = 0;
                    break;
                case 4:
                case 8:
                    setAnimationState(CostumeViewport.RIGHT_0);
                    forkAnimationXPosAdd = -12;
                    forkAnimationYPosAdd = 2;
                    break;
                case 5:
                case 7:
                    setAnimationState(CostumeViewport.RIGHT_2);
                    forkAnimationXPosAdd = -25;
                    forkAnimationYPosAdd = 1;
                    break;
                case 6:
                    setAnimationState(CostumeViewport.RIGHT_1);
                    forkAnimationXPosAdd = -28;
                    forkAnimationYPosAdd = 0;
                    break;

            }
        }
    }

    public void hitten() {
        if (!respawnDoing) {
            isDoubleSpeed = false;
            isMachinePistol = false;
            gun.setVisible(false);
            pitchfork.setVisible(false);
            spdFactor = defaultSpdFactor;
            xPos = xSpawn;
            yPos = ySpawn;
            ySpeed = 0;
            setX(xPos);
            setY(yPos);
            resetAnimation();
            respawnDoing = true;
            Graphic.countLabel(protId);
            deaths++;
        }

    }

    public void doUse() {
        if (powerup != null) {
            switch (powerup.getIcon()) {
                case DOUBLE_SPEED:
                    spdFactor = defaultSpdFactor * 2;
                    isDoubleSpeed = true;
                    break;

                case MACHINE_PISTOL:
                    isMachinePistol = true;
                    shootDoing = false;
                    break;

                case TRUCK:
                    JumpNRun.addUpdatable(new TruckHandler(this));
                    break;
            }

            JumpNRun.removeNode(powerup);
            powerup = null;
        }
    }

    public void doRight() {
        isFacingLeft = true;
        goesRight = true;
        goesLeft = false;
        pitchfork.setFacingLeft(false);
        gun.setFacingLeft(false);
    }

    public void doLeft() {
        isFacingLeft = false;
        goesLeft = true;
        goesRight = false;
        pitchfork.setFacingLeft(true);
        gun.setFacingLeft(true);
    }

    public void doJump() {
        if (ySpeed == 0) {
            jumpDone = true;
        }
    }

    public void doHit() {
        if ((!shootDoing) && (!isMachinePistol)) {
            hitDoing = true;
        }
    }

    public void doShoot() {
        if (!hitDoing) {
            shootDoing = true;
        }
    }

    public void releaseRight() {
        goesRight = false;
    }

    public void releaseLeft() {
        goesLeft = false;
    }

    public void setGoesRight(boolean gR) {
        goesRight = gR;
        if (goesLeft) {
            goesLeft = false;
        }
    }

    public void setGoesLeft(boolean gL) {
        goesLeft = gL;
        if (goesRight) {
            goesRight = false;
        }
    }

    public void setAnimationState(CostumeViewport costume) {
        currCostume = costume;
        setViewport(currCostume.getRect());

    }

    public KeyCode[] getControls() {
        return protControlls;
    }

    public Pitchfork getPitchfork() {
        return pitchfork;
    }

    public Gun getGun() {
        return gun;
    }

    public Label getRespawnLabel() {
        return respawnTimerLabel;
    }

    public static double getWidth() {
        return width;
    }

    public static double getHeight() {
        return height;
    }

    public enum CostumeViewport {

        LEFT_0(0, 0, Protagonist.getWidth(), Protagonist.getHeight()),
        LEFT_1(50, 0, Protagonist.getWidth(), Protagonist.getHeight()),
        LEFT_2(100, 0, Protagonist.getWidth(), Protagonist.getHeight()),
        LEFT_3(150, 0, Protagonist.getWidth(), Protagonist.getHeight()),
        LEFT_4(200, 0, Protagonist.getWidth(), Protagonist.getHeight()),
        MID(251, 0, Protagonist.getWidth(), Protagonist.getHeight()),
        RIGHT_4(300, 0, Protagonist.getWidth(), Protagonist.getHeight()),
        RIGHT_3(350, 0, Protagonist.getWidth(), Protagonist.getHeight()),
        RIGHT_2(400, 0, Protagonist.getWidth(), Protagonist.getHeight()),
        RIGHT_1(450, 0, Protagonist.getWidth(), Protagonist.getHeight()),
        RIGHT_0(500, 0, Protagonist.getWidth(), Protagonist.getHeight()),
        LEFT_JUMP(0, 103, Protagonist.getWidth(), Protagonist.getHeight()),
        MID_JUMP(50, 103, Protagonist.getWidth(), Protagonist.getHeight()),
        RIGHT_JUMP(100, 103, Protagonist.getWidth(), Protagonist.getHeight()),
        LEFT_HIT(150, 103, Protagonist.getWidth(), Protagonist.getHeight()),
        RIGHT_HIT(200, 103, Protagonist.getWidth(), Protagonist.getHeight()),
        LEFT_SHOOT(251, 103, Protagonist.getWidth(), Protagonist.getHeight()),
        RIGHT_SHOOT(300, 103, Protagonist.getWidth(), Protagonist.getHeight());
        private final double minX;
        private final double minY;
        private final double width;
        private final double height;
        private final Rectangle2D rect;

        CostumeViewport(double x, double y, double w, double h) {
            this.minX = x;
            this.minY = y;
            this.width = w;
            this.height = h;
            this.rect = new Rectangle2D(minX, minY, width, height);
        }

        public Rectangle2D getRect() {
            return rect;
        }

    }

    public static Rectangle2D getMidViewport() {
        return CostumeViewport.MID.getRect();
    }

    public void setPowerup(Powerup p) {
        powerup = p;
    }

    public boolean getFacingRight() {
        return isFacingLeft;
    }

    public boolean isRespawning() {
        return respawnDoing;
    }

    public int getDeaths() {
        return deaths;
    }
    
    public CostumeViewport getCurrCostume() {
        return currCostume;
    }
}
