/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.matchorganizer;

import hu.daq.watch.TimeoutListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DAQ
 */
public class MatchOrganizer implements TimeoutListener {

    private Integer currentphase;
    private List<MatchPhase> phases;

    public MatchOrganizer() {
        this.currentphase = 0;
        this.phases = new ArrayList<MatchPhase>();
    }

    public void addPhase(MatchPhase phase) {
        this.phases.add(phase);
    }

    public void setupPhase() {
        this.phases.get(this.currentphase).setupPhase();
    }

    public void jumpToPhase(Integer phasenum) {
        this.setCurrentPhase(phasenum);
        this.setupPhase();
    }

    public void jumToEnd() {
        this.jumpToPhase(this.phases.size() - 1);
    }

    public void nextPhase() {
        //if it's not the last phase and if there is any phases at all
        if (this.currentphase < (this.phases.size() - 1) && this.phases.size() != 0) {

            if (this.currentphase == null) { //We just started no phases yet
                this.currentphase = 0;
            } else {
                if (this.currentphase >= 0) {
                    this.phases.get(this.currentphase).endPhase();
                }
                this.currentphase++;
            }
            this.setupPhase();
        }

    }

    public void prevPhase() {
        //if it's not the last phase and if there is any phases at all
        if (this.currentphase > 0) {
            this.currentphase--;
            this.setupPhase();
        }

    }

    @Override
    public void timeout() {
        this.nextPhase();
    }

    public void setCurrentPhase(Integer phasenum) {
        this.currentphase = phasenum;
    }
    
    public Integer getTimeoutsByPhaseType(String name){
        Integer i = (int)(this.phases.stream().filter(E -> {return E.getAvailableTimeouts()>0&&E.getClass().getName().equalsIgnoreCase(name);}).count()); 
        System.out.println("Num "+name +"s :"+i);
        return i;
    }

}
