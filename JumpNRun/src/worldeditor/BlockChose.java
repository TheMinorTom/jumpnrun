/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldeditor;

import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Norbert
 */
public class BlockChose extends ContextMenu {

    BlockChose() {
        super();
        for (int i = 0; i < WorldEditor.blocks.length; i++) {
            Block block = Block.createBlock(WorldEditor.blocks[i]);
            addItem(block);

        }

    }
    public void unlockStrunz() {
        for (int i = 0; i < WorldEditor.strunz.length; i++) {
            Block block = Block.createBlock(WorldEditor.strunz[i]);
            addItem(block);

        }
    }

    public void addItem(Block block) {
        MenuItem m = new MenuItem();

        ImageView imageView = new ImageView(block.getImage());
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        m.setGraphic(imageView);
        m.setText(block.getName());
        m.setOnAction((ActionEvent e) -> {
            GUI.setSelectedBlock(block);
        });

        getItems().add(m);
    }
}
