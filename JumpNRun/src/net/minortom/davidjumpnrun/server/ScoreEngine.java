/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minortom.davidjumpnrun.server;

/**
 *
 * @author DavidPrivat
 */
public class ScoreEngine {
    
    
    public static int calculateScore(int placement, int playerAmount, String userId) {
        int scoreAdd = (int)((playerAmount / placement) * 30) - (playerAmount * 20);
        return scoreAdd;
    }
    
    public static int calculateXP(int kills, int deaths, int placement, int playerAmount) {
        int xp = 0;
        xp += kills *5;
        xp += deaths;
        xp += (int)((playerAmount/placement)*2);
        xp += 5;
        return xp;
    }
}
