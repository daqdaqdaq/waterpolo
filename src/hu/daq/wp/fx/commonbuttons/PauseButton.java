/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.commonbuttons;

import javafx.scene.Node;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author DAQ
 */
public class PauseButton extends GlyphButton{

    public PauseButton() {
        this.setGraphic(this.fontawesome.create(FontAwesome.Glyph.PAUSE));
    }

    public PauseButton(String text) {
        this();
        super.setText(text);
    }

    public PauseButton(String text, Node graphic) {
        super(text, graphic);
    }



}
