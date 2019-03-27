/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

public class Map {
   public String fileName;
   public String name;
   public String desc;
   public String imgUrl;
   
   public Map(String fileName, String name, String desc, String imgUrl){
       this.fileName = fileName;
       this.name = name;
       this.desc = desc;
       this.imgUrl = imgUrl;
   }
}
