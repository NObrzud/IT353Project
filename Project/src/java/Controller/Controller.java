/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import Model.Account;
import DAO.AccountDAO;
import java.util.ArrayList;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author it353F620
 */
@ManagedBean(eager=false)
@SessionScoped
public class Controller {

    private Account account;
    private UploadedFile file;
    private double rating;

    public Controller() {
        account = new Account();
    }
    
    //This method calls the method to sign you in
    public String signIn() {
        AccountDAO ad = new AccountDAO();
        int x = ad.login(account);
        if (x == 1) {
            return "dashboard.xhtml";
        } else if (x == 2) {
            return "a_dashboard.xhtml";
        } else {
            return "login.xhtml";
        }
    }

    //This method calls the method to register you into the DB
    public String authenticate() {
        //Making sure all values have been inputed
        if (account.getFirstName().equals("")) {
            return "register.xhtml";
        }
        if (account.getLastName().equals("")) {
            return "register.xhtml";
        }
        if (account.getEmail().equals("")) {
            return "register.xhtml";
        }
        if (account.getPassword().equals("")) {
            return "register.xhtml";
        }
        if (account.getConfirmPass().equals("")) {
            return "register.xhtml";
        }
        //Making sure password and confirm password are the same
        if (!account.getPassword().equals(account.getConfirmPass())) {
            return "register.xhtml";
        }
        //Register
        AccountDAO ad = new AccountDAO();
        if (ad.register(account) == 1) {
            return "index.xhtml";
        } else {
            return "register.xhtml";
        }
    }

    public void upload(FileUploadEvent event) {
        AccountDAO ad = new AccountDAO();
        int x = ad.uploadImage(event, account);
    }

    /**
     * checks if username is already DB and notifies using AJAX
     */
    public String checkValidEmail() {
        AccountDAO accDao = new AccountDAO();
        ArrayList accCollection = accDao.findByAccountEmail(account.getEmail());
        if (!accCollection.isEmpty()) {
            return "<span style=\"color:red\">Email already used!</span>";
        } else {
            return "";
        }
    }
    
    public String checkMatchingPasswords(){
        if(account.getPassword().equals(account.getConfirmPass())){
            return "";
        }
        else{
            return "<span style=\"color:red\">Passwords don't match!</span>";
        }
    }

    public String checkAccountInfo() {
        AccountDAO accDao = new AccountDAO();
        ArrayList accCollection = accDao.findByAccountEmail(account.getEmail());
        if (accCollection.isEmpty()) {
            return "error.xhtml";
        } else {
            Account a = (Account) accCollection.get(0);
            if (account.getFirstName().toUpperCase().equals(a.getFirstName().toUpperCase())
                    && account.getLastName().toUpperCase().equals(a.getLastName().toUpperCase())
                    && account.getEmail().toUpperCase().equals(a.getEmail().toUpperCase())) {
                return "changepassword.xhtml";
            } else {
                return "index.xhtml";
            }
        }
    }

    public String changePassword() {
        AccountDAO accDao = new AccountDAO();
        accDao.changePassword(account);
       // FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        //FacesContext.getCurrentInstance().addMessage(null, message);
        return "index.xhtml";
    }

    /*
    * sends email
     */
    public void sendEmail(String recipient, String sender, String subject, String content) {
        String to = recipient;
        String from = sender;
        String host = "smtp.gmail.com";
        String username = "";
        String password = "";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.user", username);
        properties.put("mail.smtp.password", password);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Message + Img part 
            MimeMultipart multipart = new MimeMultipart("related");

            BodyPart messageBodyPart = new MimeBodyPart();

            // Send the actual HTML message, as big as you like
            messageBodyPart.setContent(content, "text/html");
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource("I://Lab3/logo.png");

            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");

            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
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
}
