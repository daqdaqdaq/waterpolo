/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.player;

import hu.daq.timeengine.TimeEngine;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

/**
 *
 * @author DAQ
 */
public class AdvancedPlayerFXDisplayOverlayRight extends AdvancedPlayerFXDisplayOverlay{

    public AdvancedPlayerFXDisplayOverlayRight(PlayerDisplayFX underlaying, TimeEngine te, Integer secs) {
        super(underlaying, te, secs);
        //this.setAlignment(Pos.CENTER_LEFT);
        this.wd.setAlignment(Pos.CENTER_LEFT);
        
    }
    protected  void setBGFill(){
        BackgroundFill bf;
        if (this.time.sec.get()==0&&this.time.tsec.get()==1){
            bf = new BackgroundFill(new Color(0.0,0.9,0.0,0.8),null,null);
        } else{
            double size = this.getWidth()-(this.getWidth()*((float)(this.time.sec.get()*10+this.time.tsec.get())/(float)(this.milistocount/100)));
            bf = new BackgroundFill(new Color(0.5,0.0,0.0,0.8),null,new Insets(0,size,0,0));
        }
        this.sp.setBackground(new Background(bf));
    }    
}
