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
public class HornButton extends GlyphButton{

    public HornButton() {
        this.setGraphic(this.fontawesome.create(FontAwesome.Glyph.VOLUME_UP));
    }

    public HornButton(String text) {
        this();
        super.setText(text);
    }

    public HornButton(String text, Node graphic) {
        super(text, graphic);
    }



}
