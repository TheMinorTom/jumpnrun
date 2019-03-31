/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */

package net.minortom.davidjumpnrun.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;

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
        
        try(BufferedReader br = new BufferedReader(new FileReader(Server.getMapFolder() + "maps.jnrmapcfg"))) {
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
            retMap.put(tempArrM[1], new Map(tempArrM[0],tempArrM[1],tempArrM[2],tempArrM[3]));
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
}
