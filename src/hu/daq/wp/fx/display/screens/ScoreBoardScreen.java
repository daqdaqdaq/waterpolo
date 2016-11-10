/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.screens;

import hu.daq.wp.fx.display.control.ControlledScreen;
import client.Postgres;
import hu.daq.UDPSender.TimeSender;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.thriftconnector.talkback.PenaltyTime;
import hu.daq.thriftconnector.talkback.TimeSync;
import hu.daq.thriftconnector.talkback.WPTalkBackClient;
import hu.daq.timeengine.TimeEngine;
import hu.daq.watch.TimeoutListener;
import hu.daq.wp.MatchProfile;
import hu.daq.wp.fx.display.balltime.BallTime;
import hu.daq.wp.fx.display.balltime.BallTimeDisplay;
import hu.daq.wp.fx.display.infopopup.FiversDisplayWindow;
import hu.daq.wp.fx.display.infopopup.GoalPopup;
import hu.daq.wp.fx.display.infopopup.PlayerInfo;
import hu.daq.wp.fx.display.leginfo.LegInfo;
import hu.daq.wp.fx.display.player.PlayerDisplayFX;
import hu.daq.wp.fx.display.team.TeamDisplayFX;
import hu.daq.wp.fx.display.team.TeamDisplayLeftFX;
import hu.daq.wp.fx.display.team.TeamDisplayRightFX;
import hu.daq.wp.matchorganizer.MatchPhase;
import hu.daq.wp.matchorganizer.Organizable;
import hu.daq.wp.matchorganizer.OrganizerBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
    PlayerInfo pi;
    FiversDisplayWindow fiverswindow;
    TimeSender timesender;
    VBox leftteambox;
    VBox rightteambox;
    //private final static int LEGTIME = 8; 
    private final static int BALLTIME = 30;

    public ScoreBoardScreen(Postgres db) {

        this.setPrefSize(1024, 768);
        this.db = db;
        this.leftteam = new TeamDisplayLeftFX(this.db);
        this.rightteam = new TeamDisplayRightFX(this.db);
        this.timeengine = ServiceHandler.getInstance().getTimeEngine();
        this.timeengine.pause();
        this.balltime = new BallTimeDisplay(this.timeengine, BALLTIME);
        this.balltime.getWatch().addTimeoutListener(this);
        this.timesender = new TimeSender(this.balltime.getWatch(), ServiceHandler.getInstance().getSenderthread());
        this.leginfo = new LegInfo(this.timeengine);
        this.pi = new PlayerInfo();
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
        VBox mainbox = new VBox(5);
        //mainbox.setStyle("-fx-background-color: #77AACC;");
        //mainbox.setFillWidth(true);
        mainbox.setMinWidth(1024);
        HBox databox = this.buildDataBox();
        databox.prefWidthProperty().bind(this.widthProperty());
        HBox playersbox = this.buildPlayersBox();
        playersbox.prefWidthProperty().bind(this.widthProperty());
        mainbox.getChildren().addAll(databox, playersbox);
        //VBox.setVgrow(this, Priority.NEVER);
        this.setCenter(mainbox);

        //AnchorPane.setTopAnchor(mainbox, 0.0);
    }

    private HBox buildDataBox() {
        HBox databox = new HBox(10);
        databox.setId("databox");
        databox.setPadding(new Insets(0, 5, 0, 5));
        //databox.setStyle("-fx-background-color: #181818;");
        VBox leftteambox = this.buildScoreAndNameBox(leftteam);
        VBox rightteambox = this.buildScoreAndNameBox(rightteam);
        VBox centerinfobox = this.buildCenterInfoBox();
        //The width of the scorebox is 3/8 (0.375)of the width of the whole box
        leftteambox.prefWidthProperty().bind(Bindings.multiply(0.375, databox.widthProperty()));
        rightteambox.prefWidthProperty().bind(Bindings.multiply(0.375, databox.widthProperty()));

        databox.getChildren().addAll(leftteambox,
                centerinfobox,
                rightteambox);
        HBox.setHgrow(centerinfobox, Priority.ALWAYS);
        //HBox.setHgrow(rightteambox, Priority.ALWAYS);         

        return databox;
    }

    private VBox buildScoreAndNameBox(TeamDisplayFX team) {
        VBox scoreandname = new VBox(20);
        scoreandname.setAlignment(Pos.BOTTOM_CENTER);
        Label teamname = team.getTeamNameLabel();
        teamname.setFont(new Font(45));
        Label teamscore = team.getGoalsLabel();
        teamscore.setFont(new Font(60));
        scoreandname.getChildren().addAll(teamname,
                teamscore);
        return scoreandname;
    }

    private VBox buildCenterInfoBox() {
        VBox centerinfobox = new VBox(10);
        centerinfobox.setAlignment(Pos.CENTER);
        //Label locationnamelabel = new Label("Dunaújváros");
        //locationnamelabel.setFont(new Font(10));
        TimeEngine datetime = new TimeEngine();
        datetime.init();
        StackPane centertimebox = new StackPane();
        centertimebox.setPrefSize(100, 100);

        this.balltime.setFont(new Font(60));
        this.balltime.setTeamNodes(this.leftteambox, this.rightteambox);
        //this.balltime.fillHeightProperty().set(true);

        centertimebox.getChildren().add(this.balltime);
        centerinfobox.getChildren().addAll(
                //locationnamelabel,
                //     new DateFX(datetime, new Font(5)),
                //     new TimeFX(datetime, new Font(5)),
                this.leginfo,
                this.balltime);
        return centerinfobox;
    }

    private HBox buildPlayersBox() {
        HBox playersbox = new HBox(40);
        VBox leftteambox = this.leftteam.getPlayerListView();
        VBox rightteambox = this.rightteam.getPlayerListView();
        playersbox.getChildren().addAll(leftteambox,
                rightteambox);
        HBox.setHgrow(leftteambox, Priority.ALWAYS);
        HBox.setHgrow(rightteambox, Priority.ALWAYS);
        return playersbox;
    }

    public void loadLeftTeam(Integer teamid) {
        this.leftteam.load(teamid);
    }

    public void loadRightTeam(Integer teamid) {
        this.rightteam.load(teamid);
    }

    public void loadMatchProfile(Integer profileid) throws JSONException {
        MatchProfile mp = new MatchProfile(this.db);
        mp.load(profileid);
        ServiceHandler.getInstance().setOrganizer(OrganizerBuilder.build(mp, this));
        ServiceHandler.getInstance().getOrganizer().setCurrentPhase(0);
        ServiceHandler.getInstance().getOrganizer().setupPhase();
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

    public void addPenalty(Integer playerid) {
        try {
            this.getPlayer(playerid).addPenalty();
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
            this.getPlayer(playerid).addGoal();
            GoalPopup pi = new GoalPopup();

            pi.loadPlayer(this.getPlayer(playerid).getPlayerModel());
            pi.setTimer(3);
            pi.showIt();
        } catch (Exception ex) {
            Logger.getLogger(ScoreBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.timeengine.pause();
        this.pauseMatch();
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
        this.syncTime();
    }

    public void switchBallTimeLeft() {
        //this.timeengine.pause();
        //End all penalties in the defending team
        this.leftteam.removeAllPenalties();
        this.balltime.switchToLeft();
        if (this.balltime.getRemainingTime() > this.leginfo.getRemainingTime()) {
            this.balltime.set(this.leginfo.getRemainingTime());
        }
        this.syncTime();
    }

    public void switchBallTimeRight() {
        //this.timeengine.pause();
        //End all penalties in the defending team        
        this.rightteam.removeAllPenalties();
        this.balltime.switchToRight();

        if (this.balltime.getRemainingTime() > this.leginfo.getRemainingTime()) {
            this.balltime.set(this.leginfo.getRemainingTime());
        }
        this.syncTime();
    }

    public void switchBallTime() {
        //this.timeengine.pause();
        //End all penalties in the defending team
        if (this.balltime.isRightAttacking()){
            this.leftteam.removeAllPenalties();
        } else {
            this.rightteam.removeAllPenalties();
        }
        this.balltime.switchTeam();
        
        if (this.balltime.getRemainingTime() > this.leginfo.getRemainingTime()) {
            this.balltime.set(this.leginfo.getRemainingTime());
        }
        this.syncTime();
    }

    public void resetBallTime() {
        //this.timeengine.pause();
        this.balltime.reset();
        if (this.balltime.getRemainingTime() > this.leginfo.getRemainingTime()) {
            this.balltime.set(this.leginfo.getRemainingTime());
        }
        this.syncTime();

    }

    public void showPlayerInfo(Integer playerid) {
        try {
            this.pi.loadPlayer(this.getPlayer(playerid).getPlayerModel());
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
        if (this.leftteam.getTeamId().equals(teamid)) {
            this.leftteam.requestTimeout();
        } else {
            this.rightteam.requestTimeout();
        }
    }

    public void syncTime() {
        //Building time syncronization structure to send back to the controller app
        TimeSync tsync = new TimeSync();
        tsync.setBalltime(this.balltime.getMilis());
        tsync.setLegtime(this.leginfo.getTime());
        tsync.setAttacking(this.balltime.isRightAttacking()?"R":"L");
        this.leftteam.getPlayersInPenalty().entrySet().stream().forEach(E -> tsync.addToPenalties(new PenaltyTime(E.getKey(), E.getValue())));
        this.rightteam.getPlayersInPenalty().entrySet().stream().forEach(E -> tsync.addToPenalties(new PenaltyTime(E.getKey(), E.getValue())));
        //And sending it back via the Thrift connection
        ((WPTalkBackClient) ServiceHandler.getInstance().getThriftConnector().getClient()).syncTime(tsync);
    }

    public void sendPause() {
        //Building time syncronization structure to send back to the controller app
        TimeSync tsync = new TimeSync();
        tsync.setBalltime(this.balltime.getMilis());
        tsync.setLegtime(this.leginfo.getTime());
        tsync.setAttacking(this.balltime.isRightAttacking()?"R":"L");        
        this.leftteam.getPlayersInPenalty().entrySet().stream().forEach(E -> tsync.addToPenalties(new PenaltyTime(E.getKey(), E.getValue())));
        this.rightteam.getPlayersInPenalty().entrySet().stream().forEach(E -> tsync.addToPenalties(new PenaltyTime(E.getKey(), E.getValue())));
        //And sending it back via the Thrift connection
        ((WPTalkBackClient) ServiceHandler.getInstance().getThriftConnector().getClient()).pauseReceived(tsync);
    }    
    
    
    @Override
    public void setupPhase(MatchPhase mp) {
        this.pauseMatch();
        ((WPTalkBackClient) ServiceHandler.getInstance().getThriftConnector().getClient()).sendLegTimeout();

        if (mp.getPhaseName().equals("Büntetők")) {
            this.leginfo.setLegName(mp.getPhaseName());
            this.fiverswindow.loadLeftTeam(this.leftteam.getTeamId());
            this.fiverswindow.loadRightTeam(this.rightteam.getTeamId());
            this.fiverswindow.showIt();
        } else {
            System.out.println("Setting up new phase: "+mp.toString());
            this.leginfo.setTimeToCount(mp.getDuration());
            System.out.println("Duration is set");
            this.leginfo.resetTime();
            System.out.println("Leginfo: "+this.leginfo.getRemainingTime());
            this.resetBallTime();
            this.syncTime();
            System.out.println("Balltime reseted");
            this.leginfo.setLegName(mp.getPhaseName());
            this.leftteam.setAvailableTimeouts(mp.getAvailableTimeouts());
            this.rightteam.setAvailableTimeouts(mp.getAvailableTimeouts());
            System.out.println("Leginfo: "+this.leginfo.getRemainingTime());            
            if (mp.wantDistinctTimeEngine()){
                System.out.println("Substitute TimeEngine has been made");
                //If the phase needs its own independent timeengine then give it one and start immediatelly
                TimeEngine ti = new TimeEngine();
                this.leginfo.setTimeEngine(ti);                
                System.out.println("Substitute Time engine is set");
                ti.start();
                System.out.println("Substitute is started");
                
            } else {
                this.leginfo.setTimeEngine(this.timeengine);
            System.out.println("Original Time engine is set");                
            }
        }
        this.syncTime();
        System.out.println("New phase set up");

    }
    
    
    public void setLegTime(int milisec){
        this.leginfo.setTime(milisec);
        this.syncTime();        
    }
    
    public void nextPhase(){
        ServiceHandler.getInstance().getOrganizer().nextPhase();
        ((WPTalkBackClient) ServiceHandler.getInstance().getThriftConnector().getClient()).sendLegTimeout();

    }

    public void prevPhase(){
        ServiceHandler.getInstance().getOrganizer().prevPhase();
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
        ServiceHandler.getInstance().getTimeEngine().hibernate();
    }

    public void soundHorn() {
        ServiceHandler.getInstance().getHorn().honk();
    }

    @Override
    public void timeout() {
        //this.pauseMatch();
        this.soundHorn();
        if (this.leginfo.getRemainingTime()>0){
            this.switchBallTime();
        }
    }

}
