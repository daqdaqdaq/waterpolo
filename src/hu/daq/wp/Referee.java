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
    

    public SimpleStringProperty name = new SimpleStringProperty(); 
    public SimpleIntegerProperty referee_pic = new SimpleIntegerProperty();

    
    public Referee(){

        this.id.addListener(changelistener);
        this.name.addListener(changelistener);
        this.referee_pic.addListener(changelistener);

        
    } 
     
    @Override
    public String toString(){
        return this.name.get();
        
    }

    public SimpleIntegerProperty getReferee_id() {
        return id;
    }

    public SimpleStringProperty getName() {
        return name;
    }


    public SimpleIntegerProperty getReferee_pic() {
        return referee_pic;
    }

    @Override
    public String toInternalString() {
                return "Referee::"+this.id.getValue().toString();
    }
   
}
