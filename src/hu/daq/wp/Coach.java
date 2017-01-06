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
    

    public SimpleStringProperty name = new SimpleStringProperty(); 
    public SimpleIntegerProperty team_id = new SimpleIntegerProperty();
    public SimpleIntegerProperty coach_pic = new SimpleIntegerProperty();

    
    public Coach(){

        this.id.addListener(changelistener);
        this.name.addListener(changelistener);
        this.team_id.addListener(changelistener);
        this.coach_pic.addListener(changelistener);
       
    } 
    
    @Override
    public String toString(){
        return this.name.get();
        
    }

    public SimpleIntegerProperty getCoach_id() {
        return id;
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


    public void setTeamid(Integer teamid) {
        this.team_id.set(teamid);
    }

    @Override
    public String toInternalString() {
        return "Coach::"+this.id.getValue().toString();        
    }
   
}
