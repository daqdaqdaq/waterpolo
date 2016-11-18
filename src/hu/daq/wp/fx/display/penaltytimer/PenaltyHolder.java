/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.penaltytimer;

import javafx.scene.layout.HBox;

/**
 *
 * @author DAQ
 */
public class PenaltyHolder extends HBox{
    
    public PenaltyHolder(){
        super(5);
    }
    
    public void addPenaltyTimer(PenaltyTimer pt){
        this.getChildren().add(pt);
        pt.setHolder(this);
    }
    
    public void removePenaltyTimer(PenaltyTimer pt){
        this.getChildren().remove(pt);
    }    
    
}
