/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.AccountDAO;
import Model.Image;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Nick
 */
@ManagedBean
@ViewScoped
public class PhotoController {

    private ArrayList<Image> imageArr;
    private ArrayList<String> imageNamesArr;
    private ArrayList<Image> winnerArr;
    private WeekController wc;
    
    public PhotoController(){
        wc = new WeekController();
        getImagesFromDB();
        getWinnerImagesFromDB();
        imageNamesArr = new ArrayList<String>();
        for(int i=0; i<imageArr.size(); i++){
            imageNamesArr.add(imageArr.get(i).getFilename());
        }
    }
    public void getImagesFromDB(){
        AccountDAO dao = new AccountDAO();
        java.sql.Date d1 = new java.sql.Date(wc.getStartDate().getTime());
        java.sql.Date d2 = new java.sql.Date(wc.getEndDate().getTime());
        String query = "SELECT * FROM PHOTOS WHERE SUBMISSIONDATE BETWEEN '"+d1+"' AND '"+d2+"'";
        imageArr = dao.getImages(query);
    }
    public void getWinnerImagesFromDB(){
        AccountDAO dao = new AccountDAO();
        String query = "SELECT * FROM PHOTOS WHERE WINNER = 1";
        winnerArr = dao.getImages(query);
    }
    
    
    public ArrayList<Image> getWinnerArr() {
        return winnerArr;
    }

    public void setWinnerArr(ArrayList<Image> winnerArr) {
        this.winnerArr = winnerArr;
    }

    public ArrayList<String> getImageNamesArr() {
        return imageNamesArr;
    }

    public void setImageNamesArr(ArrayList<String> imageNamesArr) {
        this.imageNamesArr = imageNamesArr;
    }
    public ArrayList<Image> getImageArr() {
        return imageArr;
    }

    public void setImageArr(ArrayList<Image> imageArr) {
        this.imageArr = imageArr;
    }

}
