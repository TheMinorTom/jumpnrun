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
    OGAME_INITPENDANT,  //server          -
    OGAME_INITPROT,     //server          name, skin, index, id, is local player (0 = false, 1 = true)  
    OGAME_INITGAME,     //server          playerAmount, spawnY, gamemode, limit, gameName
    OGAME_UPDATEPROT,   //server          id, x, y, animationState (int)
    OGAME_KEYPRESS,     //client          playerId, gameName, action (RIGHT, LEFT, JUMP, HIT, SHOOT, USE)
    OGAME_KEYRELEASE;   //client          playerId, gameName, action

}