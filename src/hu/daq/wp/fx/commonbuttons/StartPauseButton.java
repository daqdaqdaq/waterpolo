/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.commonbuttons;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author DAQ
 */
public class StartPauseButton extends GlyphButton{
    Glyph startpic;
    Glyph pausepic;
    boolean playing;
    
    public StartPauseButton() {
        this.playing = false;
        this.startpic = this.fontawesome.create(FontAwesome.Glyph.PLAY);
        this.pausepic = this.fontawesome.create(FontAwesome.Glyph.PAUSE);
        this.adjustGraphic();
    }

    public StartPauseButton(String text) {
        this();
        super.setText(text);
    }

    public StartPauseButton(String text, Node graphic) {
        super(text, graphic);
    }

    public boolean startpauseState(){
        this.playing = !this.playing;
        this.adjustGraphic();
        return this.playing;
    }
    
    public void startState(){
        this.playing = true;
        this.adjustGraphic();
    }
    
    public void pauseState(){
        this.playing = false;
        this.adjustGraphic();
    }
    
    private void adjustGraphic(){
        if (this.playing){
            this.setGraphic(this.pausepic);
        } else {
            this.setGraphic(this.startpic);
        }
    }

}
