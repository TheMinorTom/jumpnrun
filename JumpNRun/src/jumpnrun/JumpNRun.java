/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import static javafx.scene.input.KeyCode.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import static jumpnrun.Graphic.lblYDist;
import jumpnrun.SkinChooseMenu.Skin;
import net.minortom.davidjumpnrun.CreditsScreen;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import net.minortom.davidjumpnrun.configstore.Configuration;
import worldeditor.Block;
import worldeditor.IO;
import net.minortom.davidjumpnrun.i18n.Language;
import net.minortom.davidjumpnrun.i18n.LanguageEnglish;
import net.minortom.davidjumpnrun.i18n.LanguageGerman;
import net.minortom.davidjumpnrun.netcode.GameObjectType;
import net.minortom.davidjumpnrun.netcode.NetworkManager;
import net.minortom.davidjumpnrun.netcode.screens.OnlineEndScreen;
import net.minortom.davidjumpnrun.server.OnlineGameObject;
import net.minortom.davidjumpnrun.server.Server;
import worldeditor.GUI;
import worldeditor.InGameSceneWorldEditor;
import worldeditor.WorldEditor;

/**
 *
 * @author Norbert
 */
public class JumpNRun extends Application {

    public static JumpNRun game;

    public static String sourcePath;
    private static String blocksDirPath;
    private static String worldAbsPath;

    private static final double spawnY = 100;
    public static final double spawnXDist = 350;
    public static final double powerupSize = 40;

    private static Graphic graphic;

    private static Vector<Vector<Block>> worldVector;
    private static Vector<Shoot> shoots;
    private static Vector<PowerupCollect> powerupCollects;

    private SkinChooseMenu.Skin skinProt1, skinProt2;

    private Protagonist protagonist1, protagonist2;

    private static GameLoopOffline loopOffline;
    private static GameLoopOnline loopOnline;
    private static Stage primStage;
    public static Scene scene;
    public static Scene worldEditScene;
    public static GUI worldEditGUI;
    private static double summonTimer, summonTime;
    private static Vector<Updatable> updatables;
    private static Parent mainMenu, gameScene, chooseGamemodeScreen, winScreen, offlineSkinChooseScreen1, offlineSkinChooseScreen2, onlineSkinChooseCreateGame, onlineSkinChooseJoinGame, worldEditorScreen, onlineEndScreen;
    private static CreditsScreen creditsScreen;
    private Gamemode currGamemode;
    private static int deathLimit;
    private static double timeLimit;

    public NetworkManager networkManager;
    public Language language;
    public Configuration config;

    private Scene worldEditorScene;

    //Online Stuff
    private ProtagonistOnlineClient localProt;
    private double onlineSpawnY;
    private int playerAmount;
    private double onlineTimeLimit;
    private int onlineRespawnLimit;
    private Gamemode onlineGamemode;
    private HashMap<String, ProtagonistOnlineClient> onlineProts;
    private HashMap<String, Object> onlineGameObjects;
    public String gameName = "";

    private boolean[] keysDown;

    private double xScroll, yScroll;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // DATABASE STUFF

