/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.balltime;

import hu.daq.servicehandler.ServiceHandler;
import hu.daq.timeengine.TimeEngine;
import hu.daq.watch.BaseWatch;
import hu.daq.watch.CountdownWatch;
import hu.daq.watch.TimeoutListener;
import hu.daq.watch.fx.TimeDisplay;
import hu.daq.watch.utility.WatchFactory;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

/**
 *
 * @author DAQ
 */
public class BallTimeDisplay extends StackPane implements TimeoutListener {
    
    Integer seconds;
    CountdownWatch cw;
    boolean leftpointer;
    boolean rightpointer;
    Node leftteam;
    Node rightteam;
    TimeDisplay td;
    Background tolleft;
    Background toright;
    
    public BallTimeDisplay(TimeEngine te, Integer seconds) {
        this.setPrefSize(100, 100);
        this.setMinSize(100, 100);
        this.setMaxSize(100, 100);
        this.seconds = seconds;
        this.cw = new CountdownWatch(te, 0, 0, seconds);
        this.cw.start();
        this.switchTeam();
        this.td = WatchFactory.getWatchDisplay(cw);
        StackPane.setAlignment(this.td, Pos.CENTER);
        Stop[] stops = new Stop[]{new Stop(0, Color.TRANSPARENT), new Stop(1, Color.GREEN)};
        this.tolleft = new Background(new BackgroundFill(new LinearGradient(0.5, 0.5, 0, 1, true, CycleMethod.NO_CYCLE, stops), null, null));
        this.toright = new Background(new BackgroundFill(new LinearGradient(0.5, 0.5, 1, 1, true, CycleMethod.NO_CYCLE, stops), null, null));        
        this.leftpointer = false;
        this.rightpointer = true;
        this.highlightTeam();
        this.build();
    }
    
    private void build() {
        this.getStyleClass().add("glowdown");
        //this.td.setPrefHeight(40);
        this.getChildren().addAll(this.td);
    }
    
    public void setFont(Font f) {
        this.td.setFont(f);
    }
    
    public void setTeamNodes(Node left, Node right) {
        this.leftteam = left;
        this.rightteam = right;
    }
    
    public void switchTeam() {
        this.pause();
        this.cw.reset();
        this.leftpointer = !this.leftpointer;
        this.rightpointer = !this.rightpointer;
        this.highlightTeam();
    }
    
    public void switchToLeft() {
        this.pause();
        this.cw.reset();
        this.leftpointer = true;
        this.rightpointer = false;
        this.highlightTeam();
    }
    
    public void switchToRight() {
        this.pause();
        this.cw.reset();
        this.leftpointer = false;
        this.rightpointer = true;
        this.highlightTeam();
    }
    
    private void highlightTeam() {
        if (this.leftteam != null) {
            if (this.leftpointer) {
                this.leftteam.getStyleClass().add("glowright");
            } else {
                this.leftteam.getStyleClass().remove("glowright");
            }
            
        }
        if (this.rightteam != null) {
            if (this.rightpointer) {
                this.rightteam.getStyleClass().add("glowleft");
            } else {
                this.rightteam.getStyleClass().remove("glowleft");
            }
        }
        if (this.rightpointer) {
            this.setBackground(this.toright);
        } else {
            this.setBackground(this.tolleft);
        }
    }
    
    public void start() {
        //this.cw.start();
        ServiceHandler.getInstance().getTimeEngine().start();
    }
    
    public void pause() {
        ServiceHandler.getInstance().getTimeEngine().pause();
        //this.cw.pause();
    }
    
    public void reset() {
        this.pause();
        this.cw.reset();
    }
    
    public void set(int milisec) {
        //this.cw.pause();
        this.cw.set(milisec);
    }
    
    public int getMilis() {
        return (int) this.cw.getComputedmilis().get();
    }
    
    public int getRemainingTime() {
        return this.cw.getRemainingTime();
    }
    
    public BaseWatch getWatch() {
        return this.cw;
    }
    
    @Override
    public void timeout() {
        //Sound the horn
        ServiceHandler.getInstance().getHorn().honk();
        this.pause();
    }
}