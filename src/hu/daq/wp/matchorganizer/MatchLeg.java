/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.matchorganizer;

import hu.daq.servicehandler.ServiceHandler;
import hu.daq.wp.horn.Horn;

/**
 *
 * @author DAQ
 */
public class MatchLeg extends MatchPhase{
    protected Integer phasenum;
    
    public MatchLeg(int milisec, String phasename, Organizable screen, Integer phasenum) {
        super(milisec, phasename, screen);
        this.phasenum = phasenum;
        this.availabletimeouts = 1;
    }
    
    @Override
    public String getPhaseName(){
        //return RomanNumber.get(this.phasenum)+"-";
        return RomanNumber.get(this.phasenum)+". "+this.phasename;
    }  
    
    @Override
    public void endPhase(){
        try {
            Horn h = ServiceHandler.getInstance().getHorn();
            h.honkLong();
        } catch (Exception ex){
            //Fail silently if there is no horn. Silently :)
        }
    }
    
    @Override
    public Integer getPhaseNum(){
        return this.phasenum;
    }    
}
