/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Cedomir Spalevic
 */
@ManagedBean
public class ImageController 
{
    private List<String> images;

    @PostConstruct
    public void init()
    {
        images = new ArrayList<String>();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

        try {
            String myDB = "jdbc:derby://localhost:1527/Project353";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            Statement st = connection.createStatement();
            String sql = "SELECT * FROM Photos";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                images.add(rs.getString("Names"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * @return the images
     */
    public List<String> getImages() {
        return images;
    }

    /**
     * @param images the images to set
     */
    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
