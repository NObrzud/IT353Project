/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Account;
import Model.AdminCart;
import Model.Cart;
import Model.Image;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import org.primefaces.event.FileUploadEvent;

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
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/NickCharlieMattProject";
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
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/NickCharlieMattProject";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            String sql = "select * from Account";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String fn = rs.getString("FIRSTNAME");
                String ln = rs.getString("LASTNAME");
                String e = rs.getString("EMAIL");
                String p = rs.getString("PASSWORD");
                int admin = rs.getInt("ADMIN");
                if (username.equals(e)) {
                    if (!password.equals(p)) {
                        return 0;
                    } else if (admin == 1) {
                        account.setEmail(e);
                        account.setFirstName(fn);
                        account.setLastName(ln);
                        account.setIsAdmin(admin);
                        account.setPassword(password);
                        return 2;
                    } else {
                        account.setEmail(e);
                        account.setFirstName(fn);
                        account.setLastName(ln);
                        account.setIsAdmin(admin);
                        account.setPassword(password);
                        return 1;
                    }
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
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/NickCharlieMattProject";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            Statement st = connection.createStatement();
            String sql = "INSERT INTO Account VALUES ('"
                    + account.getFirstName() + "','" + account.getLastName() + "','"
                    + account.getEmail() + "','" + account.getPassword() + "', "
                    + "0" + ")";
            st.executeUpdate(sql);
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return 0;
        }
        return 1;
    }

    public int changePassword(Account account) {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            return 0;
        }

        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/NickCharlieMattProject";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            Statement st = connection.createStatement();
            String sql = "UPDATE ACCOUNT SET password = '"
                    + account.getPassword()
                    + "' WHERE email = '"
                    + account.getEmail() + "'";
            st.executeUpdate(sql);
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return 0;
        }
        return 1;
    }

    public int uploadImage(FileUploadEvent event, Account account, double price) {
        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/NickCharlieMattProject";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            String sql = "insert into Photos (FILENAME, EMAIL, RATING, TOTAL, IMAGECONTENT, SUBMISSIONDATE, PRICE, WINNER)";
            String values = "values (?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql + values);
            ps.setString(1, event.getFile().getFileName());
            ps.setString(2, account.getEmail());
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setBytes(5, event.getFile().getContents());
            ps.setDate(6, new Date(Calendar.getInstance().getTime().getTime()));
            ps.setDouble(7, price);
            ps.setInt(8, 0);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return 0;
        }
        return 1;
    }
    
    public void updateWinner(String filename){
        try
        {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/NickCharlieMattProject";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            String sql = "UPDATE PHOTOS SET "
                    + "WINNER = " + 1 + " "
                    + "WHERE FILENAME = '" + filename+"'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.execute();
            ps.close();
        } 
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }

    public ArrayList<Image> getImages(String query) {
        Image img;
        ArrayList<Image> imagecollection = new ArrayList<Image>();
        Connection DBConn = null;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/NickCharlieMattProject";
            DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            Statement stmt = DBConn.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            String filename, email;
            int photoid, rating, total, winner;
            byte[] content;
            Date submitted;
            double price;
            while (rs.next()) {
                photoid = rs.getInt("PHOTOID");
                filename = rs.getString("FILENAME");
                email = rs.getString("EMAIL");
                content = rs.getBytes("IMAGECONTENT");
                rating = rs.getInt("RATING");
                total = rs.getInt("TOTAL");
                submitted = rs.getDate("SUBMISSIONDATE");
                price = rs.getDouble("PRICE");
                winner = rs.getInt("WINNER");
                img = new Image(photoid, filename, email, content, rating, total, submitted, price, winner);
                imagecollection.add(img);
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
        return imagecollection;
    }
    
    public byte[] getSingleImage(String id) {
        byte[] img = null;
        Connection DBConn = null;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/NickCharlieMattProject";
            DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            PreparedStatement stmt;// = DBConn.createStatement();

            stmt = DBConn.prepareStatement("SELECT * FROM PHOTOS WHERE photoid=?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                img = rs.getBytes("imagecontent");
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
        return img;
    }
    
    public void updateProfile(Account account)
    {
        try
        {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/NickCharlieMattProject";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            String sql = "UPDATE ACCOUNT SET "
                    + "FIRSTNAME = '"+ account.getFirstName() + "', "
                    + "LASTNAME = '" + account.getLastName() + "', "
                    + "PASSWORD = '" + account.getPassword() +"' "
                    + "WHERE EMAIL = '" + account.getEmail()+"'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.execute();
            ps.close();
        } 
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }
    
    public void updateRating(String fn, int rating)
    {
        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/NickCharlieMattProject";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            String sql = "UPDATE PHOTOS SET RATING = RATING+" + rating
                    + ", TOTAL = TOTAL+" + 5 + " WHERE FILENAME = '" + fn+"'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public void addToCart(String filename, String email, double price)
    {
        try{
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/NickCharlieMattProject";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            String sql = "INSERT INTO CART (EMAIL, NAME, PRICE) VALUES (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, filename);
            ps.setDouble(3, price);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public ArrayList<Cart> getCart()
    {
        ArrayList<Cart> c = new ArrayList<Cart>();
        Connection DBConn = null;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/NickCharlieMattProject";
            DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            Statement stmt = DBConn.createStatement();
            String query = "SELECT * FROM CART";
            ResultSet rs = stmt.executeQuery(query);
            String name, email;
            double price;
            while (rs.next()) {
                name = rs.getString("NAME");
                email = rs.getString("EMAIL");
                price = rs.getDouble("PRICE");
                c.add(new Cart(email, name, price));
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
        return c;
    }
    
    public void emptyCart()
    {
        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/NickCharlieMattProject";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            String sql = "DELETE FROM CART";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    
        public void addToAdminCart(String accEmail, String filename, String email, double price, String type)
    {
        try{
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/NickCharlieMattProject";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            String sql = "INSERT INTO ADMINCART (BUYER, EMAIL, NAME, PRICE, TYPE) VALUES (?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, accEmail);
            ps.setString(2, email);
            ps.setString(3, filename);
            ps.setDouble(4, price);
            ps.setString(5, type);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
        
         public ArrayList<AdminCart> getAdminCart()
    {
        ArrayList<AdminCart> ac = new ArrayList<AdminCart>();
        Connection DBConn = null;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/NickCharlieMattProject";
            DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            Statement stmt = DBConn.createStatement();
            String query = "SELECT * FROM ADMINCART";
            ResultSet rs = stmt.executeQuery(query);
            String name, email, buyer, type;
            double price;
            while (rs.next()) {
                name = rs.getString("NAME");
                email = rs.getString("EMAIL");
                price = rs.getDouble("PRICE");
                buyer = rs.getString("BUYER");
                type = rs.getString("TYPE");
                ac.add(new AdminCart(buyer, email, name, price, type));
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
        return ac;
    }
         
         public void emptyAdminCart()
    {
        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/NickCharlieMattProject";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            String sql = "DELETE FROM ADMINCART";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
