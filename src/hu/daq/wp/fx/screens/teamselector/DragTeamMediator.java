/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.screens.teamselector;

import hu.daq.draganddrop.DragObjectMediator;
import hu.daq.wp.fx.TeamSimpleFX;
import javafx.event.Event;
import javafx.scene.input.DataFormat;

/**
 *
 * @author DAQ
 */
public class DragTeamMediator implements DragObjectMediator<TeamSimpleFX> {
    
    TeamSimpleFX team;

    public DragTeamMediator(TeamSimpleFX team) {
        this.team = team;
        //System.out.println("Mediator's built");
    }

    @Override
    public String extractDragObject() {
        return this.team.getID().toString();
        
    }

    @Override
    public DataFormat getDataFormat() {
        DataFormat df = DataFormat.lookupMimeType("hu.daq.wp.fx.TeamSimpleFX");
        return df!=null?df:new DataFormat("hu.daq.wp.fx.TeamSimpleFX");
    }

    @Override
    public void finishDrag(Event event) {
        
        this.team.setInuse(Boolean.TRUE);
    }

    @Override
    public boolean isDragable() {
        if (this.team == null) return false;
        System.out.println("Check isdragable:"+this.team.isDragable());
        return this.team.isDragable();
    }


    
}
