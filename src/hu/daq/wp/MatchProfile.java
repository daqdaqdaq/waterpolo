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

    protected SimpleIntegerProperty matchprofile_id = new SimpleIntegerProperty();
    protected SimpleStringProperty profilename = new SimpleStringProperty(); 
    protected SimpleStringProperty profile  = new SimpleStringProperty(); 
    

    public MatchProfile() {
        super(null);
    }
    public MatchProfile(Postgres db) {
        super(db);
    }
    
    public static Callback<MatchProfile, Observable[]> extractor(){
        return (MatchProfile t) -> new Observable[]{t.getProfilename()};
    }  

    public SimpleIntegerProperty getMatchprofile_id() {
        return matchprofile_id;
    }

    public void setMatchprofile_id(SimpleIntegerProperty matchprofile_id) {
        this.matchprofile_id = matchprofile_id;
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
    public boolean load(Integer pk) {
        String sendstr = "select * from matchprofile where matchprofile_id="+pk.toString();
        boolean success = true;
        HashMap<String,String> rec = null;
        
        try{
            rec = this.db.query(sendstr).get(0);
        } catch (Exception e){
            success = false;
        }
        return this.load(rec);
    }

    @Override
    public boolean load(HashMap<String, String> data) {
        try{
            this.matchprofile_id.set(Integer.parseInt(data.get("matchprofile_id")));
            this.profilename.set(data.get("profilename"));
            this.profile.set(data.get("profile"));
        } catch (Exception ex){
            System.out.println("Failed to load" + ex.toString());
            return false;
        }
        this.changed.set(false); //This is the original state of the object after loading 
        return true;
    }

    @Override
    public boolean save() {
        //Nothing has changed nothing to save
        if (!this.changed.get()) return true;
        
        String sendstr;
        if (this.matchprofile_id.getValue().equals(0)){
            sendstr = "insert into matchprofile (profilename,profile) values("
                    +"'"+this.profilename.getValueSafe()+"', "
                    +this.profile.getValue()
                    + ") returning *";
        } else {
            sendstr = "update matchprofile set (profilename,profile) =("
                    +"'"+this.profilename.getValueSafe()+"', "
                    +this.profile.getValue().toString()
                    + ") where player_id="+this.matchprofile_id.getValue().toString()
                    +" returning *";        
        }
        try{
            this.load(db.query(sendstr).get(0));
        } catch (Exception ex){
          System.out.println("Failed:"+ex.toString());
          return false;          
        }
        this.changed.set(false); //The state is saved. This is the new original state 
        return true;
    }
    
    @Override
    public String toString(){
        return this.profilename.getValue();
        
    }
    @Override
    public void subscribe() {
        this.db.subscribe("matchprofile", this);
    }

    @Override
    public void unsubscribe() {
        this.db.unsubscribe("matchprofile", this);
    }

    @Override
    public void finalize() throws Throwable {
        System.out.println("finalizing " + this.toString());
        this.unsubscribe();
    }

    @Override
    public void onNotify() {
        System.out.println("notify received by " + this.toString());
        this.load(this.matchprofile_id.getValue());
    }

    //Returns the id of the object in a notification mechanism compatible form
    @Override
    public Integer getID() {
        return this.matchprofile_id.getValue();
    }
    
}
