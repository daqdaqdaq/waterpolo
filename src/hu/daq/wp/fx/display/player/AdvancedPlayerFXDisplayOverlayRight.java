/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.player;

import hu.daq.timeengine.TimeEngine;
import javafx.geometry.Pos;

/**
 *
 * @author DAQ
 */
public class AdvancedPlayerFXDisplayOverlayRight extends AdvancedPlayerFXDisplayOverlay{

    public AdvancedPlayerFXDisplayOverlayRight(PlayerDisplayFX underlaying, TimeEngine te, Integer secs) {
        super(underlaying, te, secs);
        this.setAlignment(Pos.CENTER_LEFT);
        
    }
    
}
