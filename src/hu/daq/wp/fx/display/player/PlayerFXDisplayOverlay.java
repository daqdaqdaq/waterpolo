/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.player;

import hu.daq.timeengine.TimeEngine;
import hu.daq.watch.CountdownWatch;
import hu.daq.watch.fx.TimeDisplay;
import hu.daq.watch.utility.WatchFactory;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author DAQ
 */
public class PlayerFXDisplayOverlay extends StackPane{
    CountdownWatch cw;
    public PlayerFXDisplayOverlay(PlayerDisplayFX underlaying, TimeEngine te, Integer secs) {
        this.cw = new CountdownWatch(te,0,0,secs);
        this.cw.addTimeoutListener(underlaying);
        this.setWidth(underlaying.getWidth()*0.98);
        this.setHeight(underlaying.getHeight()*0.98);
       // this.prefHeightProperty().bind(underlaying.heightProperty().multiply(0.98));
        /*
        this.setMaxWidth(underlaying.widthProperty().get()*0.98);
        this.setMinWidth(underlaying.widthProperty().get()*0.98);
        this.setMaxHeight(underlaying.heightProperty().get()*0.98);
        this.setMinHeight(underlaying.heightProperty().get()*0.98);
        */
        this.build();
    }

    private void build(){
        this.setBackground(new Background(new BackgroundFill(new Color(0.2,0.2,0.2,0.8),null,null)));
        TimeDisplay wd = WatchFactory.getWatchDisplay(cw);
       // StackPane.setAlignment(wd, Pos.CENTER);
        HBox hb = new HBox();
        wd.setFont(new Font(35));
        wd.setColor(Color.RED);
        //hb.getChildren().add(wd);
        //hb.setAlignment(Pos.CENTER);
        //StackPane.setAlignment(wd, Pos.CENTER);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(wd);
        
    }
    
    public void startCountdown(){
        //System.out.println("Countdown has started..");
        this.cw.start();
    }

    public void jumpToEnd(){
        System.out.println("PFXO is jumping to end..");
        this.cw.jumpToEnd();
    }
    
    public void setTime(int milisec){
        this.cw.set(milisec);
    }
    
    public int getTime(){
        return (int)this.cw.getComputedmilis().get();
    }
    
    public void reset(){
        this.cw.pause();
        this.cw.reset();
    }
}
