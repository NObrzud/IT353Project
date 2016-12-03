/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.swing.ImageIcon;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Cedomir Spalevic
 */
@ManagedBean
public class ImageController 
{
    private double rating;
    private UploadedFile file;
    private ArrayList<String> images;
    
    public ImageController()
    {
        images = new ArrayList<String>();
    }
    
    public void upload(FileUploadEvent event)
    {
        try{
            String myDB = "jdbc:derby://localhost:1527/Project353";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            PreparedStatement ps;
            ps = connection.prepareStatement("INSERT INTO PHOTOS (NAME, CONTENT)" + "VALUES(?,?)");
            ps.setString(1, event.getFile().getFileName());
            Blob blob = connection.createBlob();
            blob.setBytes(1, event.getFile().getContentType().getBytes());
            ps.setBlob(2, blob);
            ps.execute();
            blob.free();
            ps.close();
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("You did well son");
    }

    @PostConstruct
    public void init()
    {
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
                //images.add(rs.getString("Names"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public void onrate()
    {
        
    }
    
    public void images()
    {
        
    }

    /**
     * @return the file
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    /**
     * @return the rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * @return the images
     */
    public ArrayList<String> getImages() {
        return images;
    }

    /**
     * @param images the images to set
     */
    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
