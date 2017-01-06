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
public class Break extends MatchPhase{

    public Break(Organizable screen, Integer lengthinsec) {
        super(lengthinsec, "Szünet", screen); //2 minutes
    }

    public boolean wantDistinctTimeEngine(){
        return true;
    }
   
}
