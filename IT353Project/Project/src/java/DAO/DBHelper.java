/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Nick
 */
public class DBHelper {
        /** Creates a new instance of DBHelper */
    public DBHelper() {
    }
    
    public static void loadDriver(String driverStr) {
        try {
            Class.forName(driverStr);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static Connection connect2DB(String connectStr, String userName, String password) {
        
        String myDB = connectStr;
        Connection DBConn = null;
        try {
            DBConn = DriverManager.getConnection(myDB, userName, password);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return DBConn;
    }

}
