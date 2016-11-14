/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp;

import client.Postgres;
import java.util.HashMap;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author DAQ
 */
public class Player extends Entity {
    
    protected SimpleIntegerProperty player_id = new SimpleIntegerProperty();
    protected SimpleStringProperty name = new SimpleStringProperty(); 
    protected SimpleStringProperty shortname = new SimpleStringProperty(); 
    protected SimpleIntegerProperty capnum  = new SimpleIntegerProperty();    
    protected SimpleIntegerProperty team_id = new SimpleIntegerProperty();
    protected SimpleBooleanProperty active = new SimpleBooleanProperty();
    protected SimpleIntegerProperty player_pic = new SimpleIntegerProperty();

    
    public Player(Postgres db){
        super(db);
        this.player_id.addListener(changelistener);
        this.name.addListener(changelistener);
        this.shortname.addListener(changelistener);        
        this.capnum.addListener(changelistener);
        this.team_id.addListener(changelistener);
        this.active.addListener(changelistener);
        this.player_pic.addListener(changelistener);
        this.subscribe();
        
    } 
    
    

    @Override
    public boolean load(Integer pk) {
        String sendstr = "select * from player where player_id="+pk.toString();
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
            this.player_id.set(Integer.parseInt(data.get("player_id")));
            this.name.set(data.get("name"));
            this.shortname.set(data.get("shortname"));
            this.capnum.set(Integer.parseInt(data.get("capnum")));
            this.team_id.set(Integer.parseInt(data.get("team_id")));
            this.active.set(Boolean.parseBoolean(data.get("active")));
            this.player_pic.set(Integer.parseInt(data.get("player_pic")));
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
        if (this.player_id.getValue().equals(0)){
            sendstr = "insert into player (name,shortname,capnum,team_id,player_pic,active) values("
                    +"'"+this.name.getValueSafe()+"', "
                    +"'"+this.shortname.getValueSafe()+"', "
                    +this.capnum.getValue().toString()+", "
                    +this.team_id.getValue().toString()+", "
                    +this.player_pic.getValue().toString()+", "
                    +"'"+this.active.getValue().toString()+"'"                   
                    + ") returning *";
        } else {
            sendstr = "update player set (name,shortname,capnum,team_id,player_pic,active) =("
                    +"'"+this.name.getValueSafe()+"', "
                    +"'"+this.shortname.getValueSafe()+"', "
                    +this.capnum.getValue().toString()+", "
                    +this.team_id.getValue().toString()+", "
                    +this.player_pic.getValue().toString()+", "
                    +"'"+this.active.getValue().toString()+"'"                   
                    + ") where player_id="+this.player_id.getValue().toString()
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
        return ((Integer)this.capnum.get()).toString()+". "+this.name.get()+":"+this.active.toString();
        
    }

    public SimpleIntegerProperty getPlayer_id() {
        return player_id;
    }

    public SimpleStringProperty getName() {
        return name;
    }

    public SimpleStringProperty getShortname() {
        return shortname;
    }

    public SimpleIntegerProperty getCapnum() {
        return capnum;
    }

    public SimpleIntegerProperty getTeam_id() {
        return team_id;
    }

    public SimpleBooleanProperty getActive() {
        return active;
    }

    public SimpleIntegerProperty getPlayer_pic() {
        return player_pic;
    }

    public void setCapnum(Integer capnum) {
        this.capnum.set(capnum);
    }

    public void setTeamid(Integer team_id) {
        this.team_id.set(team_id);
    }

    @Override
    public void subscribe() {
        this.db.subscribe("player", this);
    }

    @Override
    public void unsubscribe() {
        this.db.unsubscribe("player", this);
    }

    @Override
    public void finalize() throws Throwable {
        System.out.println("finalizing " + this.toString());
        this.db.unsubscribe("player", this);
        
    }

    @Override
    public void onNotify() {
        System.out.println("notify received by " + this.toString());
        this.load(this.player_id.getValue());
    }

    //Returns the id of the object in a notification mechanism compatible form
    @Override
    public Integer getID() {
        return this.player_id.getValue();
    }
   
}
