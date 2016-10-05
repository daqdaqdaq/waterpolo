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
public class DownButton extends GlyphButton{

    public DownButton() {
        this.setGraphic(this.fontawesome.create(FontAwesome.Glyph.CHEVRON_DOWN));
    }

    public DownButton(String text) {
        this();
        super.setText(text);
    }

    public DownButton(String text, Node graphic) {
        super(text, graphic);
    }



}
