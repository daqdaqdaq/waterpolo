/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.screens;

import hu.daq.wp.fx.screens.teamselector.*;
import hu.daq.draganddrop.DragObjectMediator;
import hu.daq.wp.fx.TeamSimpleFX;
import hu.daq.wp.fx.display.player.PlayerControlFX;
import javafx.event.Event;
import javafx.scene.input.DataFormat;

/**
 *
 * @author DAQ
 */
public class DragPlayerMediator implements DragObjectMediator<PlayerControlFX> {
    
    PlayerControlFX player;

    public DragPlayerMediator(PlayerControlFX player) {
        this.player = player;
        System.out.println("Mediator's built");
    }

    @Override
    public String extractDragObject() {
        return this.player.getPlayerID().toString();
        
    }

    @Override
    public void finishDrag(Event event) {
        

    }

    @Override
    public boolean isDragable() {
        if (this.player == null) return false;
        return !this.player.isFinallyOut();
    }

    @Override
    public DataFormat getDataFormat() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
