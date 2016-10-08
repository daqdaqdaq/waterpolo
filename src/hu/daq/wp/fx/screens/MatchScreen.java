/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.screens;

import client.Postgres;
import hu.daq.draganddrop.DragObjectDecorator;
import hu.daq.draganddrop.DropObjectDecorator;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.thriftconnector.client.WPController;
import hu.daq.thriftconnector.talkback.TimeSync;
import hu.daq.thriftconnector.thrift.FailedOperation;
import hu.daq.watch.TimeoutListener;
import hu.daq.wp.fx.commonbuttons.AddTeamButton;
import hu.daq.wp.fx.commonbuttons.EndMatchButton;
import hu.daq.wp.fx.commonbuttons.TimeButton;
import hu.daq.wp.fx.display.controlpanel.ControlPanel;
import hu.daq.wp.fx.display.infopopup.PopupCloseListener;
import hu.daq.wp.fx.display.player.PlayerControlFX;
import hu.daq.wp.fx.display.team.TeamControlFX;
import hu.daq.wp.fx.display.team.TeamControlLeftFX;
import hu.daq.wp.fx.display.team.TeamControlRightFX;
import hu.daq.wp.fx.screens.teamselector.TeamSelectorWindow;
import hu.daq.wp.matchorganizer.MatchPhase;
import hu.daq.wp.matchorganizer.Organizable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author DAQ
 */
public class MatchScreen extends BorderPane implements SubScreen, Organizable, PopupCloseListener {

    private MainPage parent;
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
    private ToggleGroup showplayertoggle;
    private FiversControlWindow fivers;

    public MatchScreen(Postgres db) {
        this.db = db;
        this.adminonly = false;
        //this.teamselector = new TeamListFX(this.db);    
        //this.teamselector.setVisible(false);
        this.leftteam = new TeamControlLeftFX(this.db);
        this.rightteam = new TeamControlRightFX(this.db);
        this.rightteamtime = new TimeButton();
        this.leftteamtime = new TimeButton();
        this.stopmatch = new EndMatchButton();
        this.addteam = new AddTeamButton();
        this.controlpanel = new ControlPanel();
        this.fivers = new FiversControlWindow();
        this.fivers.setCloseListener(this);
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
        this.leftteam.getLoaded().addListener((Observable observable) -> {
            this.controlStartStopButton();
        });
        this.rightteam.getLoaded().addListener((Observable observable) -> {
            this.controlStartStopButton();
        });
        this.leftteamtime.setOnAction((ev) -> {
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).requestTimeOut(this.leftteam.getTeamID());
        });
        this.rightteamtime.setOnAction((ev) -> {
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).requestTimeOut(this.rightteam.getTeamID());
        });
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
        HBox obb = new HBox(3);
        obb.getChildren().addAll(this.addteam, this.stopmatch);

        return obb;
    }

    @Override
    public SubScreen addContainer(MainPage nd) {
        this.parent = nd;
        return this;
    }

    @Override
    public MainPage getContainer() {
        return this.parent;
    }

    @Override
    public void initScreen() {
        String token = this.db.query("select gettoken() as token").get(0).get("token");
        try {
            String user = ServiceHandler.getInstance().getThriftConnector().getClient().connect(token);
            /*
             try {
             String user = this.parent.getThriftConnector().connect(token);
             this.securitytoken = token;
             this.parent.getThriftConnector().getClient().loadteams(this.securitytoken, 1, 2);
             this.parent.getThriftConnector().getClient().goal(this.securitytoken,3);
             this.parent.getThriftConnector().getClient().foul(token, 3);
            
             //System.out.println("USSSSSEEEEEERRRR:"+user);
             } catch (FailedOperation ex) {
             ex.printStackTrace();
             //Logger.getLogger(MatchScreen.class.getName()).log(Level.SEVERE, null, ex);
             } catch (TException ex) {
             ex.printStackTrace();
             }
             */
        } catch (FailedOperation ex) {
            Logger.getLogger(MatchScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

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
            this.readyMatch();
        } else {
            this.endMatch();
        }
    }

    private void controlStartStopButton() {
        if (this.leftteam.isLoaded() && this.rightteam.isLoaded()) {
            this.stopmatch.setDisable(false);
        } else {
            this.stopmatch.setDisable(true);

        }
    }

    private void readyMatch() {
        ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).loadTeam(this.leftteam.getTeamID(), this.rightteam.getTeamID());
        ServiceHandler.getInstance().getOrganizer().setCurrentPhase(-1);
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

    public void pauseReceived(TimeSync tsync) {
        System.out.println("MS sending pause..");
        //Pause main time engine
        this.controlpanel.pause();
        //and synchronize each clock to to the scoreboard
        System.out.println("MS sending set..");
        Platform.runLater(() -> {
            this.controlpanel.setBallTimeClock(tsync.getBalltime());
            this.controlpanel.setLegTime(tsync.getLegtime());

        });
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

    @Override
    public void setupPhase(MatchPhase mp) {
        this.controlpanel.pause();
        if (mp.getPhaseName().equals("Büntetők")) {
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

        } else {
            Platform.runLater(() -> {
                this.controlpanel.setTimeToCount(mp.getDuration());
                this.controlpanel.setLegName(mp.getPhaseName());
            });

        }
    }

    @Override
    public void setTimeoutListener(TimeoutListener tl) {

    }

    @Override
    public void cleanupAfterPopup() {
        ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).closeFivemWindow();
    }

}