        // new DatabaseManager();
        // DB STUFF END
        primStage = primaryStage;
        try {
            game = this;
            // The following sections are licensed under the MIT License. You should have already received a copy located at ../net/minortom/LICENSE.txt
            // Copyright 2019 MinorTom <mail in license file>
            //Language Selection default
            if (null == Language.defaultLang()) {
                language = new Language(this);
            } else {
                switch (Language.defaultLang()) {
                    case "EN":
                        language = new LanguageEnglish(this);
                        break;
                    case "DE":
                        language = new LanguageGerman(this);
                        break;
                    default:
                        language = new Language(this);
                        break;
                }
            }

            ConfigManager.game = this;
            config = ConfigManager.loadConfiguration();

            if (config == null) {
                ConfigManager.info(language.JNRCfgDirCorrectPopTitle, ""
                        + language.JNRCfgDirCorrectPopText1
                        + ConfigManager.getStorageLocation()
                        + language.JNRCfgDirCorrectPopText2);
                config = new Configuration();
                config.gameLanguage = language;
                ConfigManager.saveConfiguration(config);
            }

            sourcePath = ConfigManager.getStorageLocation();

            worldAbsPath = ConfigManager.getStorageLocation() + "worlds/world.david";
            blocksDirPath = ConfigManager.getStorageLocation() + "sprites/blocks/";
            worldeditor.WorldEditor.initBlocksArr();
            language = config.gameLanguage;
            //language = Language.setNewLangNC(language, language);

            language.setFontFileNC();

            networkManager = new NetworkManager(this);
            // End licensed sections

            mainMenu = new MainMenu(this);
            // scene = new Scene(mainMenu); !!!!!!!!!!!!

            chooseGamemodeScreen = new ChooseGamemodeMenu(this);
            winScreen = new WinScreen(this);
            offlineSkinChooseScreen1 = new SkinChooseMenu(this, SkinChooseMenu.SkinChooseMode.OFFLINE_PLAYER_1);
            offlineSkinChooseScreen2 = new SkinChooseMenu(this, SkinChooseMenu.SkinChooseMode.OFFLINE_PLAYER_2);
            onlineSkinChooseCreateGame = new SkinChooseMenu(this, SkinChooseMenu.SkinChooseMode.ONLINE_CREATE_GAME);
            onlineSkinChooseJoinGame = new SkinChooseMenu(this, SkinChooseMenu.SkinChooseMode.ONLINE_JOIN_GAME);
            creditsScreen = new CreditsScreen(this);
            onlineEndScreen = new OnlineEndScreen(this);

            scene = new Scene(mainMenu);
            primaryStage.setScene(scene);
            worldEditorScreen = new GUI(this);
            worldEditorScene = new Scene(worldEditorScreen);

            //chooseSkinScreen = new SkinChooseMenu(this);
            //((SkinChooseMenu)chooseSkinScreen).updateStrings();
            //scene = new Scene(chooseSkinScreen);
            WorldEditor.initBlocksArr();

            primaryStage.setTitle(game.language.GWindowName);

            primaryStage.setMaximized(true);
            primaryStage.show();
            ((WinScreen) winScreen).setWinner(1); //!!!

            keysDown = new boolean[]{false, false, false, false, false, false, false};
            primStage.setMaximized(true);

            //!!
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String starttype;
        try {
            starttype = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            starttype = "client";
        }

        /*
         List<String> templeft = Arrays.asList(args);
         templeft.remove(starttype);
         String[] rArgs = (String[]) templeft.toArray();
         */
        // TODO: Fix shift left
        String[] rArgs = new String[]{};
        if (args.length > 0) {
            rArgs = new String[args.length - 1];
            for (int i = 1; i < args.length; i++) {
                rArgs[i - 1] = args[i];
            }
        }

        switch (starttype) {
            case "server":
                Server.main(rArgs);
                break;
            case "mapEdit":
                // TODO: Implement map edit start
                break;
            case "help":
                System.out.println("Options: [server|client|help]");
                System.exit(0);
                break;
            case "client":
            default:
                launch(rArgs);
                break;
        }
    }

    public void initGame() {
        updatables = new Vector();
        summonTimer = 0;
        summonTime = 5;
        powerupCollects = new Vector();

        InputStream worldIn = ConfigManager.getFileStreamAbsPath(worldAbsPath);

        worldVector = IO.openWorld(worldIn);
        protagonist1 = new Protagonist(1, A, D, W, C, V, B, spawnXDist, spawnY, skinProt1);
        protagonist2 = new Protagonist(2, LEFT, RIGHT, UP, P, O, I, getWidth() - spawnXDist - Protagonist.width, spawnY, skinProt2);
        updatables.add(protagonist1);
        updatables.add(protagonist2);
        graphic = new Graphic(worldVector, protagonist1, protagonist2, currGamemode);
        gameScene = new Group(graphic);
        scene.setRoot(gameScene);
        if (currGamemode == Gamemode.ENDLESS) {
            loopOffline = GameLoopOffline.endlessLoop(worldVector, protagonist1, protagonist2, powerupCollects, updatables, this);
        } else if (currGamemode == Gamemode.DEATHS) {
            loopOffline = GameLoopOffline.deathLimitLoop(worldVector, protagonist1, protagonist2, powerupCollects, updatables, deathLimit, this);
        } else if (currGamemode == Gamemode.TIME) {
            loopOffline = GameLoopOffline.timeLimitLoop(worldVector, protagonist1, protagonist2, powerupCollects, updatables, timeLimit, this);
        }
        loopOffline.start();
        setUpKeyHandlers();
    }

