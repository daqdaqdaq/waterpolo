/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.matchorganizer;

import hu.daq.servicehandler.ServiceHandler;

/**
 *
 * @author DAQ
 */
public class Overtime extends MatchPhase{
    protected Integer phasenum;
    
    public Overtime(int milisec, Organizable screen, Integer phasenum) {
        super(milisec, "Hosszabbítás", screen);
        this.phasenum = phasenum;        
    }
    

    
    @Override
    public void endPhase(){
        ServiceHandler.getInstance().getHorn().honkLong();
    }    
    
   @Override
    public Integer getPhaseNum(){
        return this.phasenum;
    }     
}
