/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.screens;

import client.Postgres;
import hu.daq.dialog.DialogBuilder;
import hu.daq.draganddrop.DragObjectDecorator;
import hu.daq.draganddrop.DropObjectDecorator;
import hu.daq.fileservice.FileService;
import hu.daq.keyevent.KeyEventHandler;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.settings.SettingsHandler;
import hu.daq.thriftconnector.client.WPController;
import hu.daq.thriftconnector.talkback.StatusReport;
import hu.daq.thriftconnector.talkback.TimeSync;
import hu.daq.thriftconnector.thrift.FailedOperation;
import hu.daq.timeengine.TimeEngine;
import hu.daq.watch.TimeoutListener;
import hu.daq.wp.fx.MatchProfilesFX;
import hu.daq.wp.fx.commonbuttons.AddTeamButton;
import hu.daq.wp.fx.commonbuttons.DownloadButton;
import hu.daq.wp.fx.commonbuttons.EndMatchButton;
import hu.daq.wp.fx.commonbuttons.LeftButton;
import hu.daq.wp.fx.commonbuttons.RightButton;
import hu.daq.wp.fx.commonbuttons.TimeButton;
import hu.daq.wp.fx.commonbuttons.UploadButton;
import hu.daq.wp.fx.display.balltime.BallTime;
import hu.daq.wp.fx.display.buttonholder.ButtonHolder;
import hu.daq.wp.fx.display.controlpanel.ControlPanel;
import hu.daq.wp.fx.display.infopopup.PopupCloseListener;
import hu.daq.wp.fx.display.penaltytimer.PenaltyHolder;
import hu.daq.wp.fx.display.penaltytimer.PenaltyTimer;
import hu.daq.wp.fx.display.player.PlayerControlFX;
import hu.daq.wp.fx.display.team.TeamControlFX;
import hu.daq.wp.fx.display.team.TeamControlLeftFX;
import hu.daq.wp.fx.display.team.TeamControlRightFX;
import hu.daq.wp.fx.screens.teamselector.TeamSelectorWindow;
import hu.daq.wp.matchorganizer.MatchPhase;
import hu.daq.wp.matchorganizer.Organizable;
import hu.daq.wp.matchorganizer.OrganizerBuilder;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.json.JSONException;

/**
 *
 * @author DAQ
 */
public class MatchScreen extends BorderPane implements SubScreen, Organizable, PopupCloseListener {

    SimpleBooleanProperty isdraw;
    private MainPageCommon scparent;
    private String securitytoken;
    private final Boolean adminonly;
    private final Postgres db;
    private final TeamControlFX leftteam;
    private final TeamControlFX rightteam;
    private final TimeButton leftteamtime;
    private final TimeButton rightteamtime;
    private TeamSelectorWindow teamselector;
    private final AddTeamButton addteam;
    private final ControlPanel controlpanel;
    private final EndMatchButton stopmatch;
    private final UploadButton sendstatus;
    private final DownloadButton getstatus;
    private final LeftButton prevphase;
    private final RightButton nextphase;
    private final Button attackerpenaltybutton;
    private final Button defenderpenaltybutton;
    private final Button doublepenaltybutton;
    private MatchProfilesFX matchprofile;
    private TimeEngine timeengine;
    private ToggleGroup showplayertoggle;
    private FiversControlWindow fivers;
    private PenaltyHolder ph;
    private MatchPhase mp;

    public MatchScreen(Postgres db) {
        this.db = db;
        this.adminonly = false;
        //this.teamselector = new TeamListFX(this.db);    
        //this.teamselector.setVisible(false);
        this.leftteam = new TeamControlLeftFX();
        this.rightteam = new TeamControlRightFX();
        this.rightteamtime = new TimeButton();
        this.leftteamtime = new TimeButton();
        this.stopmatch = new EndMatchButton();
        this.sendstatus = new UploadButton();
        this.getstatus = new DownloadButton();
        this.addteam = new AddTeamButton();
        this.matchprofile = new MatchProfilesFX();
        this.controlpanel = new ControlPanel();
        this.fivers = new FiversControlWindow();
        this.prevphase = new LeftButton();
        this.nextphase = new RightButton();
        //blind buttons. We dont need them, only the mechanism behind them
        this.attackerpenaltybutton = new Button();
        this.defenderpenaltybutton = new Button();
        this.doublepenaltybutton = new Button();
        this.fivers.setCloseListener(this);
        this.ph = new PenaltyHolder();
        this.timeengine = ServiceHandler.getInstance().getTimeEngine();
        this.isdraw = new SimpleBooleanProperty(true);
        this.isdraw.bind(Bindings.equal(this.leftteam.getTeamgoals(), this.rightteam.getTeamgoals()));
        this.build();
    }

