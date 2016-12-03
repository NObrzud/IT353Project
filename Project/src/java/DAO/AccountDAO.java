/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Account;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author it353F620
 */
public class AccountDAO {

    public ArrayList findByAccountEmail(String email) {
        String query = "SELECT * FROM Account ";
        query += "WHERE email = '" + email + "'";

        ArrayList aProfileCollection = selectAccountsFromDB(query);
        return aProfileCollection;
    }

    private ArrayList selectAccountsFromDB(String query) {
       ArrayList aUserCollection = new ArrayList();
        Connection DBConn = null;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            String myDB = "jdbc:derby://localhost:1527/Project353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");

            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String first, last, email, pass;
            Account account;
            while (rs.next()) {
                first = rs.getString("FirstName");
                last = rs.getString("LastName");
                pass = rs.getString("Password");
                email = rs.getString("Email");

                account = new Account(first, last, email, pass);
                aUserCollection.add(account);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println("ERROR: Problems with SQL select");
            e.printStackTrace();
        }
        try {
            DBConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aUserCollection;
    }

    public int login(Account account) {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        String username = account.getEmail();
        String password = account.getPassword();
        int a = 0;
        try {
            String myDB = "jdbc:derby://localhost:1527/Project353";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            String sql = "select * from Account";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String u = rs.getString("EMAIL");
                String p = rs.getString("PASSWORD");
                String admin = rs.getString("ADMIN");
                if(username.equals(u))
                {
                    if (!password.equals(p)) return 0;
                    else if(admin.equals("1")) return 2;
                    else return 1;
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return a;
    }

    public int register(Account account) {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            return 0;
        }

        try {
            String myDB = "jdbc:derby://localhost:1527/Project353";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            Statement st = connection.createStatement();
            String sql = "INSERT INTO Account VALUES ('"
                    + account.getFirstName() + "','" + account.getLastName() + "','"
                    + account.getEmail() + "','" + account.getPassword() + "', "
                    + "0" + ")";
            System.out.println(sql);
            st.executeUpdate(sql);
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return 0;
        }
        return 1;
    }

}
