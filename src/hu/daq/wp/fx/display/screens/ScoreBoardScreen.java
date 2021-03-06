/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.screens;

import hu.daq.wp.fx.display.control.ControlledScreen;
import client.Postgres;
import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import hu.daq.UDPSender.TimeSender;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.thriftconnector.talkback.PenaltyTime;
import hu.daq.thriftconnector.talkback.StatusReport;
import hu.daq.thriftconnector.talkback.TimeSync;
import hu.daq.thriftconnector.talkback.WPTalkBackClient;
import hu.daq.timeengine.TimeEngine;
import hu.daq.watch.TimeoutListener;
import hu.daq.wp.MatchProfile;
import hu.daq.wp.fx.display.balltime.BallTimeDisplay;
import hu.daq.wp.fx.display.infopopup.FiversDisplayWindow;
import hu.daq.wp.fx.display.infopopup.GoalPopup;
import hu.daq.wp.fx.display.infopopup.PersonInfo;
import hu.daq.wp.fx.display.leginfo.LegInfo;
import hu.daq.wp.fx.display.player.PlayerDisplayFX;
import hu.daq.wp.fx.display.team.TeamDisplayFX;
import hu.daq.wp.fx.display.team.TeamDisplayLeftFX;
import hu.daq.wp.fx.display.team.TeamDisplayRightFX;
import hu.daq.wp.fx.display.timeouts.TimeoutDisplay;
import hu.daq.wp.matchorganizer.MatchPhase;
import hu.daq.wp.matchorganizer.Organizable;
import hu.daq.wp.matchorganizer.OrganizerBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.json.JSONException;

/**
 *
 * @author DAQ
 */
public class ScoreBoardScreen extends BorderPane implements ControlledScreen, Organizable, TimeoutListener {

    SimpleBooleanProperty isdraw;
    TeamDisplayLeftFX leftteam;
    TeamDisplayRightFX rightteam;
    Postgres db;
    TimeEngine timeengine;
    BallTimeDisplay balltime;
    //BaseWatch legtime;
    LegInfo leginfo;
    PersonInfo pi;
    FiversDisplayWindow fiverswindow;
    TimeSender timesender;
    VBox leftteambox;
    VBox rightteambox;
    MatchPhase mp;
    //private final static int LEGTIME = 8; 
    private final static int BALLTIME = 30;

    public ScoreBoardScreen(Postgres db) {

        this.setPrefSize(1024, 768);
        this.db = db;
        this.leftteam = new TeamDisplayLeftFX();
        this.rightteam = new TeamDisplayRightFX();
        this.timeengine = ServiceHandler.getInstance().getTimeEngine();
        this.timeengine.pause();
        this.balltime = new BallTimeDisplay(this.timeengine, BALLTIME);
        this.balltime.getWatch().addTimeoutListener(this);
        this.timesender = new TimeSender(this.balltime.getWatch(), ServiceHandler.getInstance().getSenderthread());
        this.leginfo = new LegInfo(this.timeengine);
        this.pi = new PersonInfo();
        this.fiverswindow = new FiversDisplayWindow();
        this.leftteambox = this.leftteam.getPlayerListView();
        this.rightteambox = this.rightteam.getPlayerListView();
        this.isdraw = new SimpleBooleanProperty(true);
        this.isdraw.bind(Bindings.equal(this.leftteam.getTeamgoals(), this.rightteam.getTeamgoals()));
        //this.leginfo.setTimeToCount(0, LEGTIME, 0);
        //this.leginfo.setLegName("II. negyed");
        this.build();
    }