    public void initOnlineGame(String playerAmount, String spawnY, String gamemode, String limit, String gameName) {
        powerupCollects = new Vector<PowerupCollect>();
        yScroll = 0;
        xScroll = 0;
        loopOnline = new GameLoopOnline();
        onlineProts = new HashMap();
        onlineGameObjects = new HashMap<>();
        this.gameName = gameName;
        try {
            this.playerAmount = Integer.parseInt(playerAmount);
        } catch (NumberFormatException e) {
            System.err.println("Could not read playerAmount!");
        }
        try {
            this.onlineSpawnY = Double.parseDouble(spawnY);
        } catch (NumberFormatException e) {
            System.out.println("Could not read spawnY!");
        }

        switch (gamemode.toLowerCase()) {
            case "time":
                onlineGamemode = Gamemode.TIME;
                onlineTimeLimit = Double.parseDouble(limit);
                break;
            case "deaths":
                onlineGamemode = Gamemode.DEATHS;
                onlineRespawnLimit = Integer.parseInt(limit);
                break;
            case "endless":
            default:
                onlineGamemode = Gamemode.ENDLESS;
                break;
        }
        this.worldVector = null;

    }

    public void startOnlineGame() {
        loopOnline.start();
    }

    public void initMap(String mapAsString) {
        worldVector = IO.openWorld(mapAsString, "sprites" + File.separator + "blocks" + File.separator);
        graphic = new Graphic(worldVector);
        gameScene = new Group(graphic);
        scene.setRoot(gameScene);
        setUpOnlineKeyHandlers();
    }

    public void initOtherProt(String name, String skinFileName, int index, String pubId, String objectId, String userId, String score) {
        ProtagonistOnlineClient addProt = graphic.generateOtherOnlineProt(name, skinFileName, index, pubId, playerAmount, onlineSpawnY, userId, score);
        onlineProts.put(pubId, addProt);
        onlineGameObjects.put(objectId, addProt);
        loopOnline.addObject(addProt);
    }

    public void initLocalProt(String name, String skinFileName, int index, String pubId, String objectId, String userId, String score) {
        ProtagonistOnlineClient addProt = graphic.generateLocalOnlineProt(name, skinFileName, index, pubId, playerAmount, onlineSpawnY, userId, score);
        onlineProts.put(pubId, addProt);
        onlineGameObjects.put(objectId, addProt);
        loopOnline.addObject(addProt);
        updateScrolling();
    }

    public void setLocalProt(ProtagonistOnlineClient p) {
        localProt = p;
    }

    public void updateScrolling() {
        xScroll = -1 * (localProt.getXPos() - primStage.getWidth() / 2);
        yScroll = -1 * (localProt.getYPos() - primStage.getHeight() / 4);
        graphic.updateScrolling();

    }

