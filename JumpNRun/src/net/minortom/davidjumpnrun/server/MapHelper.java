/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */

package net.minortom.davidjumpnrun.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import net.minortom.davidjumpnrun.configstore.ConfigManager;

public class MapHelper {
    public static String[] listMaps(){
        HashMap<String, Map> maps = getMapCfgFile();
        String[] ret = new String[maps.size()];
        
        int i = 0;
        for(java.util.Map.Entry<String, Map> oneMap : maps.entrySet()) {
            ret[i] = oneMap.getValue().name;
            i++;
        }
        
        System.out.println(Arrays.toString(ret));
        return ret;
    }
    
    public static String getMapDesc(String map){
        throw new UnsupportedOperationException();
    }
    
    public static String getMapImgUrl(String map){
        throw new UnsupportedOperationException();
    }
    
    public static HashMap<String, Map> getMapCfgFile(){
        
        String everything = "";
        String mapConfigLocation = Server.getMapFolder() + "maps.jnrmapcfg";
        System.out.println("Map config stored at: " + mapConfigLocation);
        try(BufferedReader br = new BufferedReader(new FileReader(mapConfigLocation))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            
            
            
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (Exception e) {
            
        }
        
        
        String[] tempArr = everything.split("\n");
        HashMap<String, Map> retMap = new HashMap<>();
        
        for (String currMap : tempArr){
            String[] tempArrM = currMap.split(";");
            try {
                retMap.put(tempArrM[1], new Map(tempArrM[0],tempArrM[1],tempArrM[2],tempArrM[3]));
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        
        return retMap;
    }

    static String getMap(String mapName) {
        String everything = "";
        
        try(BufferedReader br = new BufferedReader(new FileReader(Server.server.getMapFolder() + mapName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            
            
            
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (Exception e) {
            
        }
        
        return everything;
    }
    
    public static String getMapAbsPath(String mapPath) {
        String everything = "";
        
        try(BufferedReader br = new BufferedReader(new FileReader(mapPath))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            
            
            
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (Exception e) {
            
        }
        
        return everything;
    }
}
