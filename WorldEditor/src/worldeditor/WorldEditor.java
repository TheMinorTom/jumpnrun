/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldeditor;

import java.io.FileWriter;
import java.util.Vector;

/**
 *
 * @author User
 */
public class WorldEditor {
    public final static String blocksPath = "file:src/blocks/";
    public static Block[] blocks = new Block[]{new Block("Air", "" , "" ,false),
        new Block("LEAVE_SOLID", blocksPath, "Leave.bmp", true),
        new Block("LEAVE_NONSOLID", blocksPath, "Leave.bmp", false),
        new Block("TREE_SOLID", blocksPath, "Tree.bmp", true),
        new Block("TREE_NONSOLID", blocksPath, "Tree.bmp", false),
        new Block("WATER_UP", blocksPath, "WaterUp.png", false),
        new Block("WATER", blocksPath, "Water.bmp", false),
        new Block("GRASS", blocksPath, "Grass.bmp", true),
        new Block("DIRT_SOLID", blocksPath, "Dirt.bmp", true),
        new Block("DIRT_NONSOLID", blocksPath,"Dirt.bmp", false)
    };
    private static GUI gui;
    FileWriter output;
   
    
    public static void main (String ... args)
    {
        gui = new GUI();
        System.out.println("Hallo");
        gui.launch(args);
    }


    
    public static void setBlocks(Block[] b)
    {
        blocks = b;
    }
    
    
    
    
    
}
