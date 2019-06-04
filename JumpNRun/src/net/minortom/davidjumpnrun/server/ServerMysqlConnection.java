/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    
}
