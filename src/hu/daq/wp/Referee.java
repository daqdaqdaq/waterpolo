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
public class Referee extends Entity {
    
    protected SimpleIntegerProperty referee_id = new SimpleIntegerProperty();
    protected SimpleStringProperty name = new SimpleStringProperty(); 
    protected SimpleIntegerProperty referee_pic = new SimpleIntegerProperty();

    
    public Referee(Postgres db){
        super(db);
        this.referee_id.addListener(changelistener);
        this.name.addListener(changelistener);
        this.referee_pic.addListener(changelistener);
        this.subscribe();
        
    } 
    
    

    @Override
    public boolean load(Integer pk) {
        String sendstr = "select * from referee where referee_id="+pk.toString();
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
            this.referee_id.set(Integer.parseInt(data.get("referee_id")));
            this.name.set(data.get("name"));
            this.referee_pic.set(Integer.parseInt(data.get("referee_pic")));
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
        if (this.referee_id.getValue().equals(0)){
            sendstr = "insert into referee (name,referee_pic) values("
                    +"'"+this.name.getValueSafe()+"', "
                    +this.referee_pic.getValue().toString()
                    + ") returning *";
        } else {
            sendstr = "update referee set (name,referee_pic) =("
                    +"'"+this.name.getValueSafe()+"', "
                    +this.referee_pic.getValue().toString()
                    + ") where referee_id="+this.referee_id.getValue().toString()
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

    public SimpleIntegerProperty getPlayer_id() {
        return referee_id;
    }

    public SimpleStringProperty getName() {
        return name;
    }


    public SimpleIntegerProperty getReferee_pic() {
        return referee_pic;
    }

    @Override
    public void subscribe() {
        this.db.subscribe("referee", this);
    }

    @Override
    public void unsubscribe() {
        this.db.unsubscribe("referee", this);
    }

    @Override
    public void finalize() throws Throwable {
        System.out.println("finalizing " + this.toString());
        this.db.unsubscribe("referee", this);
        
    }

    @Override
    public void onNotify() {
        System.out.println("notify received by " + this.toString());
        this.load(this.referee_id.getValue());
    }

    //Returns the id of the object in a notification mechanism compatible form
    @Override
    public Integer getID() {
        return this.referee_id.getValue();
    }
   
}
