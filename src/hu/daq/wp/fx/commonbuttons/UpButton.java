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
public class UpButton extends GlyphButton{

    public UpButton() {
        this.setGraphic(this.fontawesome.create(FontAwesome.Glyph.CHEVRON_UP));
    }

    public UpButton(String text) {
        this();
        super.setText(text);
    }

    public UpButton(String text, Node graphic) {
        super(text, graphic);
    }



}
