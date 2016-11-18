/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.penaltytimer;

import hu.daq.wp.fx.screens.teamselector.*;
import hu.daq.draganddrop.DragObjectMediator;
import hu.daq.wp.fx.TeamSimpleFX;
import javafx.event.Event;
import javafx.scene.input.DataFormat;

/**
 *
 * @author DAQ
 */
public class DragPenaltyMediator implements DragObjectMediator<TeamSimpleFX> {
    
    PenaltyTimer timer;

    public DragPenaltyMediator(PenaltyTimer timer) {
        this.timer = timer;
        System.out.println("Mediator's built");
    }

    @Override
    public String extractDragObject() {
        return "Semmi";
        
    }

    @Override
    public DataFormat getDataFormat() {
        DataFormat df = DataFormat.lookupMimeType("hu.daq.wp.fx.TeamSimpleFX");
        return df!=null?df:new DataFormat("hu.daq.wp.fx.TeamSimpleFX");
    }

    @Override
    public void finishDrag(Event event) {

    }

    @Override
    public boolean isDragable() {
        return true;
    }


    
}
