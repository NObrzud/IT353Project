
package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class WeekDAO {
    public ArrayList<java.util.Date> getWeek(){
        ArrayList<java.util.Date> list = new ArrayList<java.util.Date>();
        java.util.Date s = new java.util.Date();
        java.util.Date end = new java.util.Date();
        Connection DBConn = null;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            String myDB = "jdbc:derby://localhost:1527/Project353";
            DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            Statement stmt = DBConn.createStatement();
            String query = "SELECT * FROM WEEK";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                s = rs.getTimestamp("STARTDATE");
                end = rs.getTimestamp("ENDDATE");
                list.add(s);
                list.add(end);
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
        return list;
    }
    public void setNewWeek(Date start, Date end){
        try
        {
            String myDB = "jdbc:derby://localhost:1527/Project353";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            String query = "UPDATE WEEK SET STARTDATE='"
                    + start + "', "
                    + "ENDDATE='" + end +"'";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            ps.close();
        } 
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
