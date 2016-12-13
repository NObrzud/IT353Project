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
import Model.AdminCart;
import Model.Cart;
import Model.CreditCard;
import Model.Image;
import java.util.ArrayList;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.*;
import javax.mail.internet.*;
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
    private Image image;
    private CreditCard creditCard;
    private UploadedFile file;
    private ArrayList<Image> imageArr;
    private ArrayList<String> imageNamesArr;
    private ArrayList<Image> winnerArr;
    private ArrayList<Cart> cart;
    private ArrayList<AdminCart> admincart;
    private int userRating;
    private String dropDownString;
    private WeekController wc;
    
    private String fn = "";
    private String em = "";
    private double price = 0.0;

    public Controller() {
        wc = new WeekController();
        account = new Account();
        image = new Image();
        creditCard = new CreditCard();
        userRating = 0;
        dropDownString = "";
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
    public void makeWinner(){
        AccountDAO dao = new AccountDAO();
        dao.updateWinner(dropDownString);
        sendEmail(account.getEmail(),"it353project@gmail.com","You Won!", "Congratulations! \nYour image was selected as a winner for this week!");
    }
    public void getWinnerImagesFromDB(){
        AccountDAO dao = new AccountDAO();
        String query = "SELECT * FROM PHOTOS WHERE WINNER = 1";
        winnerArr = dao.getImages(query);
    }
     
    //This method calls the method to sign you in
    public String signIn() {
        AccountDAO ad = new AccountDAO();
        ad.emptyCart();
        if(account.getEmail().equals("")){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Login", "Invalid Username/Password. Please try again.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "index.xhtml";
        }
        if(account.getPassword().equals("")){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Login", "Invalid Username/Password. Please try again.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "index.xhtml";
        }
        int x = ad.login(account);
        if (x == 1) {
            return "dashboard.xhtml";
        } else if (x == 2) {
            return "a_dashboard.xhtml";
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Login", "Invalid Username/Password. Please try again.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "login.xhtml";
        }
    }

    //This method calls the method to register you into the DB
    public String authenticate() {
        //Making sure all values have been inputed
        if (account.getFirstName().equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Please enter a your firstname.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "register.xhtml";
        }
        if (account.getLastName().equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Please enter a your lastname.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "register.xhtml";
        }
        if (account.getEmail().equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Please enter a email.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "register.xhtml";
        }
        if (account.getEmail().charAt(0)=='.' ||
                account.getEmail().charAt(account.getEmail().length()-1)=='.' ||
                account.getEmail().charAt(0)=='@' ||
                account.getEmail().charAt(account.getEmail().length()-1)=='@' ||
                !account.getEmail().contains("@") ||
                !account.getEmail().contains(".")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Please enter a valid email.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "register.xhtml";
        }
        if (account.getPassword().equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Please enter a password.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "register.xhtml";
        }
        if (account.getConfirmPass().equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Please confirm your password.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "register.xhtml";
        }
        //Making sure password and confirm password are the same
        if (!account.getPassword().equals(account.getConfirmPass())) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Passwords don't match.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "register.xhtml";
        }
        if(creditCard.getAccountNumber().equals("") || creditCard.getAccountNumber().length() != 16 
                || !validCreditCard(creditCard.getAccountNumber())){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Credit Card Number.", "Please enter a valid Credit Card Number.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "register.xhtml";
        }
        if(creditCard.getCardName().equals("")){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Credit Card Information.", "Please enter a name for your Credit Card name.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "register.xhtml";
        }
        if(creditCard.getExpirationDate().equals("") || !validateExpirationDate(creditCard.getExpirationDate())){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Credit Card Date", "Please enter a valid Credit Card expiration date.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "register.xhtml";
        }
        if(creditCard.getSecurityCode().equals("") || !validateSecurityCode(creditCard.getSecurityCode())){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Credit Card Date", "Please enter a valid Credit Card expiration date.");
            FacesContext.getCurrentInstance().addMessage(null, message);
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
    
    public String authenticateCreditCardInformation()
    {
        if(creditCard.getAccountNumber().equals("") || creditCard.getAccountNumber().length() != 16 
                || !validCreditCard(creditCard.getAccountNumber())){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Credit Card Number.", "Please enter a valid Credit Card Number.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "pay.xhtml";
        }
        if(creditCard.getCardName().equals("")){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Credit Card Information.", "Please enter a name for your Credit Card name.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "pay.xhtml";
        }
        if(creditCard.getExpirationDate().equals("") || !validateExpirationDate(creditCard.getExpirationDate())){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Credit Card Date", "Please enter a valid Credit Card expiration date.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "pay.xhtml";
        }
        if(creditCard.getSecurityCode().equals("") || !validateSecurityCode(creditCard.getSecurityCode())){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Credit Card Date", "Please enter a valid Credit Card expiration date.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "pay.xhtml";
        }
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thank you!", "Thank you for purchasing these images.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        AccountDAO dao = new AccountDAO();
        dao.addToAdminCart(account, fn, em, price);
        return "dashboard.xhtml";
    }
    
    private boolean validateSecurityCode(String code)
    {
        if(code.length() != 3) return false;
        if( !isDigit(code.charAt(0)) || !isDigit(code.charAt(1)) || !isDigit(code.charAt(2))) return false;
        return true;
    }
    
    private boolean validateExpirationDate(String date)
    {
        if(date.length() != 5) return false;
        if(date.charAt(2) != '/') return false;
        if( !isDigit(date.charAt(0)) || !isDigit(date.charAt(1))
             || !isDigit(date.charAt(3)) || !isDigit(date.charAt(4)))
                        return false;
        return true;
    }
    
    private boolean validCreditCard(String name)
    {
        for(int i = 0; i < name.length(); i++)
        {
            if(!isDigit(name.charAt(i))) return false;
        }
        return true;
    }
    
    private boolean isDigit(char x)
    {
        if(x == '1' || x == '2' || x == '3' || x == '4' || x == '5' || x == '6' || x == '7' 
                || x == '8' || x == '9' || x == '0')
            return true;
        return false;
    }

    public void upload(FileUploadEvent event) {
        AccountDAO ad = new AccountDAO();
        int x = ad.uploadImage(event, account, 4.99);
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

    public String checkAccountInfo() {
        AccountDAO accDao = new AccountDAO();
        ArrayList accCollection = accDao.findByAccountEmail(account.getEmail());
        if (accCollection.isEmpty()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Could not verify your account "
                    + "with the information provided. Please try again.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "forgotpassword.xhtml";
        } else {
            Account a = (Account) accCollection.get(0);
            if (account.getFirstName().toUpperCase().equals(a.getFirstName().toUpperCase())
                    && account.getLastName().toUpperCase().equals(a.getLastName().toUpperCase())
                    && account.getEmail().toUpperCase().equals(a.getEmail().toUpperCase())) {
                return "changepassword.xhtml";
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Could not verify your account "
                        + "with the information provided. Please try again.");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "forgotpassword.xhtml";
            }
        }
    }

    public String changePassword() {
        if(account.getPassword().equals("")){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Please enter your new password.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "changepassword.xhtml";
        }
        if(account.getConfirmPass().equals("")){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Please confirm your password");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "changepassword.xhtml";
        }
        if(!account.getPassword().equals(account.getConfirmPass())){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Passwords don't match.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "changepassword.xhtml";
        }
        else{
            AccountDAO accDao = new AccountDAO();
            accDao.changePassword(account);
        }
        return "index.xhtml";
        
    }

    /*
    * sends email
     */
    public void sendEmail(String recipient, String sender, String subject, String content) {
        String to = recipient;
        String from = sender;
        String host = "smtp.gmail.com";
        String username = "it353project@gmail.com";
        String password = "nickandcharlie";

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
        Session session = Session.getInstance(properties,
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

            message.setText(content);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    public void onrate(){
        AccountDAO ad = new AccountDAO();
        ad.updateRating(dropDownString ,userRating);
    }
    
    public String payroyalties(){
        /* TODO SEND EMAIL */
        AccountDAO ad = new AccountDAO();
        ad.emptyAdminCart();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmation:", "Royalties have been paid and an email confirmation will be sent.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        return "payroyalties.xhtml";
    }
    
    public void addToCart()
    {
        fn = "";
        em = "";
        price = 0;
        for(int i = 0; i < imageArr.size(); i++){
            if(imageArr.get(i).getFilename().equals(dropDownString)){
                Image tempImage = imageArr.get(i);
                fn = dropDownString;
                price = tempImage.getPrice();
                em = tempImage.getEmail();
            }
        }
        AccountDAO ad = new AccountDAO();
        ad.addToCart(fn, em, price);
    }
    
    public ArrayList<Cart> getCart()
    {
        AccountDAO ad = new AccountDAO();
        cart = ad.getCart();
        return cart;
    }
    
    public double getTotalPrice()
    {
        double price = 0.0;
        for(int i = 0; i < cart.size(); i++)
        {
            price += cart.get(i).getPrice();
        }
        return price;
    }
    
    public String logout()
    {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        AccountDAO ad = new AccountDAO();
        ad.emptyCart();
        return "index.xhtml";
    }
    
    public String updateAccount()
    {
        boolean update = true;
        //Making sure all values have been inputed
        if (account.getFirstName().equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Please enter a your firstname.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            update = false;
        }
        if (account.getLastName().equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Please enter a your lastname.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            update = false;
        }
        if (account.getPassword().equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Please enter a password.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            update = false;
        }
        if (account.getConfirmPass().equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Please confirm your password.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            update = false;
        }
        //Making sure password and confirm password are the same
        if (!account.getPassword().equals(account.getConfirmPass())) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Inputs!", "Passwords don't match.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            update = false;
        }
       AccountDAO ad = new AccountDAO();
       if(update) ad.updateProfile(account);
       
       return "dashboard.xhtml";
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

    public ArrayList<Image> getImageArr() {
        return imageArr;
    }

    public void setImageArr(ArrayList<Image> imageArr) {
        this.imageArr = imageArr;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
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

    public String getDropDownString() {
        return dropDownString;
    }

    public void setDropDownString(String dropDownString) {
        this.dropDownString = dropDownString;
    }    

    /**
     * @return the creditCard
     */
    public CreditCard getCreditCard() {
        return creditCard;
    }

    /**
     * @param creditCard the creditCard to set
     */
    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
    public ArrayList<AdminCart> getAdmincart() {
        AccountDAO dao = new AccountDAO();
        admincart = dao.getAdminCart();
        return admincart;
    }

    public void setAdmincart(ArrayList<AdminCart> admincart) {
        this.admincart = admincart;
    }

    public WeekController getWc() {
        return wc;
    }

    public void setWc(WeekController wc) {
        this.wc = wc;
    }
    
}
