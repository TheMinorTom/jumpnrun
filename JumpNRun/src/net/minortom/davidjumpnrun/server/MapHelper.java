/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */

package net.minortom.davidjumpnrun.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class MapHelper {
    public static String[] listMaps(){
        throw new UnsupportedOperationException();
    }
    
    public static String getMapDesc(String map){
        throw new UnsupportedOperationException();
    }
    
    public static String getMapImgUrl(String map){
        throw new UnsupportedOperationException();
    }
    
    public static HashMap<String, Map> getMapCfgFile(){
        
        String everything = "";
        
        try(BufferedReader br = new BufferedReader(new FileReader(Server.server.getMapFolder() + "maps.jnrmapcfg"))) {
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
        
        throw new UnsupportedOperationException();
    }
}
