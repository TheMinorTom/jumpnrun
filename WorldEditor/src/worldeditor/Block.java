/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldeditor;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author User
 */
public class Block extends ImageView {

    private Image image;
    private final String name;
    private final String imageFileName;
    private final String blocksDir;
    private boolean isSolid;

    Block(String n, String dir, String fileName, boolean solid) {
        super();
        isSolid = solid;
        if(!isSolid)
        {
            setOpacity(0.5);
        }
        imageFileName = fileName;
        blocksDir = dir;
        
        int temp = fileName.length();
        boolean empty = (temp == 0);
        if(empty)
        {
            
        }else
        {
            if(!dir.equals("C:\\Users\\User\\AppData\\Roaming/davidjumpnrun/sprites/blocks/")) {
                int o = 0;
            }
            image = new Image(IO.getFileStream(dir+fileName));
            setImage(image);
        }
        
        name = n;

        setVisible(true);
        updateSize();
    }

    private Block(Block b) {
        super();
        image = b.getImage();
        name = b.getName();
        imageFileName = b.getImageFileName();
        blocksDir = b.getBlocksDir();
        isSolid = b.getIsSolid();
        setOpacity(b.getOpacity());
        setImage(image);
        setVisible(true);
        updateSize();
    }

    public static Block createBlock(Block b) {
        if (b != null) {
            return new Block(b);
        } else {
            return null;
        }
    }

    public static Block createBlock(Block b, double xPos, double yPos) {
        if (b != null) {
            Block retBlock = new Block(b);
            retBlock.setX(xPos);
            retBlock.setY(yPos);
            return retBlock;
        } else {
            return null;
        }
    }

    public void updateSize() {
        setFitWidth(GUI.getBlockSize());
        setFitHeight(GUI.getBlockSize());
    }

    public void updateSize(double blockSize) {
        setFitWidth(blockSize);
        setFitHeight(blockSize);
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public String getName() {
        return name;

    }

    public boolean getIsSolid() {
        return isSolid;
    }
    
    public String getBlocksDir()
    {
        return blocksDir;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() == this.getClass()) {
            Block b = (Block) o;
            if (b.getName().equals(this.getName())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
