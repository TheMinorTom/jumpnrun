/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMysqlConnection {
    
    String host, db, user, pass;
    Connection mysqlConn;
    
    ServerMysqlConnection(String host, String db, String user, String pass) {
        this.host = host;
        this.db = db;
        this.user = user;
        this.pass = pass;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        try {
            String jdbcString = "jdbc:mysql://" + host + "/" + db + "?user=" + user + "&password=" + pass + "&useSSL=false";
            System.out.println("JDBC String: " + jdbcString);
            mysqlConn = DriverManager.getConnection(jdbcString);
            
            System.out.println("Database connected");
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void updateStats (String id, int killsAdd, int deathsAdd, int winsAdd, int totalGamesPlayedAdd, int xpAdd, int coinsAdd, int scoreAdd) throws SQLException {
        Statement statement = mysqlConn.createStatement();
        String query = "SELECT * FROM jnr_playerstats WHERE id = " + id;
        ResultSet resultSet = statement.executeQuery(query);
        if(!resultSet.next()) {
            addPlayerStatsEntry(id);
            resultSet = statement.executeQuery(query);
            resultSet.next();
        }
        int kills = resultSet.getInt("Kills");
        kills += killsAdd;
        
        int deaths = resultSet.getInt("Deaths");
        deaths += deathsAdd;
        
        int wins = resultSet.getInt("Wins");
        wins += winsAdd;
        
        int totalGamesPlayed = resultSet.getInt("TotalGamesPlayed");
        totalGamesPlayed += totalGamesPlayedAdd;
        
        int xp = resultSet.getInt("XP");
        xp += xpAdd;
        
        int coins = resultSet.getInt("Coins");
        coins += coinsAdd;
        
        int score = resultSet.getInt("Score");
        score += scoreAdd;
        
        double kd = 0;
        if(deaths != 0) {
            kd = kills / deaths;
        }
        
        double winRatio = wins / totalGamesPlayed;
        
        query = "UPDATE jnr_playerstats SET "
                + "Kills = " + kills
                + ", Deaths = " + deaths
                + ", KD = " + kd
                + ", Wins = " + wins
                + ", WinRatio = " + winRatio
                + ", TotalGamesPlayed = " + totalGamesPlayed
                + ", XP = " + xp
                + ", Coins = " + coins
                + ", Score = " + score
                + " WHERE id = " + id;
        statement.execute(query);
    }
    
    private void addPlayerStatsEntry(String playerId) throws SQLException {
        String sqlString = "INSERT INTO jnr_playerstats (id) VALUES ('" + playerId + "');";
        Statement statement = mysqlConn.createStatement();
        statement.execute(sqlString);
    }
    
    public int getScore (String userId) throws SQLException  {
        Statement statement = mysqlConn.createStatement();
        String query = "SELECT Score FROM jnr_playerstats WHERE id = " + userId;
        ResultSet result = statement.executeQuery(query);
        if(!result.next()) {
            addPlayerStatsEntry(userId);
            result = statement.executeQuery(query);
            result.next();
        }
        return result.getInt("Score");
    }
    
}
