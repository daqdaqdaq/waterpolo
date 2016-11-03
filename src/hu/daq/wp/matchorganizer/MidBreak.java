/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.matchorganizer;

/**
 *
 * @author DAQ
 */
public class MidBreak extends MatchPhase{

    public MidBreak(Organizable screen) {
        super(300000, "Szünet", screen); //5 minutes
    }

    public boolean wantDistinctTimeEngine(){
        return true;
    }
   
}
