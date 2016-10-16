/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

import client.Postgres;
import hu.daq.wp.MatchProfile;
import hu.daq.wp.MatchProfiles;
import javafx.scene.control.ChoiceBox;

/**
 *
 * @author DAQ
 */
public class MatchProfileFX extends ChoiceBox<MatchProfile>{
    MatchProfiles match;

    public MatchProfileFX(Postgres db) {
        this.match = new MatchProfiles(db);
        this.setItems(this.match.getProfiles());
    }

    public void load(){
        this.match.load();
    }
    
    public MatchProfile getSelected(){
        return this.getSelectionModel().getSelectedItem();
        
    }
    
    public boolean isSelected(){
        return !this.getSelectionModel().isEmpty();
    }
    
}
