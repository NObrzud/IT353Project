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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;
/**
 *
 * @author it353F620
 */
@ManagedBean
@SessionScoped
public class Controller 
{
    private Account account;
    private String confirmPassword;
    private UploadedFile file;
    
    public void upload(){
        if(file!=null){
            FacesMessage msg = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null,msg);
        }
    }
    
    public Controller()
    {
        account = new Account();
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
    
    //This method calls the method to sign you in
    public String signIn()
    {
        AccountDAO ad = new AccountDAO();
        if(ad.login(account)==1)
            return "dashboard.xhtml";
        else
            return "login.xhtml";
    }
    
    //This method calls the method to register you into the DB
    public String authenticate()
    {
        //Making sure all values have been inputed
        if(account.getFirstName().equals("")) return "register.xhtml";
        if(account.getLastName().equals("")) return "register.xhtml";
        if(account.getEmail().equals("")) return "register.xhtml";
        if(account.getPassword().equals("")) return "register.xhtml";
        if(confirmPassword.equals("")) return "register.xhtml";
        //Making sure password and confirm password are the same
        if(!account.getPassword().equals(confirmPassword)) return "register.xhtml";
        //Register
        AccountDAO ad = new AccountDAO();
        if(ad.register(account) == 1) return "index.xhtml";
        else return "register.xhtml";
    }

    /**
     * @return the confirmPassword
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * @param confirmPassword the confirmPassword to set
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    /**
     * checks if username is already DB and notifies using AJAX
     */
    public String checkValidEmail(){
        AccountDAO accDao = new AccountDAO();
        ArrayList accCollection = accDao.findByAccountEmail(account.getEmail());
        if(!accCollection.isEmpty()){
            account.setEmailResult("<span style=\"color:red\">Email already used!</span>");
            return account.getEmailResult();
        }
        else{
            account.setEmailResult("");
            return account.getEmailResult();
        }
        
    }
}
