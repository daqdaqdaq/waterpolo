/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.commonbuttons;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 *
 * @author DAQ
 */
public class ToggleGlyphButton extends ToggleButton{
    GlyphFont fontawesome;
    
    public ToggleGlyphButton() {
        this.fontawesome = GlyphFontRegistry.font("FontAwesome");        
    }

    public ToggleGlyphButton(String text) {
        this();
        super.setText(text);
        
    }

    public ToggleGlyphButton(String text, Node graphic) {
        this();
        super.setText(text);
        super.setGraphic(graphic);
    }

    

    
}
