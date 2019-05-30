/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import worldeditor.*;

/**
 *
 * @author Norbert
 */
public class Graphic extends Group {

    public final static double lblYDist = 50;

    public static Font lblFont = new Font("Cooper Black", 30);
    public final static double lblXDist = 50;

    private static Vector<Vector<Block>> worldVector;
    private static Group worldGroup;
    private static Label leftLbl, rightLbl, timeLabel;
    private static int deaths1, deaths2, respawnsLeft1, respawnsLeft2;
    private static double blockSize;
    private static Protagonist protagonist1, protagonist2;
    private static JumpNRun.Gamemode gamemode;
    private static CountDownLabel onlinetimeLabel;

    private static HBox onlinePlayersBox;
    private static HashMap<String, Label> onlinePlayersVarLabels;
    private static ArrayList<HBox> playerBoxes;

    public Graphic(Vector<Vector<Block>> worldVec, Protagonist prot1, Protagonist prot2, JumpNRun.Gamemode gamemode) {
        super();
        lblFont = JumpNRun.game.language.getFont();
        this.gamemode = gamemode;
        worldVector = worldVec;
        deaths1 = 0;
        deaths2 = 0;
        respawnsLeft1 = JumpNRun.getDeathLimit();
        respawnsLeft2 = JumpNRun.getDeathLimit();
        for (int i = 0; i < worldVector.size(); i++) {
            for (int j = 0; j < worldVector.get(i).size(); j++) {
                Block block = worldVector.get(i).get(j);
                if (block != null) {
                    blockSize = block.getFitWidth();
                    block.setX(block.getLayoutX());
                    block.setY(block.getLayoutY());
                }
            }
        }

        leftLbl = new Label();
        rightLbl = new Label();

        if (gamemode == JumpNRun.Gamemode.ENDLESS || gamemode == JumpNRun.Gamemode.TIME) {
            leftLbl.setText("0" + JumpNRun.game.language.GraphicKills);
            rightLbl.setText("0" + JumpNRun.game.language.GraphicKills);
        } else {
            leftLbl.setText(String.valueOf(respawnsLeft1) + JumpNRun.game.language.GraphicRespawnsLeft);
            rightLbl.setText(String.valueOf(respawnsLeft1) + JumpNRun.game.language.GraphicRespawnsLeft);
        }

        leftLbl.setLayoutX(JumpNRun.spawnXDist);
        leftLbl.setLayoutY(lblYDist);
        leftLbl.setTextFill(Color.BLUE);
        leftLbl.setFont(lblFont);

        rightLbl.setLayoutX(JumpNRun.getWidth() - JumpNRun.spawnXDist);
        rightLbl.setLayoutY(lblYDist);
        rightLbl.setTextFill(Color.BLUE);
        rightLbl.setFont(lblFont);

        if (gamemode == JumpNRun.Gamemode.TIME) {
            timeLabel = new CountDownLabel();
        } else {
            timeLabel = new CountUpLabel();
        }

        JumpNRun.addUpdatable(
                (Updatable) timeLabel);

        timeLabel.setLayoutY(lblYDist);

        timeLabel.setLayoutX(JumpNRun.getWidth() / 2);
        timeLabel.setFont(JumpNRun.game.language.getHeadingFont());
        timeLabel.setBorder(
                new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(100), new BorderWidths(10))));

        protagonist1 = prot1;
        protagonist2 = prot2;
        //spriteGroup.getChildren().add(protagonist);

        worldGroup = GUI.drawWorld(worldVector, blockSize);

        worldGroup.getChildren()
                .addAll(leftLbl, rightLbl, timeLabel);
        worldGroup.getChildren()
                .addAll(protagonist1, protagonist2, protagonist1.getPitchfork(), protagonist2.getPitchfork(), protagonist1.getGun(), protagonist2.getGun(), protagonist1.getRespawnLabel(), protagonist2.getRespawnLabel());

        getChildren()
                .addAll(worldGroup);
    }

    public void updateScrolling() {
        for (int i = 0; i < worldVector.size(); i++) {
            for (int j = 0; j < worldVector.get(i).size(); j++) {
                Block block = worldVector.get(i).get(j);
                if (block != null) {
                    block.setX((blockSize * i) + JumpNRun.game.getXScroll());
                    block.setY((blockSize * j) + JumpNRun.game.getYScroll());
                }
            }
        }
    }

    public Graphic(Vector<Vector<Block>> worldVec) {

        worldVector = worldVec;
        System.out.println("World: " + worldVec);
        worldGroup = GUI.drawWorld(worldVec, worldVec.get(0).get(0).getFitWidth());
        getChildren().addAll(worldGroup);
    }

    public ProtagonistOnlineClient generateOtherOnlineProt(String name, String skinFileName, int indexId, String pubId, int playerAmount, double spawnY, String userId) {
        double worldWidth = worldVector.size() * blockSize;
        double spawnX = (worldWidth / (playerAmount + 1)) * (indexId + 1);
        ProtagonistOnlineClient addProt = new ProtagonistOnlineClient(indexId, spawnX, spawnY, skinFileName, name, pubId, LEFT, RIGHT, UP, P, O, I, userId);

        Platform.runLater(() -> {
            worldGroup.getChildren().addAll(addProt, addProt.getNameLabel());
        });
        HBox tmpBox = new HBox();
        tmpBox.setAlignment(Pos.CENTER);
        tmpBox.setSpacing(10);
        tmpBox.setPadding(new Insets(0, 5, 0, 5));
        VBox lblBox = new VBox();
        lblBox.setAlignment(Pos.CENTER);
        lblBox.setSpacing(10);
        lblBox.setPadding(new Insets(0, 0, 0, 0));
        Label playerName = new Label(addProt.nameLbl.getText());
        Label varLbl = new Label("");
        ImageView avatar = new ImageView();
        avatar.setImage(new Image("https://v1.api.minortom.net/do/avatar.php?user=" + addProt.userId));
        avatar.setFitWidth(JumpNRun.game.language.getFontSize() * 3);
        avatar.setPreserveRatio(true);
        avatar.setSmooth(true);
        playerName.setFont(JumpNRun.game.language.getFont());
        varLbl.setFont(JumpNRun.game.language.getFont());
        onlinePlayersVarLabels.put(addProt.pubId, varLbl);
        lblBox.getChildren().addAll(playerName, varLbl);
        tmpBox.getChildren().addAll(avatar, lblBox);
        playerBoxes.add(tmpBox);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                onlinePlayersBox.getChildren().addAll(tmpBox);
            }
        });
        return addProt;
    }

    public ProtagonistOnlineClient generateLocalOnlineProt(String name, String skinFileName, int indexId, String pubId, int playerAmount, double spawnY, String userId) {
        double worldWidth = worldVector.size() * blockSize;
        double spawnX = (worldWidth / (playerAmount + 1)) * (indexId + 1);
        ProtagonistOnlineClient addProt = new ProtagonistOnlineClient(indexId, spawnX, spawnY, skinFileName, JumpNRun.game.language.playerNameLocalPlayer, pubId, LEFT, RIGHT, UP, P, O, I, userId);
        JumpNRun.game.setLocalProt(addProt);
        addProt.getNameLabel().setFont(new Font("Arial Black", 30));
        Platform.runLater(() -> {
            worldGroup.getChildren().addAll(addProt, addProt.getNameLabel());
        });
        HBox tmpBox = new HBox();
        tmpBox.setAlignment(Pos.CENTER);
        tmpBox.setSpacing(10);
        tmpBox.setPadding(new Insets(0, 5, 0, 5));
        VBox lblBox = new VBox();
        lblBox.setAlignment(Pos.CENTER);
        lblBox.setSpacing(10);
        lblBox.setPadding(new Insets(0, 0, 0, 0));
        Label playerName = new Label(addProt.nameLbl.getText());
        Label varLbl = new Label("");
        ImageView avatar = new ImageView();
        avatar.setImage(new Image("https://v1.api.minortom.net/do/avatar.php?user=" + addProt.userId));
        avatar.setFitWidth(JumpNRun.game.language.getFontSize() * 3);
        avatar.setPreserveRatio(true);
        avatar.setSmooth(true);
        playerName.setFont(JumpNRun.game.language.getFont());
        varLbl.setFont(JumpNRun.game.language.getFont());
        onlinePlayersVarLabels.put(addProt.pubId, varLbl);
        lblBox.getChildren().addAll(playerName, varLbl);
        tmpBox.getChildren().addAll(avatar, lblBox);
        playerBoxes.add(tmpBox);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                onlinePlayersBox.getChildren().addAll(tmpBox);
            }
        });
        return addProt;
    }

    public static double getBlockSize() {

        return blockSize;
    }

    public static void countLabel(int id) {
        if (gamemode == JumpNRun.Gamemode.ENDLESS || gamemode == JumpNRun.Gamemode.TIME) {
            if (id == 1) {
                deaths1++;
                if (deaths1 > 1) {
                    rightLbl.setText(String.valueOf(deaths1) + " Kills");
                } else {
                    rightLbl.setText("1 Kill");
                }
            } else if (id == 2) {
                deaths2++;
                if (deaths2 > 1) {
                    leftLbl.setText(String.valueOf(deaths2) + " Kills");
                } else {
                    leftLbl.setText("1 Kill");
                }

            }
        } else {
            if (id == 1) {
                deaths1++;
                respawnsLeft1--;
                if (respawnsLeft1 > 1) {
                    leftLbl.setText(String.valueOf(respawnsLeft1) + " Respawns übrig");
                } else if (respawnsLeft1 == 1) {
                    leftLbl.setText("1 Respawn übrig");
                } else {
                    leftLbl.setText("KEIN Respawn übrig");
                }
            } else if (id == 2) {
                deaths2++;
                respawnsLeft2--;
                if (respawnsLeft2 > 1) {
                    rightLbl.setText(String.valueOf(respawnsLeft2) + " Respawns übrig");
                } else if (respawnsLeft2 == 1) {
                    rightLbl.setText("1 Respawn übrig");
                } else {
                    rightLbl.setText("KEIN Respawn übrig");
                }
            }
        }
    }

    public static void initOnlineOverlay() {
        onlinePlayersVarLabels = new HashMap<>();
        onlinePlayersBox = new HBox();
        onlinePlayersBox.setLayoutX(JumpNRun.getWidth() / 2);
        onlinePlayersBox.setLayoutY(JumpNRun.getHeight() - lblYDist - JumpNRun.game.language.getFontSize() * 3);
        onlinePlayersBox.setAlignment(Pos.CENTER);
        onlinePlayersBox.setSpacing(50);
        onlinePlayersBox.setPadding(new Insets(0, 20, 0, 20));
        playerBoxes = new ArrayList<>();
    }

    public static void drawOnlineOverlay() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("executed onlineplayersbox");
                if (!worldGroup.getChildren().contains(onlinePlayersBox)) {
                    worldGroup.getChildren().addAll(onlinePlayersBox);
                }
            }
        });
    }



    class CountDownLabel extends Label implements Updatable, OnlineUpdatableObject {

        private int timeLeftMinutes;
        private int timeLeftSeconds;
        private final double startTime;
        private double timeLeft;

        public CountDownLabel() {
            super(String.valueOf(JumpNRun.getTimeLimit()));
            startTime = JumpNRun.getTimeLimit();
            timeLeft = startTime;

        }

        @Override
        public void update(double timeElapsed, Vector<Vector<Block>> world, Protagonist prot1, Protagonist prot2, Vector<PowerupCollect> powerupCollects) {
            timeLeft = startTime - JumpNRun.getRunTime();
            if (timeLeft < 10) {
                setTextFill(Color.RED);
            }
            timeLeftSeconds = ((int) timeLeft) % 60;
            timeLeftMinutes = (int) (timeLeft / 60);
            setText(String.valueOf(timeLeftMinutes) + " min, " + String.valueOf(timeLeftSeconds) + "s");
            setLayoutX(JumpNRun.getWidth() / 2 - getWidth() / 2);
        }

        @Override
        public void updatePos(double d1, double d2, int val) {
            timeLeftSeconds = ((int) val) % 60;
            timeLeftMinutes = (int) (val / 60);

        }

        @Override
        public void updateGraphic(double d1, double d2) {
            setText(String.valueOf(timeLeftMinutes) + " min, " + String.valueOf(timeLeftSeconds) + "s");
            setLayoutX(JumpNRun.getWidth() / 2 - getWidth() / 2);
        }

    };

    class CountUpLabel extends Label implements Updatable {

        private int runTimeMinutes;
        private int runTimeSeconds;

        public CountUpLabel() {
            super("0");
        }

        @Override
        public void update(double timeElapsed, Vector<Vector<Block>> world, Protagonist prot1, Protagonist prot2, Vector<PowerupCollect> powerupCollects) {
            runTimeSeconds = ((int) JumpNRun.getRunTime()) % 60;
            runTimeMinutes = (int) (JumpNRun.getRunTime() / 60);
            setText(String.valueOf(runTimeMinutes) + " min, " + String.valueOf(runTimeSeconds) + "s");
            setLayoutX(JumpNRun.getWidth() / 2 - getWidth() / 2);
        }

    };

    public double getLeftLabelWidth() {
        return leftLbl.getWidth();
    }

    public double getRightLabelWidth() {
        return rightLbl.getWidth();
    }

    public void addNode(Node n) {
        worldGroup.getChildren().add(n);
    }

    public CountDownLabel getOnlineTimeLabel() {
        return onlinetimeLabel;
    }
}
