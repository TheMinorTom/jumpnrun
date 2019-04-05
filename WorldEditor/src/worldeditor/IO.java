/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldeditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author Norbert
 */
public class IO {

    private static FileWriter output;
    private static FileReader input;
    private static final char nameUrlSolSep = ',';
    private static final char blockSep = ';';
    private static final char colSep = '|';

    /*
    public static void saveWorld(Vector<Vector<Block>> world, File file) {
        try {
            output = new FileWriter(file);
            String worldInString = "";
            for (int i = 0; i < world.size(); i++) {
                for (int j = 0; j < world.get(i).size(); j++) {
                    if (world.get(i).get(j) != null) {
                        worldInString += world.get(i).get(j).getName() + nameUrlSolSep + world.get(i).get(j).getImageFileName();
                    }
                    worldInString += blockSep;
                }
                worldInString += colSep;
            }
            output.append(worldInString);
            output.close();

        } catch (IOException ex) {

        }
    }
     */
    public static void saveWorld(Vector<Vector<Block>> world, File file, double blockSize) {
        try {
            output = new FileWriter(file);
            String worldInString = "";
            output.append(Integer.toString((int) blockSize) + "B");
            for (int i = 0; i < world.size(); i++) {
                worldInString += colSep;
                for (int j = 0; j < world.get(i).size(); j++) {
                    worldInString += blockSep;
                    if (world.get(i).get(j) != null) {
                        int solInt = world.get(i).get(j).getIsSolid() ? 1 : 0;
                        String name = world.get(i).get(j).getName();
                        String imageFileName = world.get(i).get(j).getImageFileName();

                        worldInString += name + nameUrlSolSep + imageFileName + nameUrlSolSep + solInt;
                    }

                }

            }
            output.append(worldInString);
            output.close();

        } catch (IOException ex) {

        }
    }
    
    public static Vector<Vector<Block>> openWorld(String worldString, String blockDir) {
        Vector<Vector<Block>> retVec = new Vector();
        Vector<Block> blocks = new Vector<Block>();
        
        String[] cols = worldString.split("\\"+String.valueOf(colSep));
        String blockSizeString = cols[0].substring(0, cols[0].length()-1);
        double blockSize = Double.parseDouble(blockSizeString);
        
        String currCol;
        String currBlock;
        String[] blocksInCol;
        String[] dataInBlock;
        
        int xIndex = 0;
        int yIndex = 0;
        
        boolean addIsSolid = true;
        String addName;
        String addFileName;
        Block addBlock;
        for(int i = 1; i < cols.length; i++) {
            currCol = cols[i];
            blocksInCol = currCol.split("\\"+String.valueOf(blockSep));
            retVec.add(new Vector<Block>());
            for(int j = 0; j < blocksInCol.length; j++) {
                currBlock = blocksInCol[j];
                dataInBlock = currBlock.split("\\"+String.valueOf(nameUrlSolSep));
                if(dataInBlock.length == 3) {
                    retVec.get(xIndex).add(null);
                    
                    addName = dataInBlock[0];
                    addFileName = dataInBlock[1];
                    try {
                    addIsSolid = (Integer.parseInt(dataInBlock[2]) == 1) ? true : false;
                    } catch(NumberFormatException e) {
                        System.err.println(currBlock);
                    }
                    addBlock = new Block(addName, blockDir, addFileName, addIsSolid);
                    
                    if(!blocks.contains(addBlock)) {
                        blocks.add(addBlock);
                    }
                    
                    addBlock.setFitWidth(blockSize);
                    addBlock.setFitHeight(blockSize);
                    retVec.get(xIndex).set(yIndex, addBlock);
                    yIndex++;
                }
            }
            yIndex = 0;
            xIndex++;
        }
        
        Block[] blocksAsArr = new Block[blocks.size()];
        for(int i = 0; i < blocks.size(); i++) {
            blocksAsArr[i] = blocks.get(i);
        }
        WorldEditor.setBlocks(blocksAsArr);
        return retVec;
    }

