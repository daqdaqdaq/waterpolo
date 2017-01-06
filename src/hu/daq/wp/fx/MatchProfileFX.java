/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

import client.Postgres;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.wp.MatchProfile;
import javafx.collections.FXCollections;

import javafx.scene.control.ChoiceBox;

/**
 *
 * @author DAQ
 */
public class MatchProfileFX extends ChoiceBox<MatchProfile>{


    public MatchProfileFX() {
    }

    public void load(){
        this.setItems(FXCollections.observableList(ServiceHandler.getInstance().getDbService().getMatchProfiles()));
    }
    
    public MatchProfile getSelected(){
        return this.getSelectionModel().getSelectedItem();
        
    }
    
    public boolean isSelected(){
        return !this.getSelectionModel().isEmpty();
    }
    
}
