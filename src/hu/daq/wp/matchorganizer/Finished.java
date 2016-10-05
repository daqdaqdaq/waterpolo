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
public class Finished extends MatchPhase{

    public Finished(Organizable screen) {
        super(-1, "Vége", screen);
    }
    
}
