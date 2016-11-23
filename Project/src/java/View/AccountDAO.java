/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;
import Model.Account;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author it353F620
 */
public class AccountDAO 
{
    public int login(Account account)
    {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        String username = account.getUsername();
        String password = account.getPassword();
        int a = 0;
        try
        {
            String myDB = "jdbc:derby://localhost:1527/Project353";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            String sql = "select * from Account";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                String u = rs.getString("USERNAME");
                String p = rs.getString("PASSWORD");
                if(!username.equals(u)){}
                else
                {
                    if(!password.equals(p)) return 0; 
                    else return 1;
                }
            }
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
        return a;
    }
    
    public int register(Account account)
    {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            return 0;
        }
       
       try
        {
            String myDB = "jdbc:derby://localhost:1527/Project353";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            Statement st = connection.createStatement();
            String sql = "INSERT INTO Account VALUES ('"
                    + account.getFirstName() + "','" + account.getLastName() + "','"
                    + account.getUsername() + "','" + account.getPassword() + "','"
                    + account.getEmail() + "');";
            System.out.println(sql);
            st.executeUpdate(sql);
            connection.close();
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
            return 0;
        }
        return 1;
    }
    
}
