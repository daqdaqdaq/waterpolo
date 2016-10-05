/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.datetime;

import hu.daq.timeengine.TimeEngine;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 *
 * @author DAQ
 */
public class TimeFX extends Label{

    public TimeFX(TimeEngine te, Font font) {
        this.textProperty().bind(te.getTime());
        this.setFont(font);
    }
    
}
