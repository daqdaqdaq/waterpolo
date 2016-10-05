/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.controlpanel;

import hu.daq.servicehandler.ServiceHandler;
import hu.daq.thriftconnector.client.WPController;
import hu.daq.watch.fx.TimeDisplay;
import hu.daq.wp.fx.commonbuttons.GlyphButton;
import hu.daq.wp.fx.commonbuttons.HornButton;
import hu.daq.wp.fx.commonbuttons.LeftButton;
import hu.daq.wp.fx.commonbuttons.PauseButton;
import hu.daq.wp.fx.commonbuttons.ResetButton;
import hu.daq.wp.fx.commonbuttons.RightButton;
import hu.daq.wp.fx.commonbuttons.StartButton;
import hu.daq.wp.fx.display.balltime.BallTime;
import hu.daq.wp.fx.display.leginfo.LegInfo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/**
 *
 * @author DAQ
 */
public class ControlPanel extends GridPane {

    private TimeDisplay legtimer;
    //private TimeDisplay balltimer;
    private GlyphButton balltimeright;
    private GlyphButton balltimeleft;
    private GlyphButton start;
    private GlyphButton pause;
    private GlyphButton balltimereset;
    private GlyphButton horn;
    private Label leglabel;
    private BallTime balltimedisplay;
    private LegInfo leginfo;

    public ControlPanel() {
        //this.balltimer = new TimeDisplay(false, false, true, true);
        this.balltimeright = new RightButton();
        this.balltimeleft = new LeftButton();
        this.start = new StartButton();
        this.pause = new PauseButton();
        this.balltimereset = new ResetButton();
        this.horn = new HornButton();
        this.leglabel = new Label("1. játékrész");
        this.balltimedisplay = new BallTime(ServiceHandler.getInstance().getTimeEngine(), 30);
        this.leginfo = new LegInfo(ServiceHandler.getInstance().getTimeEngine());
        this.build();
    }

    private void build() {
        this.setMaxWidth(200);
        this.setMinWidth(200);
        this.setHgap(5);
        this.setVgap(5);
        this.setPadding(new Insets(10));
        this.balltimedisplay.setFont(new Font(20));
        //this.add(this.leglabel, 0, 0, 3, 1);
        //this.leglabel.setAlignment(Pos.CENTER);
        this.add(this.leginfo, 0, 1, 3, 1);
       // this.legtimer.setAlignment(Pos.CENTER);

        this.add(this.balltimeleft, 0, 2);
        this.add(this.balltimedisplay, 1, 2);
        this.add(this.balltimeright, 2, 2);

        GridPane buttongrid = new GridPane();
        buttongrid.setVgap(5);
        buttongrid.setHgap(5);
        buttongrid.add(this.pause, 0, 0);
        buttongrid.add(this.balltimereset, 1, 0);
        buttongrid.add(this.start, 2, 0);
        buttongrid.add(this.horn, 1, 1);
        buttongrid.setAlignment(Pos.CENTER);

        this.add(buttongrid, 0, 3, 3, 1);

        /*    private GlyphButton balltimeright;
         private GlyphButton balltimeleft;
         private GlyphButton start;
         private GlyphButton pause;    
         private GlyphButton balltimereset;
         private GlyphButton horn;
         */
        this.balltimeleft.setOnAction((ev) -> {
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).switchTeamToLeft();
            this.balltimedisplay.switchToLeft();
        });

        this.balltimeright.setOnAction((ev) -> {
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).switchTeamToRight();
            this.balltimedisplay.switchToRight();
        });

        this.start.setOnAction((ev) -> {
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).startMatch();
            this.balltimedisplay.start();
        });

        this.pause.setOnAction((ev) -> {
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).pauseMatch();
            //this.balltimedisplay.pause();
        });

        this.balltimereset.setOnAction((ev) -> ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).resetBallTime());
        
        this.horn.setOnAction((ev)->((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).soundHorn());        
    }

    public TimeDisplay getLegtimer() {
        return legtimer;
    }

    public GlyphButton getBalltimeright() {
        return balltimeright;
    }

    public GlyphButton getBalltimeleft() {
        return balltimeleft;
    }

    public GlyphButton getStart() {
        return start;
    }

    public GlyphButton getPause() {
        return pause;
    }

    public GlyphButton getBalltimereset() {
        return balltimereset;
    }

    public GlyphButton getHorn() {
        return horn;
    }

    public Label getLeglabel() {
        return leglabel;
    }

    public void setBallTimeClock(int milisecs) {
        this.balltimedisplay.set(milisecs);
    }

    public void pause() {
        this.balltimedisplay.pause();
    }

    public void disable() {
        this.setDisable(true);
    }

    public void enable() {
        this.setDisable(false);
    }

    public void setLegName(String legname) {
        this.leginfo.setLegName(legname);
    }

    public void setTimeToCount(int milisecs) {
        this.leginfo.setTimeToCount(milisecs);
    }
    
    public void setLegTime(int milisecs){
        this.leginfo.setTime(milisecs);
    }
}