    public void updateOnlineObject(String objectId, String objectTypeAsIntAsString, String xPosString, String yPosString, String animationStateAsIntAsString) {

        GameObjectType objectType = GameObjectType.values()[Integer.parseInt(objectTypeAsIntAsString)];
        double xPos = Double.parseDouble(xPosString);
        double yPos = Double.parseDouble(yPosString);
        int animationStateAsInt = Integer.parseInt(animationStateAsIntAsString);
        boolean alreadyExists = onlineGameObjects.containsKey(objectId);
        if (alreadyExists) {
            Node currNode = (Node) onlineGameObjects.get(objectId);
            if (animationStateAsInt < 0) {
                currNode.setVisible(false);
            } else {
                currNode.setVisible(true);
            }
        }
        if (animationStateAsInt >= 0) {
            switch (objectType) {
                case PROTAGONIST:
                    ((ProtagonistOnlineClient) onlineGameObjects.get(objectId)).updatePos(xPos, yPos, animationStateAsInt);
                    break;
                case PITCHFORK:
                    if (alreadyExists) {
                        Pitchfork pitchfork = (Pitchfork) onlineGameObjects.get(objectId);
                        pitchfork.updatePos(xPos, yPos, animationStateAsInt);

                    } else {
                        Pitchfork addPitchfork = new Pitchfork();
                        Platform.runLater(() -> {
                            graphic.getChildren().add(addPitchfork);
                        });
                        onlineGameObjects.put(objectId, addPitchfork);
                        loopOnline.addObject(addPitchfork);
                        updateOnlineObject(objectId, objectTypeAsIntAsString, xPosString, yPosString, animationStateAsIntAsString);
                    }
                    break;
                case GUN:
                    if (alreadyExists) {
                        Gun gun = (Gun) onlineGameObjects.get(objectId);
                        gun.updatePos(xPos, yPos, animationStateAsInt);

                    } else {
                        Gun addGun = new Gun();
                        Platform.runLater(() -> {
                            graphic.getChildren().add(addGun);
                        });
                        onlineGameObjects.put(objectId, addGun);
                        loopOnline.addObject(addGun);
                        updateOnlineObject(objectId, objectTypeAsIntAsString, xPosString, yPosString, animationStateAsIntAsString);
                    }
                    break;
                case SHOOT:
                    if (alreadyExists) {
                        Shoot shoot = (Shoot) onlineGameObjects.get(objectId);
                        shoot.updatePos(xPos, yPos, animationStateAsInt);

                    } else {
                        Shoot addShoot = new Shoot();
                        Platform.runLater(() -> {
                            graphic.getChildren().add(addShoot);
                        });
                        onlineGameObjects.put(objectId, addShoot);
                        loopOnline.addObject(addShoot);
                        updateOnlineObject(objectId, objectTypeAsIntAsString, xPosString, yPosString, animationStateAsIntAsString);
                    }
                    break;

                case RESPAWNTIMER:
                    if (alreadyExists) {

                        ((OnlineUpdatableCounterLabel) onlineGameObjects.get(objectId)).updatePos(xPos, yPos, animationStateAsInt);

                    } else {

                        OnlineUpdatableCounterLabel respawnTimerLabel = new OnlineUpdatableCounterLabel("", xPos, yPos, true);
                        respawnTimerLabel.setFont(new Font("Arial Black", 80));
                        respawnTimerLabel.setTextFill(Color.RED);
                        respawnTimerLabel.setVisible(true);
                        onlineGameObjects.put(objectId, respawnTimerLabel);
                        loopOnline.addObject(respawnTimerLabel);
                        Platform.runLater(() -> {
                            graphic.getChildren().add(respawnTimerLabel);
                        });
                    }
                    break;
                case GAMETIMER:
                    if (alreadyExists) {
                        OnlineUpdatableCounterLabel counterLabel = (OnlineUpdatableCounterLabel) onlineGameObjects.get(objectId);
                        counterLabel.updatePos(primStage.getWidth() * 0.5, Graphic.lblYDist, animationStateAsInt);
                        counterLabel.updateText(String.valueOf((int) (animationStateAsInt / 60)) + "min, " + String.valueOf(animationStateAsInt % 60) + "s");

                    } else {

                        OnlineUpdatableCounterLabel onlinetimeLabel = new OnlineUpdatableCounterLabel("", primStage.getWidth() / 2, Graphic.lblYDist, false);

                        loopOnline.addObject(onlinetimeLabel);
                        onlinetimeLabel.setFont(JumpNRun.game.language.getHeadingFont());
                        onlinetimeLabel.setBorder(
                                new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(100), new BorderWidths(10))));
                        onlineGameObjects.put(objectId, onlinetimeLabel);
                        Platform.runLater(() -> {
                            graphic.getChildren().add(onlinetimeLabel);
                        });
                    }

                    break;
                case KILLCOUNT:
                    if (alreadyExists) {

                        ((OnlineUpdatableCounterLabel) onlineGameObjects.get(objectId)).updatePos(primStage.getWidth() * 0.25, Graphic.lblYDist, animationStateAsInt);
                        //((OnlineUpdatableCounterLabel) onlineGameObjects.get(objectId)).updateText(String.valueOf((int) (animationStateAsInt / 60)) + "min, " + String.valueOf(animationStateAsInt % 60) + "s");

                    } else {

                        OnlineUpdatableCounterLabel onlineKillCount = new OnlineUpdatableCounterLabel(language.killsLabelText, Graphic.lblXDist, Graphic.lblYDist, false);

                        loopOnline.addObject(onlineKillCount);
                        onlineKillCount.setFont(JumpNRun.game.language.getHeadingFont());
                        onlineKillCount.setBorder(
                                new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.DASHED, new CornerRadii(10), new BorderWidths(2))));
                        onlineGameObjects.put(objectId, onlineKillCount);
                        Platform.runLater(() -> {
                            graphic.getChildren().add(onlineKillCount);
                        });
                    }

                    break;

                case DEATHCOUNT:
                    if (alreadyExists) {
                        OnlineUpdatableCounterLabel label = (OnlineUpdatableCounterLabel) onlineGameObjects.get(objectId);
                        label.updatePos((0.75 * primStage.getWidth()), Graphic.lblYDist, animationStateAsInt);
                        //((OnlineUpdatableCounterLabel) onlineGameObjects.get(objectId)).updateText(String.valueOf((int) (animationStateAsInt / 60)) + "min, " + String.valueOf(animationStateAsInt % 60) + "s");

                    } else {
                        OnlineUpdatableCounterLabel onlineDeathCount;
                        if (onlineGamemode.equals(Gamemode.DEATHS)) {
                            onlineDeathCount = new OnlineUpdatableCounterLabel(language.respawnLabelText, (primStage.getWidth() * 0.75) - Graphic.lblXDist, Graphic.lblYDist, false);
                        } else {
                            onlineDeathCount = new OnlineUpdatableCounterLabel(language.deathLabelText, primStage.getWidth() - Graphic.lblXDist, Graphic.lblYDist, false);
                        }

                        loopOnline.addObject(onlineDeathCount);
                        onlineDeathCount.setFont(JumpNRun.game.language.getHeadingFont());
                        onlineDeathCount.setBorder(
                                new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.DASHED, new CornerRadii(10), new BorderWidths(2))));
                        onlineGameObjects.put(objectId, onlineDeathCount);
                        Platform.runLater(() -> {
                            graphic.getChildren().add(onlineDeathCount);
                        });
                    }

                    break;
                case POWERUP_COLLECT:
                    if (alreadyExists) {
                        ((PowerupCollect) onlineGameObjects.get(objectId)).updatePos(xPos, yPos, animationStateAsInt);
                        //((OnlineUpdatableCounterLabel) onlineGameObjects.get(objectId)).updateText(String.valueOf((int) (animationStateAsInt / 60)) + "min, " + String.valueOf(animationStateAsInt % 60) + "s");

                    } else {
                        PowerupCollect collect = new PowerupCollect();
                        loopOnline.addPowerupCollect(collect);

                        onlineGameObjects.put(objectId, collect);
                        Platform.runLater(() -> {
                            graphic.getChildren().add(collect);
                        });
                    }

                    break;
                case POWERUP:
                    if (alreadyExists) {
                        ((Powerup) onlineGameObjects.get(objectId)).updatePos(xPos, yPos, animationStateAsInt);
                        //((OnlineUpdatableCounterLabel) onlineGameObjects.get(objectId)).updateText(String.valueOf((int) (animationStateAsInt / 60)) + "min, " + String.valueOf(animationStateAsInt % 60) + "s");

                    } else {
                        Powerup powerup = new Powerup(animationStateAsInt);
                        loopOnline.addObject(powerup);

                        onlineGameObjects.put(objectId, powerup);
                        Platform.runLater(() -> {
                            graphic.getChildren().add(powerup);
                        });
                    }

                    break;

            }

        } else {
            if (objectType.equals(GameObjectType.PROTAGONIST)) {
                ((ProtagonistOnlineClient) onlineGameObjects.get(objectId)).updatePos(xPos, yPos, animationStateAsInt);
            }
        }
    }

    public double getXScroll() {
        return xScroll;
    }

    public double getYScroll() {
        return yScroll;
    }

    public void removeOnlineObject(String objectId) {
        Node removeObject = (Node) onlineGameObjects.get(objectId);
        Platform.runLater(() -> {
            graphic.getChildren().remove(removeObject);
        });
        if (removeObject instanceof OnlineUpdatableObject) {
            loopOnline.removeObject((OnlineUpdatableObject) removeObject);
        }

        onlineGameObjects.remove(objectId);
    }

    public void updateOnlineObjects(String message) {
        String[] differentObjectsArr = message.split("\\" + NetworkManager.differentObjectsSeperator);
        String[] currArgs = null;
        for (String currObject : differentObjectsArr) {
            currArgs = currObject.split("\\" + NetworkManager.subArgsSeperator);
            updateOnlineObject(currArgs[0], currArgs[1], currArgs[2], currArgs[3], currArgs[4]);
        }
    }

    public void endOnlineGame(String message) {
        ((OnlineEndScreen) onlineEndScreen).startInserting();
        String[] playersArgs = message.split("\\" + NetworkManager.differentObjectsSeperator);
        String[] currArgs = null;
        for (String currPlayer : playersArgs) {
            currArgs = currPlayer.split("\\" + NetworkManager.subArgsSeperator);
            ((OnlineEndScreen) onlineEndScreen).addPlayerEntry(currArgs);
        }
        ((OnlineEndScreen) onlineEndScreen).updateStrings();
        scene.setRoot(onlineEndScreen);
    }

    public HashMap<String, ProtagonistOnlineClient> getOnlineProts() {
        return onlineProts;
    }

    public static Graphic getGraphic() {
        return graphic;
    }

    private void setUpKeyHandlers() {
        graphic.setOnKeyPressed((KeyEvent e) -> {
            if (e.getCode() == protagonist1.getControls()[0]) {
                protagonist1.doLeft();
            } else if (e.getCode() == protagonist1.getControls()[1]) {
                protagonist1.doRight();
            } else if (e.getCode() == protagonist1.getControls()[2]) {
                protagonist1.doJump();
            } else if (e.getCode() == protagonist1.getControls()[3]) {
                protagonist1.doHit();
            } else if (e.getCode() == protagonist1.getControls()[4]) {
                protagonist1.doShoot();
            } else if (e.getCode() == protagonist1.getControls()[5]) {
                protagonist1.doUse();
            } else if (e.getCode() == protagonist2.getControls()[0]) {
                protagonist2.doLeft();
            } else if (e.getCode() == protagonist2.getControls()[1]) {
                protagonist2.doRight();
            } else if (e.getCode() == protagonist2.getControls()[2]) {
                protagonist2.doJump();
            } else if (e.getCode() == protagonist2.getControls()[3]) {
                protagonist2.doHit();
            } else if (e.getCode() == protagonist2.getControls()[4]) {
                protagonist2.doShoot();
            } else if (e.getCode() == protagonist2.getControls()[5]) {
                protagonist2.doUse();
            }

        });
        graphic.setOnKeyReleased((KeyEvent e) -> {
            if (e.getCode() == protagonist1.getControls()[0]) {
                protagonist1.releaseLeft();
            } else if (e.getCode() == protagonist1.getControls()[1]) {
                protagonist1.releaseRight();
            } else if (e.getCode() == protagonist2.getControls()[0]) {
                protagonist2.releaseLeft();
            } else if (e.getCode() == protagonist2.getControls()[1]) {
                protagonist2.releaseRight();
            }
        });
        graphic.requestFocus();
    }

    private void setUpOnlineKeyHandlers() {
        graphic.setOnKeyPressed((KeyEvent e) -> {
            if (e.getCode() == localProt.getControls()[0]) {

                if (!keysDown[0]) {
                    keysDown[0] = true;
                    networkManager.sendKeyPress(localProt.pubId, gameName, "LEFT");
                }
            } else if (e.getCode() == localProt.getControls()[1]) {
                if (!keysDown[1]) {
                    keysDown[1] = true;
                    networkManager.sendKeyPress(localProt.pubId, gameName, "RIGHT");
                }
            } else if (e.getCode() == localProt.getControls()[2]) {
                if (!keysDown[2]) {
                    keysDown[2] = true;

                    networkManager.sendKeyPress(localProt.pubId, gameName, "JUMP");
                }
            } else if (e.getCode() == localProt.getControls()[3]) {
                if (!keysDown[3]) {
                    keysDown[3] = true;

                    networkManager.sendKeyPress(localProt.pubId, gameName, "HIT");
                }
            } else if (e.getCode() == localProt.getControls()[4]) {
                if (!keysDown[4]) {
                    keysDown[4] = true;

                    networkManager.sendKeyPress(localProt.pubId, gameName, "SHOOT");
                }
            } else if (e.getCode() == localProt.getControls()[5]) {
                if (!keysDown[5]) {
                    keysDown[5] = true;

                    networkManager.sendKeyPress(localProt.pubId, gameName, "USE");
                }
            } else if (e.getCode() == localProt.getControls()[6]) {
                if (!keysDown[6]) {
                    keysDown[6] = true;

                    networkManager.sendKeyPress(localProt.pubId, gameName, "DOWN");
                }
            }

        });
        graphic.setOnKeyReleased((KeyEvent e) -> {
            if (e.getCode() == localProt.getControls()[0]) {
                keysDown[0] = false;
                networkManager.sendKeyRelease(localProt.pubId, gameName, "LEFT");
            } else if (e.getCode() == localProt.getControls()[1]) {
                keysDown[1] = false;
                networkManager.sendKeyRelease(localProt.pubId, gameName, "RIGHT");
            } else if (e.getCode() == localProt.getControls()[2]) {
                keysDown[2] = false;
                networkManager.sendKeyRelease(localProt.pubId, gameName, "JUMP");
            } else if (e.getCode() == localProt.getControls()[3]) {
                keysDown[3] = false;
                networkManager.sendKeyRelease(localProt.pubId, gameName, "HIT");
            } else if (e.getCode() == localProt.getControls()[4]) {
                keysDown[4] = false;
                networkManager.sendKeyRelease(localProt.pubId, gameName, "SHOOT");
            } else if (e.getCode() == localProt.getControls()[5]) {
                keysDown[5] = false;
                networkManager.sendKeyRelease(localProt.pubId, gameName, "USE");
            } else if (e.getCode() == localProt.getControls()[6]) {
                keysDown[6] = false;
                networkManager.sendKeyRelease(localProt.pubId, gameName, "DOWN");
            }
        });
        Platform.runLater(() -> {
            graphic.requestFocus();
        });
    }

    void setWorldPath(String customPath) {
        if (!customPath.isEmpty()) {
            worldAbsPath = customPath;

        }
    }

    public enum Gamemode {

        ENDLESS,
        TIME,
        DEATHS;
    }

    public static void updateSummons(double timeElapsed) {
        summonTimer += timeElapsed;
        if (summonTimer > summonTime) {
            summonTimer = 0;
            summonTime = 25 - Math.random() * 10;
            summonPowerup();
        }
    }

    public void endGame() {
        loopOffline.stop();
        if (protagonist1.getDeaths() < protagonist2.getDeaths()) {
            ((WinScreen) winScreen).setWinner(1);
        } else if (protagonist1.getDeaths() > protagonist2.getDeaths()) {
            ((WinScreen) winScreen).setWinner(2);
        } else {
            ((WinScreen) winScreen).setWinner(-1);
        }
        scene.setRoot(winScreen);
    }

    public void openChooseGamemodeMenu() {
        scene.setRoot(chooseGamemodeScreen);
        ((ChooseGamemodeMenu) chooseGamemodeScreen).updateStrings();
    }

    public void openMainMenu() {
        if (!(primStage.getScene().equals(scene))) {
            primStage.setTitle(game.language.GWindowName);
            primStage.setMaximized(false);
            primStage.setScene(scene);
            primStage.setMaximized(true);
        }
        scene.setRoot(mainMenu);
        ((MainMenu) mainMenu).updateStrings();
    }

    public void openOfflineSkinChooseMenu1() {
        scene.setRoot(offlineSkinChooseScreen1);
        ((SkinChooseMenu) offlineSkinChooseScreen1).updateStrings();
    }

    public void openOfflineSkinChooseMenu2() {
        scene.setRoot(offlineSkinChooseScreen2);
        ((SkinChooseMenu) offlineSkinChooseScreen2).updateStrings();
    }

    public void openOnlineSkinChooseCreateGameMenu() {
        scene.setRoot(onlineSkinChooseCreateGame);
        ((SkinChooseMenu) onlineSkinChooseCreateGame).updateStrings();
    }

    public void openOnlineSkinChooseJoinGameMenu() {
        scene.setRoot(onlineSkinChooseJoinGame);
        ((SkinChooseMenu) onlineSkinChooseJoinGame).updateStrings();
    }

    // The following function is licensed under the MIT License. You should have already received a copy located at ../net/minortom/LICENSE.txt
    // Copyright 2019 MinorTom <mail in license file>
    public void openNetworkScreen() {
        scene.setRoot(networkManager);
        networkManager.updateStrings();
    }

    public void openCreditsScreen() {
        scene.setRoot(creditsScreen);
        creditsScreen.updateStrings();
    }

    public void openWorldEditor() {
        Platform.runLater(() -> {

            ((GUI) worldEditorScreen).setUpHandlers(worldEditorScene);
            ((GUI) worldEditorScreen).drawWorld();
            //WorldEditor.main();
            primStage.setScene(worldEditorScene);
            primStage.setMaximized(false);
            primStage.setMaximized(true);

        });

    }

    public static void addUpdatable(Updatable u) {
        updatables.add(u);
    }

    public static void removeUpdatable(Updatable u) {
        updatables.remove(u);
    }

    public static void addNode(Node n) {
        graphic.getChildren().add(n);
    }

    public static void removeNode(Node n) {
        graphic.getChildren().remove(n);
    }

    static void doCollect(PowerupCollect collect, Powerup powerupNew, Powerup powerupOld, int id) {
        powerupCollects.remove(collect);
        graphic.getChildren().remove(collect);
        updatables.remove(collect);
        if (powerupOld != null) {
            graphic.getChildren().remove(powerupOld);
        }

        powerupNew.setFitWidth(100);
        powerupNew.setFitHeight(100);
        if (id == 1) {
            powerupNew.setLayoutX(spawnXDist + graphic.getLeftLabelWidth() + 20);
            powerupNew.setLayoutY(Graphic.lblYDist - powerupNew.getFitHeight() / 2 + Graphic.lblFont.getSize() / 2);

        } else if (id == 2) {
            powerupNew.setLayoutX(getWidth() - spawnXDist - powerupNew.getFitWidth() - 20);
            powerupNew.setLayoutY(Graphic.lblYDist - powerupNew.getFitHeight() / 2 + Graphic.lblFont.getSize() / 2);
        }
        graphic.getChildren().add(powerupNew);
    }

    public static void summonPowerup() {
        boolean isSummoned = false;
        while (!isSummoned) {
            int xIndex = (int) (System.nanoTime() % (worldVector.size() - 2));
            int yIndex = (int) (System.nanoTime() % (worldVector.get(0).size() - 2));
            Block currBlock = worldVector.get(xIndex + 1).get(yIndex + 1);
            if (!currBlock.getIsSolid()) {
                Block blockUnder = worldVector.get(xIndex + 1).get(yIndex + 2);
                Block blockAbove = worldVector.get(xIndex + 1).get(yIndex);
                if (blockUnder != null && blockUnder.getIsSolid() && (!blockAbove.getIsSolid())) {
                    PowerupCollect powerupCollect = new PowerupCollect();
                    powerupCollect.setLayoutX(currBlock.getX());
                    powerupCollect.setLayoutY(currBlock.getY());
                    graphic.getChildren().add(powerupCollect);
                    powerupCollects.add(powerupCollect);
                    updatables.add(powerupCollect);
                    isSummoned = true;
                }
            }

        }
    }

    public static double getWidth() {
        return Screen.getPrimary().getVisualBounds().getWidth();
    }

    public static double getHeight() {
        return Screen.getPrimary().getVisualBounds().getHeight();
    }

    public void setCurrGamemode(Gamemode gamemode) {
        currGamemode = gamemode;
    }

    public void setTimeLimit(double limit) {
        timeLimit = limit;
    }

    public void setDeathLimit(int limit) {
        deathLimit = limit;
    }

    public static int getDeathLimit() {
        return deathLimit;
    }

    public static double getTimeLimit() {
        return timeLimit;
    }

    public static double getRunTime() {
        return loopOffline.getRunTime();
    }

    public void setSkinProt1(SkinChooseMenu.Skin s) {
        skinProt1 = s;
    }

    public void setSkinProt2(SkinChooseMenu.Skin s) {
        skinProt2 = s;
    }

    @Override
    public void stop() {
        primStage.close();
        networkManager.shutdown();
        // TODO: Find out who is still running, making us unable to gracefully shut down.
        System.exit(0);
    }

    public Stage getPrimStage() {
        return primStage;
    }

    public GameLoopOnline getOnlineLoop() {
        return loopOnline;
    }

    public HashMap getGameObjects() {
        return onlineGameObjects;
    }

    public Vector<PowerupCollect> getPowerupCollects() {
        return powerupCollects;
    }

}
