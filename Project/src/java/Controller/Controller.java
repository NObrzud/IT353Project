/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import Model.Account;
import View.AccountDAO;

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
        if(account.getUsername().equals("")) return "register.xhtml";
        if(account.getPassword().equals("")) return "register.xhtml";
        if(confirmPassword.equals("")) return "register.xhtml";
        //Making sure password and confirm password are the same
        if(!account.getPassword().equals(confirmPassword)) return "register.xhtml";
        //Register
        AccountDAO ad = new AccountDAO();
        if(ad.register(account) == 1) return "dashboard.xhtml";
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
    
}
