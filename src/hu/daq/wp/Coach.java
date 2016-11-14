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
public class Coach extends Entity {
    
    protected SimpleIntegerProperty coach_id = new SimpleIntegerProperty();
    protected SimpleStringProperty name = new SimpleStringProperty(); 
    protected SimpleIntegerProperty team_id = new SimpleIntegerProperty();
    protected SimpleIntegerProperty coach_pic = new SimpleIntegerProperty();

    
    public Coach(Postgres db){
        super(db);
        this.coach_id.addListener(changelistener);
        this.name.addListener(changelistener);
        this.team_id.addListener(changelistener);
        this.coach_pic.addListener(changelistener);
        this.subscribe();
        
    } 
    
    

    @Override
    public boolean load(Integer pk) {
        String sendstr = "select * from coach where coach_id="+pk.toString();
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
        this.unsubscribe();
        try{
            this.coach_id.set(Integer.parseInt(data.get("coach_id")));
            this.name.set(data.get("name"));
            this.team_id.set(Integer.parseInt(data.get("team_id")));
            this.coach_pic.set(Integer.parseInt(data.get("coach_pic")));
        } catch (Exception ex){
            System.out.println("Failed to load" + ex.toString());
            return false;
        }
        this.changed.set(false); //This is the original state of the object after loading 
        this.subscribe();
        return true;
    }

    @Override
    public boolean save() {
        //Nothing has changed nothing to save
        if (!this.changed.get()) return true;
        
        String sendstr;
        if (this.coach_id.getValue().equals(0)){
            sendstr = "insert into coach (name,team_id,coach_pic) values("
                    +"'"+this.name.getValueSafe()+"', "
                    +this.team_id.getValue().toString()+", "
                    +this.coach_pic.getValue().toString()
                    + ") returning *";
        } else {
            sendstr = "update coach set (name,team_id,coach_pic) =("
                    +"'"+this.name.getValueSafe()+"', "
                    +this.team_id.getValue().toString()+", "
                    +this.coach_pic.getValue().toString()                
                    + ") where coach_id="+this.coach_id.getValue().toString()
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
        return this.name.get();
        
    }

    public SimpleIntegerProperty getCoach_id() {
        return coach_id;
    }

    public SimpleStringProperty getName() {
        return name;
    }

    public SimpleIntegerProperty getTeam_id() {
        return team_id;
    }

    public SimpleIntegerProperty getCoach_pic() {
        return coach_pic;
    }

    @Override
    public void subscribe() {
        this.db.subscribe("coach", this);
    }

    @Override
    public void unsubscribe() {
        this.db.unsubscribe("coach", this);
    }

    @Override
    public void finalize() throws Throwable {
        System.out.println("finalizing " + this.toString());
        this.db.unsubscribe("coach", this);
        
    }

    @Override
    public void onNotify() {
        System.out.println("notify received by " + this.toString());
        this.load(this.coach_id.getValue());
    }

    //Returns the id of the object in a notification mechanism compatible form
    @Override
    public Integer getID() {
        return this.coach_id.getValue();
    }

    public void setTeamid(Integer teamid) {
        this.team_id.set(teamid);
    }
   
}