    private void build() {
        VBox mainbox = new VBox();
        mainbox.setPadding(new Insets(10, 0, 10, 0));
        //mainbox.setStyle("-fx-background-color: #77AACC;");
        //mainbox.setFillWidth(true);
        mainbox.setMinWidth(1024);
        //HBox databox = this.buildDataBox();
        //VBox.setVgrow(databox, Priority.NEVER);
        //databox.prefWidthProperty().bind(this.widthProperty());
        HBox playersbox = this.buildPlayersBox();
        playersbox.prefWidthProperty().bind(this.widthProperty());
        HBox overtime = this.buildOvertimeBox();
        mainbox.setAlignment(Pos.TOP_CENTER);
        mainbox.getChildren().addAll(playersbox, overtime);
        //VBox.setVgrow(this, Priority.NEVER);
        this.setCenter(mainbox);

        //AnchorPane.setTopAnchor(mainbox, 0.0);
    }

    private HBox buildOvertimeBox() {
        HBox hb = new HBox(120);
        //hb.setMinWidth(1024);

        hb.setAlignment(Pos.CENTER);
        Label pad = new Label("TIMEOUT");
        pad.setFont(new Font(40));
        pad.setAlignment(Pos.CENTER);
        TimeoutDisplay leftto = this.leftteam.getTimeoutDisplay();
        TimeoutDisplay rightto = this.rightteam.getTimeoutDisplay();
        HBox.setHgrow(leftto, Priority.NEVER);
        HBox.setHgrow(rightto, Priority.NEVER);
        HBox.setHgrow(pad, Priority.ALWAYS);
        hb.getChildren().addAll(leftto, pad, rightto);
        return hb;
    }

    private VBox buildDataBox() {
        VBox vb = new VBox(10);
        vb.setAlignment(Pos.TOP_CENTER);
        HBox databox = new HBox(20);
        //databox.setMaxHeight(140);
        databox.setFillHeight(false);
        databox.setId("databox");
        //databox.setPadding(new Insets(0, 5, 0, 5));
        //databox.setStyle("-fx-background-color: #181818;");
        VBox leftteambox = this.buildScoreAndNameBox(leftteam);
        //leftteambox.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        VBox rightteambox = this.buildScoreAndNameBox(rightteam);
        //rightteambox.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        VBox centerinfobox = this.buildCenterInfoBox();
        //The width of the scorebox is 3/8 (0.375)of the width of the whole box
        leftteambox.prefWidthProperty().bind(Bindings.multiply(0.375, databox.widthProperty()));
        rightteambox.prefWidthProperty().bind(Bindings.multiply(0.375, databox.widthProperty()));
        HBox.setHgrow(leftteambox, Priority.SOMETIMES);
        HBox.setHgrow(rightteambox, Priority.SOMETIMES);
        databox.getChildren().addAll(leftteambox,
                rightteambox);
        VBox.setVgrow(this.balltime, Priority.ALWAYS);
        vb.getChildren().addAll(centerinfobox, databox, this.balltime);
        return vb;
    }

    private VBox buildScoreAndNameBox(TeamDisplayFX team) {
        VBox scoreandname = new VBox(0);
        //scoreandname.setMaxHeight(140);
        scoreandname.setAlignment(Pos.CENTER);
        Label teamname = team.getTeamNameLabel();
        //teamname.setId("cropedlabel");
        teamname.setFont(new Font(55));
        //FontMetrics metrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(teamname.getFont());
        //teamname.setPadding(new Insets(-metrics.getDescent()+5, 0, -metrics.getAscent()+5, 0));

        //teamname.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        //VBox.setVgrow(teamname, Priority.NEVER);
        Label teamscore = team.getGoalsLabel();
        //teamscore.setId("morecropedlabel");        
        teamscore.setFont(new Font(100));
        //metrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(teamscore.getFont());
        //teamscore.setPadding(new Insets(-metrics.getDescent()+5, 0, -metrics.getAscent()+5, 0));

        //teamscore.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        //VBox.setVgrow(teamscore, Priority.NEVER);        
        scoreandname.getChildren().addAll(teamname,
                teamscore);
        return scoreandname;
    }

