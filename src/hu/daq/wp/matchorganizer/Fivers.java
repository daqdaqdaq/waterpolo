/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.matchorganizer;

import hu.daq.wp.fx.display.screens.ScoreBoardScreen;

/**
 *
 * @author DAQ
 */
public class Fivers extends MatchPhase{
    protected Integer phasenum;
    
    public Fivers(Organizable screen) {
        super(0, "Büntetők", screen);

    }
    
    @Override
    public String getPhaseName(){
        return this.phasename;
    }    
}