    private void build() {
        //this.teamselector.loadTeams();
        DropObjectDecorator.decorate(this.rightteam.getPlayerListView(), this.rightteam, DataFormat.PLAIN_TEXT, TransferMode.MOVE);
        DropObjectDecorator.decorate(this.leftteam.getPlayerListView(), this.leftteam, DataFormat.PLAIN_TEXT, TransferMode.MOVE);

        this.addteam.setOnAction((E) -> {
            this.showTeamSelector();
        });
        VBox mainbox = new VBox();
        mainbox.setFillWidth(true);
        HBox teamsbox = new HBox();
        teamsbox.prefWidthProperty().bind(this.widthProperty());
        VBox ltv = leftteam.getPlayerListView();
        //ltv.setMaxWidth(Double.MAX_VALUE);
        VBox rtv = rightteam.getPlayerListView();
        //rtv.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(ltv, Priority.SOMETIMES);
        HBox.setHgrow(rtv, Priority.SOMETIMES);
        teamsbox.getChildren().addAll(ltv, rtv);
        //mainbox.getChildren().addAll(this.teamselector,this.addteam,teamsbox);
        this.controlpanel.setAlignment(Pos.CENTER);
        mainbox.getChildren().addAll(this.buildUpperBox(), teamsbox);
        this.setCenter(mainbox);
        this.controlpanel.disable();
        this.stopmatch.setDisable(true);
        this.stopmatch.setOnAction((ActionEvent E) -> {
            this.startstopMatch();
        });
        this.sendstatus.setOnAction((ev) -> {
            this.sendStatusReport();
        });
        this.getstatus.setOnAction((ev) -> {
            this.getStatusReport();
        });

        this.leftteam.getLoaded().addListener((Observable observable) -> {
            this.controlStartStopButton();
        });
        this.rightteam.getLoaded().addListener((Observable observable) -> {
            this.controlStartStopButton();
        });

        this.matchprofile.getSelectionModel().selectedItemProperty().addListener((Observable observable) -> {
            this.controlStartStopButton();
        });

        this.leftteamtime.setOnAction((ev) -> {
            this.controlpanel.pause();
            this.leftteam.getTimeoutDisplay().addMatchTimeout(this.mp.getPhaseNum());
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).requestTimeOut(this.leftteam.getTeamID());
        });

