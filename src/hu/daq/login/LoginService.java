/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.login;

import client.DbClient;
import client.Postgres;

/**
 *
 * @author DAQ
 */
public class LoginService {
    
    private Boolean admin = false;
    private String user;
    private String token;
    private static LoginService instance;
    private final Postgres db;
    private String dburi ;
            //= "jdbc:postgresql://192.168.71.213/waterpolo?ssl=true&tcpKeepAlive=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

    private LoginService(){
        this.db = new Postgres();
    }
    
    public static LoginService getInst(){
        
        if (instance == null){
            instance = new LoginService();
        }
        return instance;
    }
    
    public Boolean login(String user, String pwd){
        
        if (this.db.connect(this.dburi, user, pwd)){
            this.admin = false;
            this.askForToken();
            return true;
        }
        return false;

        
    }
    
    private void askForToken(){
        
        this.token = this.db.query("select gettoken() as token").get(0).get("token");
    
    }
    
    public Postgres getDb(){
        return this.db;
    }
    
    public Boolean isLoggedIn(){
        return this.db.stateProperty().getValue()>DbClient.DISCONNECTED;
    }
    
    public Boolean isAdmin(){
        return this.admin;
    }

    public void setDburi(String dburi) {
        this.dburi = dburi;
    }
    
}
