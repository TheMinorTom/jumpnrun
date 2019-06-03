/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasecode;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author DavidPrivat
 */
public class DatabaseManager {

    private Connection connection;
    private String connectionUrl =
                "jdbc:sqlserver://v1.api.minortom.net;"
                        //+ "databaseName=ni688978_2sql1;"
                        + "user=ni688978_2sql1;"
                        + "password=ef9baaed;";
  

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
