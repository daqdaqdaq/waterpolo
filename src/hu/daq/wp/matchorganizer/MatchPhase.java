/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.matchorganizer;

import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author DAQ
 */
public abstract class MatchPhase{
    protected int milisec;
    protected String phasename;
    protected Organizable screen;
    protected int availabletimeouts = 0;

    
    //Duration of the phase in milisecs, the name of the phase and the screen to control
    public MatchPhase(int milisec, String phasename, Organizable screen){
        this.milisec = milisec;
        this.phasename = phasename;
        this.screen = screen;
 
    }
    
    public String getPhaseName(){
        return this.phasename;
    }
    
    public int getDuration(){
        return this.milisec;
    }
    
    
    public void setupPhase(){
        this.screen.setupPhase(this);
    
    }

    public int getAvailableTimeouts() {
        return availabletimeouts;
    }
    
 
    
    //Jobs at the end of the phase eg. sound the horn at match legs' end
    public void endPhase(){
        //Do nothing usually
    }
    
}
