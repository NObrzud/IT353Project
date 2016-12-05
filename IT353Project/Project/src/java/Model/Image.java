/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import DAO.AccountDAO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Nick
 */
public class Image {

    private int photoid;
    private String filename;
    private String email;
    private byte[] content;
    private int rating;
    private int total;
    private Date submitted;
    private double price;
    private StreamedContent streamContent;

    public Image() {

    }

    public Image(int photoid, String filename, String email, byte[] content, int rating, int total, Date submitted, double price) {
        this.photoid = photoid;
        this.filename = filename;
        this.email = email;
        this.content = content;
        this.rating = rating;
        this.total = total;
        this.submitted = submitted;
        this.price = price;
        this.streamContent = getStreamContent();
    }

    public int getPhotoid() {
        return photoid;
    }

    public void setPhotoid(int photoid) {
        this.photoid = photoid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getRating() {
        
        double temp;
        if(total != 0){
            temp = (double)(((double)rating)/((double)total)*5);
        }
        else temp = 0.0;
        return (int)temp;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Date getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Date submitted) {
        this.submitted = submitted;
    }

    public StreamedContent getStreamContent() {
        AccountDAO dao = new AccountDAO();
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {

            String id = context.getExternalContext().getRequestParameterMap()
                    .get("pid");

            byte[] image = dao.getSingleImage(id);

            return new DefaultStreamedContent(new ByteArrayInputStream(image));

        }
        /* InputStream is = new ByteArrayInputStream(content);
        StreamedContent sc = new DefaultStreamedContent(is);
        return sc;*/
    }

    public void setStreamContent(StreamedContent streamContent) {
        this.streamContent = streamContent;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
