/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import static javafx.scene.input.KeyCode.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import net.minortom.davidjumpnrun.configstore.Configuration;
import worldeditor.Block;
import worldeditor.IO;
import net.minortom.davidjumpnrun.i18n.Language;
import net.minortom.davidjumpnrun.i18n.LanguageEnglish;
import net.minortom.davidjumpnrun.i18n.LanguageGerman;
import net.minortom.davidjumpnrun.netcode.NetworkManager;

/**
 *
 * @author Norbert
 */
public class JumpNRun extends Application {
    public static JumpNRun game;
    
    public static final String sourcePath = ConfigManager.getStorageLocation();
    private static final String blocksDirPath = sourcePath + "sprites/blocks/";
    private static String worldPath = sourcePath + "worlds/world.david";

    private static final double spawnY = 100;
    public static final double spawnXDist = 350;

    private static Graphic graphic;

    private static Vector<Vector<Block>> worldVector;
    private static Vector<Shoot> shoots;
    private static Vector<PowerupCollect> powerupCollects;

    private SkinChooseMenu.Skin skinProt1, skinProt2;
    
    private Protagonist protagonist1, protagonist2;
    private Vector<ProtagonistOnlineClient> onlineProts;
    
    private static GameLoopOffline loopOffline;
    private static GameLoopOnline loopOnline;
    private static Stage primStage;
    public static Scene scene;
    private static double summonTimer, summonTime;
    private static Vector<Updatable> updatables;
    private static Parent mainMenu, gameScene, chooseGamemodeScreen, winScreen, offlineSkinChooseScreen1, offlineSkinChooseScreen2, onlineSkinChooseCreateGame, onlineSkinChooseJoinGame;
    private Gamemode currGamemode;
    private static int deathLimit;
    private static double timeLimit;

    public NetworkManager networkManager;
    public Language language;
    public Configuration config;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
        game = this;
        // The following sections are licensed under the MIT License. You should have already received a copy located at ../net/minortom/LICENSE.txt
        // Copyright 2019 MinorTom <mail in license file>
        //Language Selection default
        if(null == Language.defaultLang()){
            language = new Language(this);
        } else
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
        
        ConfigManager.game = this;
        config = ConfigManager.loadConfiguration();
        
        if(config == null){
            ConfigManager.info(language.JNRCfgDirCorrectPopTitle, ""
                    + language.JNRCfgDirCorrectPopText1
                    + ConfigManager.getStorageLocation()
                    + language.JNRCfgDirCorrectPopText2);
            config = new Configuration();
            config.gameLanguage = language;
            ConfigManager.saveConfiguration(config);
        }
        language = config.gameLanguage;
        language = Language.setNewLangNC(language, language);
        
        networkManager = new NetworkManager(this);
        // End licensed sections
        
        primStage = primaryStage;
        mainMenu = new MainMenu(this);
        chooseGamemodeScreen = new ChooseGamemodeMenu(this);
        winScreen = new WinScreen(this);
        offlineSkinChooseScreen1 = new SkinChooseMenu(this, SkinChooseMenu.SkinChooseMode.OFFLINE_PLAYER_1);
        offlineSkinChooseScreen2 = new SkinChooseMenu(this, SkinChooseMenu.SkinChooseMode.OFFLINE_PLAYER_2);
        onlineSkinChooseCreateGame = new SkinChooseMenu(this, SkinChooseMenu.SkinChooseMode.ONLINE_CREATE_GAME);
        onlineSkinChooseJoinGame = new SkinChooseMenu(this, SkinChooseMenu.SkinChooseMode.ONLINE_JOIN_GAME);
        //chooseSkinScreen = new SkinChooseMenu(this);
        //((SkinChooseMenu)chooseSkinScreen).updateStrings();
        //scene = new Scene(chooseSkinScreen);
        scene = new Scene(mainMenu);
        
        primaryStage.setTitle("Jump-N-Run");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
        ((WinScreen) winScreen).setWinner(1); //!!!
        
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        launch(args);
    }

    public void initGame() {
        updatables = new Vector();
        summonTimer = 0;
        summonTime = 5;
        powerupCollects = new Vector();

        InputStream worldIn = IO.getFileStream(worldPath);

        worldVector = IO.openWorld(worldIn, blocksDirPath);
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

    public void initOnlineGame() {
        worldVector = null; //Get world from server, maqybe to the constructor
        onlineProts = new Vector<ProtagonistOnlineClient>();
        /*
        fill Vector with new ProtagonistOnlineClients from server
        
        */
        loopOnline = new GameLoopOnline((ProtagonistOnlineClient[])onlineProts.toArray());
        loopOnline.start();
        
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
        } else if (protagonist1.getDeaths() > protagonist2.getDeaths()){
            ((WinScreen) winScreen).setWinner(2);
        } else {
            ((WinScreen)winScreen).setWinner(-1);
        }
        scene.setRoot(winScreen);
    }

    public void openChooseGamemodeMenu() {
        scene.setRoot(chooseGamemodeScreen);
        ((ChooseGamemodeMenu) chooseGamemodeScreen).updateStrings();
    }

    public void openMainMenu() {
        scene.setRoot(mainMenu);
        ((MainMenu) mainMenu).updateStrings();
    }
    
    public void openOfflineSkinChooseMenu1() {
        scene.setRoot(offlineSkinChooseScreen1);
        ((SkinChooseMenu)offlineSkinChooseScreen1).updateStrings();
    }
    
    public void openOfflineSkinChooseMenu2() {
        scene.setRoot(offlineSkinChooseScreen2);
        ((SkinChooseMenu)offlineSkinChooseScreen2).updateStrings();
    }
    
    public void openOnlineSkinChooseCreateGameMenu() {
        scene.setRoot(onlineSkinChooseCreateGame);
        ((SkinChooseMenu)onlineSkinChooseCreateGame).updateStrings();
    }
    
    public void openOnlineSkinChooseJoinGameMenu() {
        scene.setRoot(onlineSkinChooseJoinGame);
        ((SkinChooseMenu)onlineSkinChooseJoinGame).updateStrings();
    }
    
    // The following function is licensed under the MIT License. You should have already received a copy located at ../net/minortom/LICENSE.txt
    // Copyright 2019 MinorTom <mail in license file>
    public void openNetworkScreen() {
        scene.setRoot(networkManager);
        networkManager.updateStrings();
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
        return primStage.getWidth();
    }

    public static double getHeight() {
        return primStage.getHeight();
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
    
    public void setSkinProt1 (SkinChooseMenu.Skin s) {
        skinProt1 = s;
    }
    
    public void setSkinProt2 (SkinChooseMenu.Skin s) {
        skinProt2 = s;
    }

    @Override
    public void stop() {
        primStage.close();
        networkManager.shutdown();
        // TODO: Find out who is still running, making us unable to gracefully shut down.
        System.exit(0);
    }

    
}
