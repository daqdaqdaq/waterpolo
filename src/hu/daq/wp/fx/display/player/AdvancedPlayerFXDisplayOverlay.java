/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.player;

import hu.daq.timeengine.TimeEngine;
import hu.daq.watch.CountdownWatch;
import hu.daq.watch.Time;
import hu.daq.watch.fx.TimeDisplay;
import hu.daq.watch.utility.WatchFactory;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
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
public class AdvancedPlayerFXDisplayOverlay extends StackPane{
    CountdownWatch cw;
    Time time;
    Integer milistocount;
    
    public AdvancedPlayerFXDisplayOverlay(PlayerDisplayFX underlaying, TimeEngine te, Integer secs) {
        this.cw = new CountdownWatch(te,0,0,secs);
        this.cw.addTimeoutListener(underlaying);
        this.time = this.cw.getObservableTime();
        this.time.tsec.addListener((ob,ov,nv) -> this.setBGFill());
        this.milistocount = cw.getTimeToCount();
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
        this.setBackground(new Background(new BackgroundFill(new Color(0.5,0.0,0.0,0.8),null,null)));
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
    
    private void setBGFill(){
        BackgroundFill bf;
        if (this.time.sec.get()==0&&this.time.tsec.get()==1){
            bf = new BackgroundFill(new Color(0.0,0.9,0.0,0.8),null,null);
        } else{
            double size = this.getWidth()-(this.getWidth()*((float)(this.time.sec.get()*10+this.time.tsec.get())/(float)(this.milistocount/100)));
            bf = new BackgroundFill(new Color(0.5,0.0,0.0,0.8),null,new Insets(0,size,0,0));
        }
        this.setBackground(new Background(bf));
    }
}
