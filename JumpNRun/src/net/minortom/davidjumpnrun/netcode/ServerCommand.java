/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minortom.davidjumpnrun.netcode;

/**
 *
 * @author d.betko
 */
public enum ServerCommand {
                        //ISSUED BY       ARGS
    
    AUTH_REQ,           //client          user name, password
    AUTH_OK,            //server          user id, user token
    AUTH_LOGOUT,        //client          -
    AUTH_WRONGCREDS,     //server          -
    
    MAP_LISTREQ,        //client          -
    MAP_LISTOK,         //server          comma seperated map names
    
    OGAME_CREATE,       //client          name, max players, game mode, limit (if infinite 0), map, skin
    OGAME_JOIN,         //client          name, skin
    
    @Deprecated
    OGAME_YJOINED,      //server          name, max players, game mode, limit (if infinite 0), map, skin
    OGAME_PJOINED,      //server          pname, pubid, max players
    
    OGAME_WILLSTART,    //server          TODO
    OGAME_ERR,          //client          fatal, code
    OGAME_INITMAP,      //server          map
    @Deprecated
    OGAME_INITPENDANT,  //server          -
    OGAME_INITPROT,     //server          name, skin, index, pubId, objectId, is local player (0 = false, 1 = true), userId 
    OGAME_INITGAME,     //server          playerAmount, spawnY, gamemode, limit, gameName
    @Deprecated //use updateobject
    OGAME_UPDATEPROT,   //server          id, x, y, animationState (int)
    OGAME_KEYPRESS,     //client          playerId, gameName, action (RIGHT, LEFT, JUMP, HIT, SHOOT, USE)
    OGAME_KEYRELEASE,   //client          playerId, gameName, action
    OGAME_START,        //server          -
    @Deprecated
    OGAME_UPDATEOBJECT, //server          objectId, objectType(int), xPos, yPos, animationState(int)
    OGAME_UPDATEOBJECTS,//server          List of OGAME_UPDATEOBJECT's args; different objects seperated by NetworkManager.updateObjectsSeperator; different args per objet seperated by NetworkManager.subArgsSeperator
    OGAME_REMOVEOBJECT, //server          objectId
    OGAME_UPDATE_RESPAWNTIMER; //server   time

}
