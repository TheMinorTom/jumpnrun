/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldeditor;

import java.io.FileWriter;
import java.util.Vector;
import javafx.application.Application;
import javafx.scene.control.Alert;
import net.minortom.davidjumpnrun.configstore.ConfigManager;

/**
 *
 * @author User
 */
public class WorldEditor {

    public static final String configFolderName = "davidjumpnrun";
    public static final String blocksPath = "sprites/blocks/";
    public static Block[] blocks, strunz;
    FileWriter output;

    public static void setBlocks(Block[] b) {
        blocks = b;
    }


    public static void initBlocksArr() {
        blocks = new Block[]{new Block("Air", "", "", false),
            new Block("LEAVE_SOLID", blocksPath, "Leave.bmp", true),
            new Block("LEAVE_NONSOLID", blocksPath, "Leave.bmp", false),
            new Block("TREE_SOLID", blocksPath, "Tree.bmp", true),
            new Block("TREE_NONSOLID", blocksPath, "Tree.bmp", false),
            new Block("WATER_UP", blocksPath, "WaterUp.png", false),
            new Block("WATER", blocksPath, "Water.bmp", false),
            new Block("GRASS", blocksPath, "Grass.bmp", true),
            new Block("GRASS_NONSOLID", blocksPath, "Grass.bmp", false),
            new Block("DIRT_SOLID", blocksPath, "Dirt.bmp", true),
            new Block("DIRT_NONSOLID", blocksPath, "Dirt.bmp", false),
            new Block("STONE_SOLID", blocksPath, "Stone.bmp", true),
            new Block("STONE_NONSOLID", blocksPath, "Stone.bmp", false),
            new Block("STONE_WALL_SOLID", blocksPath, "StoneWall.bmp", true),
            new Block("STONE_WALL_NONSOLID", blocksPath, "StoneWall.bmp", false),
            new Block("METAL_SOLID", blocksPath, "Metal.bmp", true),
            new Block("METAL_NONSOLID", blocksPath, "Metal.bmp", false),
            new Block("SAND_SOLID", blocksPath, "Sand.bmp", true),
            new Block("SAND_NONSOLID", blocksPath, "Sand.bmp", false),
            new Block("MAGMA_ROCK_SOLID", blocksPath, "Magmarock.bmp", true),
            new Block("MAGMA_ROCK_NONSOLID", blocksPath, "Magmarock.bmp", false),
            new Block("BRICKS_SOLID", blocksPath, "Bricks.bmp", true),
            new Block("BRICKS_NONSOLID", blocksPath, "Bricks.bmp", false),
            new Block("ICE_SOLID", blocksPath, "Ice.bmp", true),
            new Block("ICE_NONSOLID", blocksPath, "Ice.bmp", false),
            new Block("RED_SOLID", blocksPath, "Red.png", true),
            new Block("RED_NONSOLID", blocksPath, "Red.png", false),
            new Block("BLUE_SOLID", blocksPath, "Blue.png", true),
            new Block("Blue_NONSOLID", blocksPath, "Blue.png", false),
            new Block("YELLOW_SOLID", blocksPath, "Yellow.png", true),
            new Block("YELLOW_NONSOLID", blocksPath, "Yellow.png", false),
            new Block("GREEN_SOLID", blocksPath, "Green.png", true),
            new Block("GREEN_NONSOLID", blocksPath, "Green.png", false),
            new Block("CHOCOLATE_SOLID", blocksPath, "Chocolate.png", true),
            new Block("CHOCOLATE_NONSOLID", blocksPath, "Chocolate.png", false),
       };
        
        strunz = new Block[]{
            new Block("STRUNZ_SOLID", blocksPath, "Strunz.bmp", true),
            new Block("STRUNZ_NONSOLID", blocksPath, "Strunz.bmp", false),
       };
    }
    
 

}