    private VBox buildCenterInfoBox() {
        VBox centerinfobox = new VBox(5);
        centerinfobox.setAlignment(Pos.CENTER);
        //centerinfobox.setMinWidth(610);
        //Label locationnamelabel = new Label("Dunaújváros");
        //locationnamelabel.setFont(new Font(10));
        TimeEngine datetime = new TimeEngine();
        datetime.init();
        //StackPane centertimebox = new StackPane();
        //centertimebox.setPrefSize(100, 100);

        this.balltime.setFont(new Font(90));
        //this.balltime.setTeamNodes(this.leftteambox, this.rightteambox);
        //this.balltime.fillHeightProperty().set(true);

        //centertimebox.getChildren().add(this.balltime);
        centerinfobox.getChildren().add(
                //locationnamelabel,
                //     new DateFX(datetime, new Font(5)),
                //     new TimeFX(datetime, new Font(5)),
                this.leginfo
        );
        return centerinfobox;
    }

    private HBox buildPlayersBox() {
        HBox playersbox = new HBox(10);
        playersbox.setAlignment(Pos.CENTER);
        playersbox.setMinWidth(1023);
        VBox databox = this.buildDataBox();
        VBox leftteambox = this.leftteam.getPlayerListView();
        VBox rightteambox = this.rightteam.getPlayerListView();
        playersbox.getChildren().addAll(leftteambox, databox,
                rightteambox);
        HBox.setHgrow(databox, Priority.ALWAYS);
        HBox.setHgrow(leftteambox, Priority.NEVER);
        HBox.setHgrow(rightteambox, Priority.NEVER);
        return playersbox;
    }

    public void loadLeftTeam(Integer teamid) {
        this.leftteam.load(teamid);
    }

    public void loadRightTeam(Integer teamid) {
        this.rightteam.load(teamid);
    }

    public void loadMatchProfile(Integer profileid) throws JSONException {
        MatchProfile mp = ServiceHandler.getInstance().getDbService().getMatchProfile(profileid);
        ServiceHandler.getInstance().setOrganizer(OrganizerBuilder.build(mp, this));
        ServiceHandler.getInstance().getOrganizer().setCurrentPhase(0);
        ServiceHandler.getInstance().getOrganizer().setupPhase();
        this.balltime.setTimeToCount(ServiceHandler.getInstance().getOrganizer().getBallTimeInSecs());
        this.leftteam.getTimeoutDisplay().setUp(ServiceHandler.getInstance().getOrganizer());
        this.rightteam.getTimeoutDisplay().setUp(ServiceHandler.getInstance().getOrganizer());

    }

    private PlayerDisplayFX getPlayer(Integer playerid) throws Exception {
        PlayerDisplayFX player;
        //first search the player in the left team. If it didn't find then try in the right team
        player = this.leftteam.getPlayer(playerid);
        if (player == null) {
            player = this.rightteam.getPlayer(playerid);
        }
        if (player == null) {
            throw new Exception("Player with id " + playerid.toString() + " hasn't found");
        }
        return player;
    }

    private Boolean isRightPlayer(Integer playerid) {
        return this.rightteam.getPlayer(playerid) != null;
    }

    private Boolean isLeftPlayer(Integer playerid) {
        return this.leftteam.getPlayer(playerid) != null;
    }

    public void attackerPenalty() {
        this.switchBallTime();
    }

    public void defenderPenalty() {
        this.resetBallTime();
    }

