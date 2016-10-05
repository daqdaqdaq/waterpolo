/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.commonbuttons;

import javafx.scene.Node;

/**
 *
 * @author DAQ
 */
public class TimeButton extends GlyphButton{

    public TimeButton() {
        super.setText("T");
    }

    public TimeButton(String text) {
        this();
        super.setText(text);
    }

    public TimeButton(String text, Node graphic) {
        super(text, graphic);
    }



}
