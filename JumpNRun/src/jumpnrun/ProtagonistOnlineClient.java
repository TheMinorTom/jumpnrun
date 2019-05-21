/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import jumpnrun.Protagonist.CostumeViewport;
import static jumpnrun.Protagonist.height;
import static jumpnrun.Protagonist.width;
import jumpnrun.SkinChooseMenu.Skin;
import net.minortom.davidjumpnrun.configstore.ConfigManager;
import worldeditor.IO;

/**
 *
 * @author DavidPrivat
 */
public class ProtagonistOnlineClient extends ImageView implements OnlineUpdatableObject {

    private static final double width = Protagonist.getWidth();
    private Label nameLbl;
    public final String pubId;
    private double spawnX, spawnY;
    private Label respawnTimerLabel;
    private String spritePath;
    private double xPos, yPos;
    private int indexId;
    private KeyCode[] protControlls;
    private Rectangle2D currViewport;

    public ProtagonistOnlineClient(int indexId, double x, double y, String skinFileName, String name, String pubId, KeyCode left, KeyCode right, KeyCode jump, KeyCode hit, KeyCode shoot, KeyCode use) {
        protControlls = new KeyCode[]{left, right, jump, hit, shoot, use};
        spawnY = y;
        spawnX = x;
        yPos = y;
        xPos = x;
        this.pubId = pubId;
        nameLbl = new Label(name);
        nameLbl.setFont(new Font(JumpNRun.game.language.getFontName(), 20));
        nameLbl.setVisible(true);
        updatePos(x, y, CostumeViewport.MID.ordinal());

        spritePath = "sprites/protagonist/" + skinFileName;
        this.indexId = indexId;
        Image image = new Image(ConfigManager.getFileStream(spritePath));

        setImage(image);
        setViewport(CostumeViewport.MID.getRect());

        respawnTimerLabel = new Label("3");
        respawnTimerLabel.setLayoutX(x);
        respawnTimerLabel.setLayoutY(y);
        respawnTimerLabel.setFont(new Font("Arial Black", 80));
        respawnTimerLabel.setTextFill(Color.RED);
        respawnTimerLabel.setVisible(false);

        setFitWidth(width);
        setFitHeight(height);

        setX(x);
        setY(y);
        setVisible(true);

    }

    @Override
    public void updatePos(double x, double y, int viewPort) {
        xPos = x;
        yPos = y;
        currViewport = CostumeViewport.values()[viewPort].getRect();
    }

    @Override
    public void updateGraphic() {
        setX(xPos);
        setY(yPos);
        setViewport(currViewport);
        nameLbl.setLayoutX((xPos + width / 2) - (nameLbl.getWidth() / 2));
        nameLbl.setLayoutY(yPos - 40);
    }

    public Label getNameLabel() {
        return nameLbl;
    }

    public KeyCode[] getControls() {
        return protControlls;
    }
}
