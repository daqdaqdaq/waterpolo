/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.penaltytimer;

import hu.daq.draganddrop.DragObjectDecorator;
import hu.daq.timeengine.TimeEngine;
import hu.daq.watch.CountdownWatch;
import hu.daq.watch.fx.TimeDisplay;
import hu.daq.wp.fx.screens.entityselector.DragEntityMediator;
import javafx.scene.input.TransferMode;

/**
 *
 * @author DAQ
 */
public class PenaltyTimer extends TimeDisplay{
    
    CountdownWatch cw;
    PenaltyHolder ph;
    
    public PenaltyTimer(TimeEngine te){
        super(false,false,true,true); 
        this.setFontSize(18);
        this.cw = new CountdownWatch(te,0,0,20);
        this.attachWatch(cw);
        this.cw.start();
        DragObjectDecorator.decorate(this, new DragPenaltyMediator(this), TransferMode.MOVE);
    }

    public void setHolder(PenaltyHolder ph){
        this.ph = ph;
    }

    public PenaltyHolder getHolder(){
        return this.ph;
    }    
  
    public int getPenaltyTime(){
        return (int)this.cw.getComputedmilis().get();
    }

}