    public static Vector<Vector<Block>> openWorld(File file) {
        Vector<Vector<Block>> world = new Vector<Vector<Block>>();
        try {

            input = new FileReader(file);
            double blockSize = 0;
            String blockSizeStr = "";
            int countX = -1;
            int countY = -1;
            Vector<Block> blocks = new Vector<Block>();
            boolean cancel = false;
            for (char c = (char) input.read(); c != 'B'; c = (char) input.read()) {
                blockSizeStr += Integer.parseInt(Character.toString(c));
            }
            blockSize = Double.parseDouble(blockSizeStr);
            while (!cancel) {
                int inputCharInt = input.read();
                if (inputCharInt == -1) {
                    cancel = true;
                } else {
                    char currentChar = (char) inputCharInt;
                    if (currentChar == blockSep) {
                        countY++;
                        world.get(countX).add(null);

                    } else if (currentChar == colSep) {
                        countX++;
                        countY = -1;
                        world.add(new Vector<Block>());

                    } else {
                        String blockName = "";
                        String blockFileName = "";
                        while (currentChar != nameUrlSolSep) {

                            blockName += currentChar;
                            currentChar = (char) input.read();

                        }
                        int temp = input.read();
                        currentChar = (char) temp;
                        while ((currentChar != blockSep) && (currentChar != colSep) && (currentChar != nameUrlSolSep)) {

                            if (temp != -1) {

                                blockFileName += currentChar;
                                temp = input.read();
                                currentChar = (char) temp;
                            } else {
                                currentChar = blockSep;
                            }
                        }

                        boolean addIsSolid;
                        temp = input.read();
                        currentChar = (char) temp;
                        if (currentChar == '0') {
                            addIsSolid = false;
                        } else {
                            addIsSolid = true;
                        }
                        boolean exists = false;
                        Block additBlock = new Block(blockName, WorldEditor.blocksPath, blockFileName, addIsSolid);
                        if (blocks.contains(additBlock)) {
                            exists = true;
                        } else {
                            blocks.add(additBlock);
                        }
                        additBlock.setFitWidth(blockSize);
                        additBlock.setFitHeight(blockSize);
                        world.get(countX).set(countY, additBlock);
                        currentChar = (char) input.read();
                        if (currentChar == blockSep) {
                            countY++;
                            world.get(countX).add(null);
                        } else {
                            countX++;
                            countY = -1;
                            world.add(new Vector<Block>());
                        }

                    }
                }

            }
            Object[] BlocksObjArr = blocks.toArray();
            Block[] BlocksArr = new Block[BlocksObjArr.length];
            for (int i = 0; i < BlocksObjArr.length; i++) {
                BlocksArr[i] = (Block) BlocksObjArr[i];
            }
            WorldEditor.setBlocks(BlocksArr);

        } catch (IOException ex) {

        }

        return world;
    }

    public static Vector<Vector<Block>> openWorld(InputStream input, String blocksDirPath) {
        Vector<Vector<Block>> world = new Vector<Vector<Block>>();
        try {

            double blockSize = 0;
            String blockSizeStr = "";
            int countX = -1;
            int countY = -1;
            Vector<Block> blocks = new Vector<Block>();
            boolean cancel = false;
            for (char c = (char) input.read(); c != 'B'; c = (char) input.read()) {
                blockSizeStr += Character.toString(c);
                }
            blockSize = Double.parseDouble(blockSizeStr);
            while (!cancel) {
                int inputCharInt = input.read();
                if (inputCharInt == -1) {
                    cancel = true;
                } else {
                    char currentChar = (char) inputCharInt;
                    if (currentChar == blockSep) {
                        countY++;
                        world.get(countX).add(null);

                    } else if (currentChar == colSep) {
                        countX++;
                        countY = -1;
                        world.add(new Vector<Block>());

                    } else {
                        String blockName = "";
                        String blockUrl = "";
                        while (currentChar != nameUrlSolSep) {

                            blockName += currentChar;
                            currentChar = (char) input.read();

                        }
                        int temp = input.read();
                        currentChar = (char) temp;
                        while ((currentChar != blockSep) && (currentChar != colSep) && (currentChar != nameUrlSolSep)) {

                            if (temp != -1) {

                                blockUrl += currentChar;
                                temp = input.read();
                                currentChar = (char) temp;
                            } else {
                                currentChar = blockSep;
                            }
                        }

                        boolean addIsSolid;
                        temp = input.read();
                        currentChar = (char) temp;
                        if (currentChar == '0') {
                            addIsSolid = false;
                        } else {
                            addIsSolid = true;
                        }

                        Block additBlock = new Block(blockName, blocksDirPath, blockUrl, addIsSolid);  //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        if (blocks.contains(additBlock)) {

                        } else {
                            blocks.add(additBlock);
                        }
                        additBlock.setFitWidth(blockSize);
                        additBlock.setFitHeight(blockSize);

                        world.get(countX).set(countY, additBlock);
                        currentChar = (char) input.read();
                        if (currentChar == blockSep) {
                            countY++;
                            world.get(countX).add(null);
                        } else {
                            countX++;
                            countY = -1;
                            world.add(new Vector<Block>());
                        }

                    }
                }

            }
            Object[] BlocksObjArr = blocks.toArray();
            Block[] BlocksArr = new Block[BlocksObjArr.length];
            for (int i = 0; i < BlocksObjArr.length; i++) {
                BlocksArr[i] = (Block) BlocksObjArr[i];
            }
            WorldEditor.setBlocks(BlocksArr);

        } catch (IOException ex) {

        }

        return world;
    }
    
    public static InputStream getFileStream(String uri) {

        try {
            InputStream in = new FileInputStream(uri);
            return in;
        } catch(FileNotFoundException e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Game");
            alert.setHeaderText("Error");
            alert.setContentText("File at " + uri + " not found. This could be because the program was incorrectly installed.");
            alert.setResizable(true);
            alert.getDialogPane().setPrefSize(480, 320);
            alert.showAndWait();
            return null;
        }
    }

}
