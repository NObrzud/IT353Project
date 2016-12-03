/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Account;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Cedomir Spalevic
 */
@ManagedBean
@SessionScoped
public class ImageController 
{
    private double rating;
    private UploadedFile file;
    private ArrayList<String> images;
    private Account account;
    
    public ImageController()
    {
        images = new ArrayList<String>();
        //account = (Account) FacesContext.getCurrentInstance().getClass(Account.class);
    }
    
    public void upload(FileUploadEvent event)
    {
        try{
            String myDB = "jdbc:derby://localhost:1527/Project353";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            String sql = "insert into Photos (FILENAME, EMAIL, RATING, TOTAL, IMAGECONTENT, SUBMISSIONDATE)";
            String values = "values (?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql + values);
            ps.setString(1, event.getFile().getFileName());
            ps.setString(2, getAccount().getEmail());
            ps.setInt(3, 0);
            ps.setInt(4, 5);
            Blob blob = connection.createBlob();
            blob.setBytes(1, event.getFile().getContentType().getBytes());
            ps.setBlob(5, blob);
            ps.setDate(6, new Date(Calendar.getInstance().getTime().getTime()));
            ps.execute();
            blob.free();
            ps.close();
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @PostConstruct
    public void getPhotos()
    {
        try{
            String myDB = "jdbc:derby://localhost:1527/Project353";
            Connection connection = DriverManager.getConnection(myDB, "itkstu", "student");
            String sql = "select * from Photos";
        }
        catch (SQLException e) {
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

    /**
     * @return the account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(Account account) {
        this.account = account;
    }
}
