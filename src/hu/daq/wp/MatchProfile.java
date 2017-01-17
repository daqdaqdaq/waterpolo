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
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author DAQ
 */
public class MatchProfile extends Entity{

    public SimpleStringProperty profilename = new SimpleStringProperty(); 
    public SimpleStringProperty profile  = new SimpleStringProperty(); 
    public SimpleIntegerProperty balltimelength  = new SimpleIntegerProperty(); 
    public SimpleIntegerProperty numlegs  = new SimpleIntegerProperty();
    public SimpleIntegerProperty legduration  = new SimpleIntegerProperty(); 
    public SimpleIntegerProperty breakduration  = new SimpleIntegerProperty();
    public SimpleIntegerProperty midbreakduration  = new SimpleIntegerProperty(); 
    public SimpleIntegerProperty numovertimes  = new SimpleIntegerProperty();
    public SimpleIntegerProperty overtimeduration  = new SimpleIntegerProperty();    

    
    public MatchProfile() {
        this.profilename.addListener(changelistener);
        this.profile.addListener(changelistener);
        this.balltimelength.addListener(changelistener);
        this.numlegs.addListener(changelistener);
        this.legduration.addListener(changelistener);
        this.breakduration.addListener(changelistener);
        this.midbreakduration.addListener(changelistener);
        this.numovertimes.addListener(changelistener);
        this.overtimeduration.addListener(changelistener);
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

    public String getProfile() throws JSONException {
        return this.getProfileRaw().toString();
    }

    public void setProfile(String profile) throws JSONException {
        JSONObject js = new JSONObject(profile);
        this.balltimelength.set(js.getInt("balltimelength")/1000);
        this.numlegs.set(js.getInt("numlegs"));
        this.legduration.set(js.getInt("legduration")/1000);
        this.breakduration.set(js.getInt("breakduration")/1000);
        this.midbreakduration.set(js.getInt("midbreakduration")/1000);
        this.numovertimes.set(js.getInt("numovertimes"));
        this.overtimeduration.set(js.getInt("overtimeduration")/1000);
    }

    public JSONObject getProfileRaw() throws JSONException{
        JSONObject js = new JSONObject();
        js.put("balltimelength",this.balltimelength.get()*1000);
        js.put("numlegs",this.numlegs.get());
        js.put("legduration",this.legduration.get()*1000);
        js.put("breakduration",this.breakduration.get()*1000);
        js.put("midbreakduration",this.midbreakduration.get()*1000);
        js.put("numovertimes",this.numovertimes.get());
        js.put("overtimeduration",this.overtimeduration.get()*1000);
        return js;
    }
    
    @Override
    public String toString(){
        return this.profilename.getValue();
        
    }

    
    @Override
    public String toInternalString() {
        return "MatchProfile::"+this.id.getValue().toString();
    }
    
}
