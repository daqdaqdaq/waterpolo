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
    
    public SimpleStringProperty name = new SimpleStringProperty(); 
    public SimpleStringProperty shortname = new SimpleStringProperty(); 
    public SimpleIntegerProperty capnum  = new SimpleIntegerProperty();    
    public SimpleIntegerProperty team_id = new SimpleIntegerProperty();
    public SimpleBooleanProperty active = new SimpleBooleanProperty();
    public SimpleIntegerProperty player_pic = new SimpleIntegerProperty();

    
    public Player(){

        this.id.addListener(changelistener);
        this.name.addListener(changelistener);
        this.shortname.addListener(changelistener);        
        this.capnum.addListener(changelistener);
        this.team_id.addListener(changelistener);
        this.active.addListener(changelistener);
        this.player_pic.addListener(changelistener);

        
    } 
    
    @Override
    public String toString(){
        return ((Integer)this.capnum.get()).toString()+". "+this.name.get()+":"+this.active.toString();
      
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

    public void setName(SimpleStringProperty name) {
        this.name = name;
    }

    public void setShortname(SimpleStringProperty shortname) {
        this.shortname = shortname;
    }

    public void setCapnum(SimpleIntegerProperty capnum) {
        this.capnum = capnum;
    }

    public void setTeam_id(SimpleIntegerProperty team_id) {
        this.team_id = team_id;
    }

    public void setActive(SimpleBooleanProperty active) {
        this.active = active;
    }

    public void setPlayer_pic(SimpleIntegerProperty player_pic) {
        this.player_pic = player_pic;
    }

    @Override
    public String toInternalString() {
        return "Player::"+this.id.getValue().toString();
    }

   
}
