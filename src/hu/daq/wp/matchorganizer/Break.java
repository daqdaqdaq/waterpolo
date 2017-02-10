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
public class Break extends MatchPhase{

    public Break(Organizable screen, Integer lengthinsec) {
        super(lengthinsec, "Szünet", screen); //2 minutes
    }

    @Override
    public boolean wantDistinctTimeEngine(){
        return true;
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
   
}