        this.rightteamtime.setOnAction((ev) -> {
            this.controlpanel.pause();
            this.rightteam.getTimeoutDisplay().addMatchTimeout(this.mp.getPhaseNum());
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).requestTimeOut(this.rightteam.getTeamID());
        });

        this.prevphase.setOnAction((ev) -> {
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).prevPhase();
        });

        this.nextphase.setOnAction((ev) -> {
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).nextPhase();
        });

        this.attackerpenaltybutton.setOnAction((ev) -> {
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).attackerPenalty();
            this.attackerPenalty();
        });

        this.defenderpenaltybutton.setOnAction((ev) -> {
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).defenderPenalty();
            this.defenderPenalty();
        });

        this.doublepenaltybutton.setOnAction((ev) -> {
            this.doublePenalty();
        });
        KeyEventHandler keh = ServiceHandler.getInstance().getKeyEventHandler();
        SettingsHandler sh = ServiceHandler.getInstance().getSettings();
        keh.bindButton(sh.getProperty("key_attackerpenalty"), this.attackerpenaltybutton);
        keh.bindButton(sh.getProperty("key_defenderpenalty"), this.defenderpenaltybutton);
        keh.bindButton(sh.getProperty("key_doublepenalty"), this.doublepenaltybutton);
        keh.bindButton(sh.getProperty("key_leftrequesttime"), this.leftteamtime);
        keh.bindButton(sh.getProperty("key_rightrequesttime"), this.rightteamtime);
    }

    private HBox buildUpperBox() {
        HBox ub = new HBox(5);

        AnchorPane leftteaminfo = new AnchorPane();
        Label lgoalslabel = this.leftteam.getGoalsLabel();
        leftteaminfo.getChildren().add(lgoalslabel);
        AnchorPane.setBottomAnchor(lgoalslabel, 10.0);
        AnchorPane.setRightAnchor(lgoalslabel, 150.0);
        leftteaminfo.getChildren().add(this.leftteamtime);
        AnchorPane.setBottomAnchor(this.leftteamtime, 10.0);
        AnchorPane.setRightAnchor(this.leftteamtime, 10.0);
        HBox obb = this.buildOtherButtonBox();
        leftteaminfo.getChildren().add(obb);
        AnchorPane.setTopAnchor(obb, 5.0);
        AnchorPane.setLeftAnchor(obb, 10.0);

        AnchorPane rightteaminfo = new AnchorPane();
        Label rgoalslabel = this.rightteam.getGoalsLabel();
        rightteaminfo.getChildren().add(rgoalslabel);
        AnchorPane.setBottomAnchor(rgoalslabel, 10.0);
        AnchorPane.setLeftAnchor(rgoalslabel, 150.0);
        rightteaminfo.getChildren().add(this.rightteamtime);
        AnchorPane.setBottomAnchor(this.rightteamtime, 10.0);
        AnchorPane.setLeftAnchor(this.rightteamtime, 10.0);

        HBox.setHgrow(leftteaminfo, Priority.ALWAYS);
        HBox.setHgrow(rightteaminfo, Priority.ALWAYS);

        ub.getChildren().addAll(leftteaminfo, this.controlpanel, rightteaminfo);
        return ub;
    }

    private HBox buildOtherButtonBox() {
        HBox obb = new HBox();
        VBox vb = new VBox(3);
        HBox upperhb = new HBox(3);
        HBox lowerhb = new HBox(3);
        ButtonHolder bh = new ButtonHolder();
        bh.addVisibleButton(this.stopmatch);
        bh.addHiddenButton(this.getstatus);
        bh.addHiddenSpacer();
        bh.addHiddenButton(this.sendstatus);
        upperhb.getChildren().addAll(this.addteam, this.prevphase, this.nextphase);
        lowerhb.getChildren().addAll(this.matchprofile, bh);
        vb.getChildren().addAll(upperhb, lowerhb);
        obb.getChildren().addAll(vb, this.ph);
        return obb;
    }

    @Override
    public SubScreen addContainer(MainPageCommon nd) {
        this.scparent = nd;
        return this;
    }

    @Override
    public MainPageCommon getContainer() {
        return this.scparent;
    }

    @Override
    public void initScreen() {
        this.matchprofile.load();
    }

    private void showTeamSelector() {
        if (this.teamselector == null) {
            this.teamselector = new TeamSelectorWindow(this.db);
            this.addteam.disableProperty().bind(this.teamselector.showingProperty());
            this.teamselector.loadTeams();
        }

        this.teamselector.show();

        //this.teamselector.setVisible(true);
        //this.teamselector.loadTeams();
    }

    private void startstopMatch() {
        //If the control panel is disabled assume there is no match in progress
        if (this.controlpanel.disabledProperty().getValue().equals(true)) {
            String token = this.db.query("select gettoken() as token").get(0).get("token");
            try {
                String user = ServiceHandler.getInstance().getThriftConnector().getClient().connect(token);
                this.readyMatch();
            } catch (FailedOperation ex) {
                Alert errdialog = DialogBuilder.getErrorDialog("A kijelző nem elérhető", ex.errormsg);
                errdialog.showAndWait();
            } catch (JSONException ex) {
                Alert errdialog = DialogBuilder.getErrorDialog("Meccs profil hiba", "A meccs profil betöltése sikertelen");
                errdialog.showAndWait();
            }

        } else {
            this.endMatch();
        }
    }

    private void controlStartStopButton() {
        if (this.leftteam.isLoaded() && this.rightteam.isLoaded() && this.matchprofile.isSelected()) {
            this.stopmatch.setDisable(false);
        } else {
            this.stopmatch.setDisable(true);

        }
    }

    private void readyMatch() throws JSONException {
        ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).readyMatch(this.leftteam.getTeamID(), this.rightteam.getTeamID(), this.matchprofile.getSelected().getID());
        ServiceHandler.getInstance().setOrganizer(OrganizerBuilder.build(this.matchprofile.getSelected(), this));
        this.controlpanel.getBallTime().setTimeToCount(ServiceHandler.getInstance().getOrganizer().getBallTimeInSecs());
        ServiceHandler.getInstance().getOrganizer().setCurrentPhase(0);
        ServiceHandler.getInstance().getOrganizer().setupPhase();
        this.scparent.setPlayingTeams(this.leftteam.getTeamID(), this.rightteam.getTeamID());
        this.leftteam.getTimeoutDisplay().setUp(ServiceHandler.getInstance().getOrganizer());
        this.rightteam.getTimeoutDisplay().setUp(ServiceHandler.getInstance().getOrganizer());
        this.leftteam.enable();
        this.rightteam.enable();
        this.controlpanel.enable();
    }

    private void endMatch() {
        this.controlpanel.disable();
        this.leftteam.deleteTeam();
        this.rightteam.deleteTeam();
        ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).clearScoreBoard();
    }

    public void sendStatusReport() {
        Optional<ButtonType> response = DialogBuilder.getConfirmDialog("Szinkronizáció", "Biztosan a vezérlőhöz akarod szinkronizálni a kijelzőt?").showAndWait();

        if (response.get().equals(ButtonType.OK)) {
            try {
                String token = this.db.query("select gettoken() as token").get(0).get("token");
                ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).connect(token);
            } catch (FailedOperation ex) {
                Logger.getLogger(MatchScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
            //((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).readyMatch(this.leftteam.getTeamID(), this.rightteam.getTeamID(), this.matchprofile.getSelected().getID());
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).sendStatus(this.reportStatus());
        }
    }

    public void getStatusReport() {
        Optional<ButtonType> response = DialogBuilder.getConfirmDialog("Szinkronizáció", "Biztosan a kijelzőhöz akarod szinkronizálni a vezérlőt?").showAndWait();

        if (response.get().equals(ButtonType.OK)) {
            try {
                String token = this.db.query("select gettoken() as token").get(0).get("token");
                ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).connect(token);
            } catch (FailedOperation ex) {
                Logger.getLogger(MatchScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.setStatus(((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).getStatus());
        }

    }

    public StatusReport reportStatus() {
        StatusReport sr = new StatusReport();
        sr.setMatchprofile(ServiceHandler.getInstance().getOrganizer().getId());
        sr.setMatchphase(ServiceHandler.getInstance().getOrganizer().getCurrentPhase());
        sr.setLeftteam(this.leftteam.getTeamID());
        sr.setRightteam(this.rightteam.getTeamID());
        sr.setBalltimeleft(this.controlpanel.getBallTime().isLeftAttacking());
        this.leftteam.getPlayerStatus().stream().forEach(E -> sr.addToPlayerstat(E.toPlayerStat()));
        this.rightteam.getPlayerStatus().stream().forEach(E -> sr.addToPlayerstat(E.toPlayerStat()));
        sr.setTsync(this.getTimeSync());
        sr.setLeftteamtos(this.leftteam.getTimeouts());
        sr.setRightteamtos(this.rightteam.getTimeouts());
        return sr;
    }

    public void setStatus(StatusReport sr) {
        this.leftteam.deleteTeam();
        this.rightteam.deleteTeam();
        try {
            ServiceHandler.getInstance()
                    .setOrganizer(
                            OrganizerBuilder.build(
                                    ServiceHandler.getInstance().getDbService().getMatchProfile(sr.getMatchprofile()), this));
            ServiceHandler.getInstance().getOrganizer().setCurrentPhase(sr.getMatchphase());
            this.leftteam.load(sr.getLeftteam());
            this.rightteam.load(sr.getRightteam());
            if (sr.balltimeleft) {
                this.controlpanel.getBallTime().switchToLeft();
            } else {
                this.controlpanel.getBallTime().switchToRight();
            }
            sr.getPlayerstat().stream().forEach(E -> {
                this.getPlayer(E.getPlayerid()).setGoal(E.getNumgoals());
                this.getPlayer(E.getPlayerid()).setNumPenalties(E.getNumpenalties());
            });
            this.receiveSyncTime(sr.getTsync());
            this.leftteam.getTimeoutDisplay().setTimeouts(sr.getLeftteamtos());
            this.rightteam.getTimeoutDisplay().setTimeouts(sr.getRightteamtos());
            this.scparent.setPlayingTeams(this.leftteam.getTeamID(), this.rightteam.getTeamID());
            this.leftteam.enable();
            this.rightteam.enable();
            this.controlpanel.enable();
        } catch (JSONException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }

    private TimeSync getTimeSync() {
        TimeSync tsync = new TimeSync();
        tsync.setBalltime(this.controlpanel.getBallTime().getMilis());
        tsync.setLegtime(this.controlpanel.getLegInfo().getTime());
        tsync.setAttacking(this.controlpanel.getBallTime().isRightAttacking() ? "R" : "L");
        this.leftteam.getPlayerStatus().stream().forEach(E -> tsync.addToPenalties(E.toPenaltyTime()));
        this.rightteam.getPlayerStatus().stream().forEach(E -> tsync.addToPenalties(E.toPenaltyTime()));
        return tsync;
    }

    public void receiveSyncTime(TimeSync tsync) {
        Platform.runLater(() -> {

            this.controlpanel.setLegTime(tsync.getLegtime());
            BallTime bt = this.controlpanel.getBallTime();
            if (tsync.getAttacking().equals("R")) {
                if (bt.isLeftAttacking()) {
                    bt.switchToRight();
                }

            }
            if (tsync.getAttacking().equals("L")) {
                if (bt.isRightAttacking()) {
                    bt.switchToLeft();
                }

            }
            this.controlpanel.setBallTimeClock(tsync.getBalltime());
        });

        if (tsync.getPenalties() != null) {
            tsync.getPenalties().parallelStream().forEach(E -> {
                Platform.runLater(() -> {
                    try {
                        this.getPlayer(E.playerid).setTime(E.msecs);
                    } catch (NullPointerException ex) {
                        //no penalties, nothing to do
                    }
                });

            });
        }
    }

    public void pauseReceived(TimeSync tsync) {
        this.controlpanel.pause();
        this.receiveSyncTime(tsync);
    }

    public PlayerControlFX getPlayer(int playerid) {
        PlayerControlFX res;

        res = (PlayerControlFX) this.leftteam.getPlayer(playerid);
        if (res != null) {
            return res;
        }
        res = (PlayerControlFX) this.rightteam.getPlayer(playerid);
        return res;
    }

    @Override
    public Boolean isAdminOnly() {
        return this.adminonly;
    }

    public void attackerPenalty() {
        this.ph.addPenaltyTimer(new PenaltyTimer(ServiceHandler.getInstance().getTimeEngine()));
    }

    public void defenderPenalty() {
        this.ph.addPenaltyTimer(new PenaltyTimer(ServiceHandler.getInstance().getTimeEngine()));
    }

    public void doublePenalty() {
        this.ph.addPenaltyTimer(new PenaltyTimer(ServiceHandler.getInstance().getTimeEngine()));
        this.ph.addPenaltyTimer(new PenaltyTimer(ServiceHandler.getInstance().getTimeEngine()));
    }

    @Override
    public void setupPhase(MatchPhase mp) {
        this.mp = mp;
        this.controlpanel.pause();
        if (mp.getPhaseName().equals("Büntetők")) {
            if (this.leftteam.getTeamgoals().getValue().equals(this.rightteam.getTeamgoals().getValue())) {
                this.controlpanel.setLegName(mp.getPhaseName());
                this.fivers.loadLeftTeam(this.leftteam.getTeamID());
                this.fivers.loadRightTeam(this.rightteam.getTeamID());
                this.leftteam.getPlayers().stream().forEach(E -> {
                    DragObjectDecorator.decorate((PlayerControlFX) E, new DragPlayerMediator((PlayerControlFX) E), TransferMode.MOVE);
                });
                this.rightteam.getPlayers().stream().forEach(E -> {
                    DragObjectDecorator.decorate((PlayerControlFX) E, new DragPlayerMediator((PlayerControlFX) E), TransferMode.MOVE);
                });
                Platform.runLater(() -> {
                    this.fivers.showIt();
                });

            }
        } else {
            if (mp.wantDistinctTimeEngine()) {
                //If the phase needs its own independent timeengine then give it one and start immediatelly
                TimeEngine ti = new TimeEngine();
                ti.init();
                this.controlpanel.getLegInfo().setTimeEngine(ti);
                ti.start();
            } else {
                this.controlpanel.getLegInfo().setTimeEngine(this.timeengine);
            }
            Platform.runLater(() -> {
                this.controlpanel.setTimeToCount(mp.getDuration());
                this.controlpanel.setLegName(mp.getPhaseName());
            });

        }
    }


    /*          this.leginfo.setTimeToCount(mp.getDuration());
     this.leginfo.resetTime();
     this.resetBallTime();
     this.receiveSyncTime();
     this.leginfo.setLegName(mp.getPhaseName());
     this.leftteam.setAvailableTimeouts(mp.getAvailableTimeouts());
     this.rightteam.setAvailableTimeouts(mp.getAvailableTimeouts());
     */
    public void nextPhase() {
        ServiceHandler.getInstance().getOrganizer().nextPhase(true);
    }

    public void prevPhase() {
        ServiceHandler.getInstance().getOrganizer().prevPhase();
    }

    @Override
    public void setTimeoutListener(TimeoutListener tl) {

    }

    @Override
    public void cleanupAfterPopup() {
        this.fivers.clearAllPlayer();
        ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).closeFivemWindow();
    }

}
