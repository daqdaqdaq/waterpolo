/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.controlpanel;

import hu.daq.keyevent.KeyEventHandler;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.settings.SettingsHandler;
import hu.daq.thriftconnector.client.WPController;
import hu.daq.watch.fx.TimeDisplay;
import hu.daq.wp.fx.commonbuttons.GlyphButton;
import hu.daq.wp.fx.commonbuttons.HornButton;
import hu.daq.wp.fx.commonbuttons.LeftButton;
import hu.daq.wp.fx.commonbuttons.PauseButton;
import hu.daq.wp.fx.commonbuttons.ResetButton;
import hu.daq.wp.fx.commonbuttons.RightButton;
import hu.daq.wp.fx.commonbuttons.SetTimeButton;
import hu.daq.wp.fx.commonbuttons.StartPauseButton;
import hu.daq.wp.fx.display.balltime.BallTime;
import hu.daq.wp.fx.display.leginfo.LegInfo;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
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
    private StartPauseButton start;
    private GlyphButton settime;
    private GlyphButton balltimereset;
    private GlyphButton horn;
    private Label leglabel;
    private BallTime balltimedisplay;
    private LegInfo leginfo;


    public ControlPanel() {
        //this.balltimer = new TimeDisplay(false, false, true, true);
        this.balltimeright = new RightButton();
        this.balltimeleft = new LeftButton();
        this.start = new StartPauseButton();
        this.settime = new SetTimeButton();
        this.balltimereset = new ResetButton();
        this.horn = new HornButton();
        this.leglabel = new Label("1. játékrész");
        this.balltimedisplay = new BallTime(ServiceHandler.getInstance().getTimeEngine(), 30);
        this.leginfo = new LegInfo(ServiceHandler.getInstance().getTimeEngine());
        KeyEventHandler keh = ServiceHandler.getInstance().getKeyEventHandler();
        SettingsHandler sh = ServiceHandler.getInstance().getSettings();
        keh.bindButton(sh.getProperty("key_startstop"), this.start);
        keh.bindButton(sh.getProperty("key_left"), this.balltimeleft);
        keh.bindButton(sh.getProperty("key_right"), this.balltimeright);
        keh.bindButton(sh.getProperty("key_reset"), this.balltimereset);
        keh.bindButton(sh.getProperty("key_horn"), this.horn);

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
        buttongrid.add(this.settime, 0, 0);
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
        ServiceHandler.getInstance().getTimeEngine().getRunning().addListener((bservable, ov, nv) -> {
            if (nv) {
                Platform.runLater(() -> {
                    this.start.startState();
                });

            } else {
                Platform.runLater(() -> {
                    this.start.pauseState();
                });

            }

        });

        this.balltimeleft.setOnAction((ev) -> {
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).switchTeamToLeft();
            this.balltimedisplay.switchToLeft();
        });

        this.balltimeright.setOnAction((ev) -> {
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).switchTeamToRight();
            this.balltimedisplay.switchToRight();
        });

        this.start.setOnAction((ev) -> {
            if (this.start.startpauseState()) {
                ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).startMatch();
                this.balltimedisplay.start();
            } else {
                ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).pauseMatch();
            }

        });

        this.balltimereset.setOnAction((ev) -> ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).resetBallTime());
        
        this.settime.setOnAction((ev) -> this.balltimedisplay.showPopOver());
        
        this.horn.setOnAction((ev) -> ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).soundHorn());
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

    public void setLegTime(int milisecs) {
        this.leginfo.setTime(milisecs);
    }
}
