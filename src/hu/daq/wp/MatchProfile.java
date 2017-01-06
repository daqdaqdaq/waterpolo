/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp;

import client.Postgres;
import java.util.HashMap;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Callback;

/**
 *
 * @author DAQ
 */
public class MatchProfile extends Entity{

    public SimpleStringProperty profilename = new SimpleStringProperty(); 
    public SimpleStringProperty profile  = new SimpleStringProperty(); 
    

    public MatchProfile() {

    }
    
    public static Callback<MatchProfile, Observable[]> extractor(){
        return (MatchProfile t) -> new Observable[]{t.getProfilename()};
    }  



    public SimpleStringProperty getProfilename() {
        return profilename;
    }

    public void setProfilename(SimpleStringProperty profilename) {
        this.profilename = profilename;
    }

    public SimpleStringProperty getProfile() {
        return profile;
    }

    public void setProfile(SimpleStringProperty profile) {
        this.profile = profile;
    }

    @Override
    public String toString(){
        return this.profilename.getValue();
        
    }


    @Override
    public String toInternalString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
