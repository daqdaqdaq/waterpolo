/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.commonbuttons;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.control.Button;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 *
 * @author DAQ
 */
public class GlyphButton extends Button {

    GlyphFont fontawesome;

    public GlyphButton() {

        try {
            InputStream is = new FileInputStream("D:/fontawesome-webfont.ttf");
            //GlyphFont fa = new FontAwesome(is);
            //GlyphFontRegistry.re.register("LocalFontAwesome", fa, 11);
            this.fontawesome = new GlyphFont("FontAwesome", 16, Class.class.getResourceAsStream("af/fontawesome-webfont.ttf"));
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to load local FontAwesome copy");
            this.fontawesome = GlyphFontRegistry.font("FontAwesome");
        }

    }

    public GlyphButton(String text) {
        this();
        super.setText(text);

    }

    public GlyphButton(String text, Node graphic) {
        this();
        super.setText(text);
        super.setGraphic(graphic);
    }

}