    public void addPenalty(Integer playerid) {

        //this.timeengine.pause();
        try {
            this.getPlayer(playerid).addPenalty();
        } catch (Exception ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.sendSyncTime();
        /* if (this.balltime.isLeftAttacking()&&this.isLeftPlayer(playerid)){
         this.switchBallTimeRight();
                    
         }   
         if (this.balltime.isRightAttacking()&&this.isRightPlayer(playerid)){
         this.switchBallTimeLeft();
         }*/
    }

    public void setPenalty(Integer playerid, Integer milisecs) {
        try {
            this.getPlayer(playerid).setPenalty(milisecs);
        } catch (Exception ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.sendSyncTime();
    }

    public void setNumPenalties(Integer playerid, Integer penalties) {
        try {
            this.getPlayer(playerid).setNumPenalties(penalties);
        } catch (Exception ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removePenalty(Integer playerid) {
        try {
            this.getPlayer(playerid).removePenalty();
        } catch (Exception ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addGoal(Integer playerid) {
        try {
            this.timeengine.pause();
            this.pauseMatch();
            this.getPlayer(playerid).addGoal();
            GoalPopup pi = new GoalPopup();

            pi.loadPlayer(playerid);
            pi.setTimer(5);
            pi.showIt();
            this.switchBallTime();
        } catch (Exception ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.pauseMatch();
    }

    public void setGoal(Integer playerid) {
        try {

            this.getPlayer(playerid).addGoal();
        } catch (Exception ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setGoal(Integer playerid, Integer goals) {
        try {

            this.getPlayer(playerid).setGoal(goals);
        } catch (Exception ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeGoal(Integer playerid) {
        try {
            this.getPlayer(playerid).removeGoal();
        } catch (Exception ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addFivemGoal(Integer playerid) {
        try {
            this.fiverswindow.getPlayer(playerid).addGoal();

        } catch (Exception ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeFivemGoal(Integer playerid) {
        try {
            this.fiverswindow.getPlayer(playerid).removeGoal();
        } catch (Exception ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addFivemPlayer(Integer playerid) {
        try {
            this.fiverswindow.addPlayer(playerid);

        } catch (Exception ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setBallTime(int milisecs) {
        //this.timeengine.pause();
        if (milisecs > this.leginfo.getRemainingTime()) {
            this.balltime.set(this.leginfo.getRemainingTime());
        } else {
            this.balltime.set(milisecs);
        }
        this.sendSyncTime();
    }

    public void switchBallTimeLeft() {
        //this.timeengine.pause();
        //End all penalties in the defending team

        if (this.balltime.getTimeToCount() > this.leginfo.getRemainingTime()) {
            this.balltime.reset(this.leginfo.getTime());
        } else {
            this.balltime.reset();
        }    
        this.leftteam.removeAllPenalties();
        this.balltime.switchToLeft();
        this.sendSyncTime();
    }

    public void switchBallTimeRight() {
        //this.timeengine.pause();
        //End all penalties in the defending team        


        if (this.balltime.getTimeToCount() > this.leginfo.getRemainingTime()) {
            this.balltime.reset(this.leginfo.getTime());
            //this.balltime.set(this.leginfo.getRemainingTime());
        } else {
            this.balltime.reset();
        }
        this.rightteam.removeAllPenalties();
        this.balltime.switchToRight();        
        this.sendSyncTime();
    }

    public void switchBallTime() {
        //this.timeengine.pause();
        //End all penalties in the defending team


        if (this.balltime.getTimeToCount() > this.leginfo.getRemainingTime()) {
            this.balltime.reset(this.leginfo.getTime());
            //this.balltime.set(this.leginfo.getRemainingTime());
        } else {
            this.balltime.reset();
        }
        if (this.balltime.isRightAttacking()) {
            this.leftteam.removeAllPenalties();
        } else {
            this.rightteam.removeAllPenalties();
        }
        this.balltime.switchTeam();        
        this.sendSyncTime();
    }

    public void resetBallTime() {
        //this.timeengine.pause();
        //this.balltime.reset();
        if (this.balltime.getTimeToCount() > this.leginfo.getRemainingTime()) {
            this.balltime.reset(this.leginfo.getTime());
            //this.balltime.set(this.leginfo.getRemainingTime());
        } else {
            this.balltime.reset();
        }
        this.sendSyncTime();

    }

    public void showPlayerInfo(Integer playerid) {
        try {
            this.pi.loadPlayer(playerid);
            this.pi.showIt();
        } catch (Exception ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showCoachInfo(Integer playerid) {
        try {
            this.pi.loadCoach(playerid);
            this.pi.showIt();
        } catch (Exception ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showRefereeInfo(Integer playerid) {
        try {
            this.pi.loadReferee(playerid);
            this.pi.showIt();
        } catch (Exception ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void hidePlayerInfo() {
        try {
            this.pi.close();
        } catch (Exception ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void startMatch() {
        if (this.leginfo.getTimeToCount() == 0) {
            this.leginfo.getTimeoutListener().timeout();
        } else {
            this.timeengine.start();
        }
    }

    public void pauseMatch() {
        //Pause the main time engine
        this.timeengine.pause();
        this.sendPause();
    }

    public void requestTimeout(Integer teamid) {
        int timeoutnum = 0;
        if (this.mp.getClass().getName().equals("hu.daq.wp.matchorganizer.MatchLeg")) {
            timeoutnum = mp.getPhaseNum();
        }
        if (this.mp.getClass().getName().equals("hu.daq.wp.matchorganizer.Overtime")) {
            timeoutnum = 10 + mp.getPhaseNum();
        }
        if (this.leftteam.getTeamId().equals(teamid)) {
            this.leftteam.requestTimeout(timeoutnum);
        } else {
            this.rightteam.requestTimeout(timeoutnum);
        }
    }

    private TimeSync getTimeSync() {
        TimeSync tsync = new TimeSync();
        tsync.setBalltime(this.balltime.getMilis());
        tsync.setLegtime(this.leginfo.getTime());
        tsync.setAttacking(this.balltime.isRightAttacking() ? "R" : "L");
        this.leftteam.getPlayerStatus().stream().forEach(E -> tsync.addToPenalties(E.toPenaltyTime()));
        this.rightteam.getPlayerStatus().stream().forEach(E -> tsync.addToPenalties(E.toPenaltyTime()));
        return tsync;
    }

    public void sendSyncTime() {
        //Building time syncronization structure to send back to the controller app

        //And sending it back via the Thrift connection
        ((WPTalkBackClient) ServiceHandler.getInstance().getThriftConnector().getClient()).syncTime(this.getTimeSync());
    }

    public void receiveSyncTime(TimeSync tsync) {

        this.leginfo.setTime(tsync.getLegtime());

        if (tsync.getAttacking().equals("R")) {
            if (this.balltime.isLeftAttacking()) {
                this.balltime.switchToRight();
            }

        }
        if (tsync.getAttacking().equals("L")) {
            if (this.balltime.isRightAttacking()) {
                this.balltime.switchToLeft();
            }

        }
        this.balltime.set(tsync.getBalltime());

        if (tsync.getPenalties() != null) {
            tsync.getPenalties().parallelStream().forEach(E -> {
                if (E.msecs != 0) {
                    try {
                        Platform.runLater(() -> {
                            this.setPenalty(E.playerid, E.msecs);
                        });
                    } catch (NullPointerException ex) {
                        //no penalties, nothing to do
                    } catch (Exception ex) {
                        Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
    }

    public void sendPause() {
        //Building time syncronization structure to send back to the controller app
        ((WPTalkBackClient) ServiceHandler.getInstance().getThriftConnector().getClient()).pauseReceived(this.getTimeSync());
    }

    public StatusReport reportStatus() {
        StatusReport sr = new StatusReport();
        sr.setMatchprofile(ServiceHandler.getInstance().getOrganizer().getId());
        sr.setMatchphase(ServiceHandler.getInstance().getOrganizer().getCurrentPhase());
        sr.setLeftteam(this.leftteam.getTeamID());
        sr.setRightteam(this.rightteam.getTeamID());
        sr.setBalltimeleft(this.balltime.isLeftAttacking());
        this.leftteam.getPlayerStatus().stream().forEach(E -> sr.addToPlayerstat(E.toPlayerStat()));
        this.rightteam.getPlayerStatus().stream().forEach(E -> sr.addToPlayerstat(E.toPlayerStat()));
        sr.setTsync(this.getTimeSync());
        sr.setLeftteamtos(this.leftteam.getTimeouts());
        sr.setRightteamtos(this.rightteam.getTimeouts());

        return sr;
    }

    public void setStatus(StatusReport sr) {
        //this.clearTeams();
        this.timeengine.pause();
        try {
            this.loadLeftTeam(sr.getLeftteam());
            this.loadRightTeam(sr.getRightteam());            
            this.loadMatchProfile(sr.getMatchprofile());
            ServiceHandler.getInstance().getOrganizer().setCurrentPhase(sr.getMatchphase());
            ServiceHandler.getInstance().getOrganizer().setupPhase();

            if (sr.balltimeleft) {
                this.balltime.switchToLeft();
            } else {
                this.balltime.switchToRight();
            }
            sr.getPlayerstat().stream().forEach(E -> {

                this.setGoal(E.getPlayerid(), E.getNumgoals());
                this.setNumPenalties(E.getPlayerid(), E.getNumpenalties());
            });

            this.receiveSyncTime(sr.getTsync());
            this.leftteam.getTimeoutDisplay().setTimeouts(sr.getLeftteamtos());
            this.rightteam.getTimeoutDisplay().setTimeouts(sr.getRightteamtos());
            this.sendSyncTime();
        } catch (JSONException ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void setupPhase(MatchPhase mp) {
        this.mp = mp;
        this.pauseMatch();
        // ((WPTalkBackClient) ServiceHandler.getInstance().getThriftConnector().getClient()).sendLegTimeout();

        if (mp.getPhaseName().equals("Büntetők")) {
            if (this.leftteam.getTeamgoals().getValue().equals(this.rightteam.getTeamgoals().getValue())) {
                this.leginfo.setLegName(mp.getPhaseName());
                this.fiverswindow.loadLeftTeam(this.leftteam.getTeamId());
                this.fiverswindow.loadRightTeam(this.rightteam.getTeamId());
                this.fiverswindow.showIt();
            }
        } else {
            System.out.println("Setting up new phase: " + mp.toString());
            if (mp.wantDistinctTimeEngine()) {
                //If the phase needs its own independent timeengine then give it one and start immediatelly
                TimeEngine ti = new TimeEngine();
                ti.init();
                this.leginfo.setTimeEngine(ti);
                ti.start();
            } else {
                this.leginfo.setTimeEngine(this.timeengine);
            }
            this.leginfo.setTimeToCount(mp.getDuration());
            this.leginfo.resetTime();
            this.resetBallTime();
            this.sendSyncTime();
            this.leginfo.setLegName(mp.getPhaseName());
            this.leftteam.setAvailableTimeouts(mp.getAvailableTimeouts());
            this.rightteam.setAvailableTimeouts(mp.getAvailableTimeouts());
        }
        this.sendSyncTime();
        System.out.println("New phase set up");

    }

    public void setLegTime(int milisec) {
        this.leginfo.setTime(milisec);
        this.sendSyncTime();
    }

    public int getLegTime() {
        return this.leginfo.getTime();
    }

    public void nextPhase() {
        ServiceHandler.getInstance().getOrganizer().nextPhase(false);
        ((WPTalkBackClient) ServiceHandler.getInstance().getThriftConnector().getClient()).nextPhase();
        this.sendSyncTime();

    }

    public void prevPhase() {
        ServiceHandler.getInstance().getOrganizer().prevPhase();
        ((WPTalkBackClient) ServiceHandler.getInstance().getThriftConnector().getClient()).prevPhase();
        this.sendSyncTime();
    }

    @Override
    public void setTimeoutListener(TimeoutListener tl) {
        this.leginfo.setTimeoutListener(tl);
    }

    public void closeFivemWindow() {
        this.fiverswindow.close();
    }

    public void clearTeams() {
        this.leftteam.clearTeam();
        this.rightteam.clearTeam();
        this.leginfo.resetTime();
        this.leginfo.setLegName("");
        //ServiceHandler.getInstance().getTimeEngine().hibernate();
    }

    public void soundHorn() {
        ServiceHandler.getInstance().getHorn().honkShort();
    }

    @Override
    public void timeout() {
        this.pauseMatch();
        this.soundHorn();
        if (this.leginfo.getRemainingTime() > 0) {
            this.switchBallTime();
        }
    }

}
