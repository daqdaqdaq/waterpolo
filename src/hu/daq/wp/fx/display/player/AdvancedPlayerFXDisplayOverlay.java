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
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author DAQ
 */
public abstract class AdvancedPlayerFXDisplayOverlay extends StackPane{
    protected CountdownWatch cw;
    protected Time time;
    protected TimeDisplay wd;
    protected Integer milistocount;
    protected StackPane sp;
    
    public AdvancedPlayerFXDisplayOverlay(PlayerDisplayFX underlaying, TimeEngine te, Integer secs) {
        this.cw = new CountdownWatch(te,0,0,secs);
        this.cw.addTimeoutListener(underlaying);
        this.time = this.cw.getObservableTime();
        this.time.tsec.addListener((ob,ov,nv) -> this.setBGFill());
        this.milistocount = cw.getTimeToCount();
        this.sp = new StackPane();
        this.setWidth(underlaying.getWidth()*0.98);
        this.setHeight(underlaying.getHeight()*0.98);
        //this.sp.setWidth(underlaying.getWidth()*0.98);
        //this.sp.setHeight(underlaying.getHeight()*0.98);        
        this.wd = WatchFactory.getWatchDisplay(cw);
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
        this.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        this.setBackground(new Background(new BackgroundFill(new Color(0.3,0.3,0.3,0.5),null,null)));
        this.sp.setBackground(new Background(new BackgroundFill(new Color(0.5,0.0,0.0,0.8),null,null)));
       // StackPane.setAlignment(wd, Pos.CENTER);
        HBox hb = new HBox();
        wd.setFont(new Font(35));
        this.sp.getChildren().add(wd);
        //wd.setColor(Color.RED);
        //hb.getChildren().add(wd);
        //hb.setAlignment(Pos.CENTER);
        //StackPane.setAlignment(wd, Pos.CENTER);
        //this.setAlignment(Pos.CENTER);
        this.getChildren().add(this.sp);
        
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
        this.setBGFill();
    }
    
    public int getTime(){
        return (int)this.cw.getComputedmilis().get();
    }
    
    public void reset(){
        this.cw.pause();
        this.cw.reset();
    }
    
    protected abstract void setBGFill();
}
