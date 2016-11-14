/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.balltime;

import hu.daq.servicehandler.ServiceHandler;
import hu.daq.thriftconnector.client.WPController;
import hu.daq.timeengine.TimeEngine;
import hu.daq.watch.BaseWatch;
import hu.daq.watch.CountdownWatch;
import hu.daq.watch.TimeoutListener;
import hu.daq.watch.fx.RemoteTransmitter;
import hu.daq.watch.fx.TimeDisplay;
import hu.daq.watch.utility.WatchFactory;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author DAQ
 */
public class BallTime extends HBox implements TimeoutListener,RemoteTransmitter {

    Integer seconds;
    CountdownWatch cw;
    PointerTriangle leftpointer;
    PointerTriangle rightpointer;
    Node leftteam;
    Node rightteam;
    TimeDisplay td;
    TimeSetPopOver tspo;

    public BallTime(TimeEngine te, Integer seconds) {
        this.seconds = seconds;
        this.cw = new CountdownWatch(te, 0, 0, seconds);
        this.cw.start();
        this.leftpointer = new PointerTriangleLeft(Color.GREEN, Color.TRANSPARENT);
        this.rightpointer = new PointerTriangleRight(Color.GREEN, Color.TRANSPARENT);
        this.switchToLeft();
        this.td = WatchFactory.getWatchDisplay(cw);
        this.td.enableTimeSetPopOver();
        this.td.setFont(new Font(60));
        td.attachTransmitter(this);
        this.tspo = new TimeSetPopOver(this);
        this.build();
    }

    private void build() {
        this.getStyleClass().add("glowdown");
        //this.td.setPrefHeight(40);
        HBox.setHgrow(this.leftpointer, Priority.NEVER);
        HBox.setHgrow(this.rightpointer, Priority.NEVER);
        HBox.setHgrow(this.td, Priority.SOMETIMES);
        this.getChildren().addAll(this.leftpointer, this.td, this.rightpointer);
    }

    public void setFont(Font f) {
        this.td.setFont(f);
    }

    public void setTeamNodes(Node left, Node right) {
        this.leftteam = left;
        this.rightteam = right;
    }

    public void switchTeam() {
        //this.pause();
        this.cw.reset();
        this.leftpointer.switchState();
        this.rightpointer.switchState();
        this.highlightTeam();
    }

    public void switchToLeft() {
        //this.pause();
        this.cw.reset();
        this.leftpointer.on();
        this.rightpointer.off();
        this.highlightTeam();
    }

    public void switchToRight() {
        //this.pause();
        this.cw.reset();
        this.leftpointer.off();
        this.rightpointer.on();
        this.highlightTeam();
    }

    private void highlightTeam() {
        if (this.leftteam != null) {
            if (this.leftpointer.isOn()) {
                this.leftteam.getStyleClass().add("glowright");
            } else {
                this.leftteam.getStyleClass().remove("glowright");
            }

        }
        if (this.rightteam != null) {
            if (this.rightpointer.isOn()) {
                this.rightteam.getStyleClass().add("glowleft");
            } else {
                this.rightteam.getStyleClass().remove("glowleft");
            }
        }
    }
    
    public boolean isRightAttacking(){
        return this.rightpointer.on.get();
    }

    public boolean isLeftAttacking(){
        return this.leftpointer.on.get();
    }    
    
    public void showPopOver(){
        this.tspo.show(this.td);
    }
    
    public void start() {
        this.cw.start();
        ServiceHandler.getInstance().getTimeEngine().start();
    }

    public void pause() {
        this.cw.pause();
        ServiceHandler.getInstance().getTimeEngine().pause();

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
        //ServiceHandler.getInstance().getHorn().honk();
        this.pause();
    }
    @Override
    public void transmit(int milisec) {
        ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).setBallTime(milisec);
    }
}
