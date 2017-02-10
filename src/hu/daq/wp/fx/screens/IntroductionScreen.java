/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.screens;

import client.Postgres;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.thriftconnector.client.WPController;
import hu.daq.wp.Coach;
import hu.daq.wp.Team;
import hu.daq.wp.fx.AdvancedTeamFX;
import hu.daq.wp.fx.CoachFX;
import hu.daq.wp.fx.EntityFX;
import hu.daq.wp.fx.PlayerFX;
import hu.daq.wp.fx.RefereePosition;
import hu.daq.wp.fx.commonbuttons.AddPlayerButton;
import hu.daq.wp.fx.commonbuttons.DeleteButton;
import hu.daq.wp.fx.commonbuttons.LeftButton;
import hu.daq.wp.fx.commonbuttons.ResetButton;
import hu.daq.wp.fx.commonbuttons.RightButton;
import hu.daq.wp.fx.screens.entityselector.EntitySelectorWindow;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author DAQ
 */
public class IntroductionScreen extends StackPane implements SubScreen {

    private MainPageCommon parent;
    private Postgres db;
    private VBox rightteam;
    private VBox leftteam;
    private RefereePosition firstreferee;
    private RefereePosition secondreferee;
    private RefereePosition inspector;
    ArrayList<EntityFX> introductionlist;
    private ResetButton refreshteams;
    private RightButton rightbutton;
    private LeftButton leftbutton;
    private DeleteButton finishbutton;
    private AddPlayerButton openentities;
    private EntitySelectorWindow esw;
    private Label currentname;
    private int currentpos = -1;

    public IntroductionScreen(Postgres db) {
        this.db = db;
        this.esw = new EntitySelectorWindow();
        this.rightteam = new VBox(1);
        this.leftteam = new VBox(1);
        this.firstreferee = new RefereePosition("Játékvezető");
        this.secondreferee = new RefereePosition("Játékvezető");
        this.inspector = new RefereePosition("Ellenőr");
        this.introductionlist = new ArrayList<EntityFX>();
        this.refreshteams = new ResetButton();
        this.rightbutton = new RightButton();
        this.leftbutton = new LeftButton();
        this.finishbutton = new DeleteButton();
        this.openentities = new AddPlayerButton();
        this.currentname = new Label();
        this.build();
    }

    private void build() {
        this.currentname.setFont(new Font(30));
        this.currentname.setMinWidth(300);
        this.currentname.setAlignment(Pos.CENTER);
        this.refreshteams.setOnAction((ev) -> {
            this.loadTeams(this.parent.getLeftTeam(), this.parent.getRightTeam());
        });

        this.openentities.setOnAction((ev) -> {
            this.esw.loadEntities();
            this.esw.show();
        });

        this.rightbutton.setOnAction((ev) -> {
            if (this.introductionlist.isEmpty()){
                this.populateIntroductionList();
            }
            this.showNext();
        });

        this.leftbutton.setOnAction((ev) -> {
            if (this.introductionlist.isEmpty()){
                this.populateIntroductionList();
            }            
            this.showPrev();
        });

        this.finishbutton.setOnAction((ev) -> {
            this.currentpos = -1;
            this.introductionlist.clear();
            this.leftteam.getChildren().clear();
            this.rightteam.getChildren().clear();
            this.firstreferee.clearCoach();
            this.secondreferee.clearCoach();
            this.inspector.clearCoach();
            this.currentname.setText("");
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).hidePlayerInfo();
        });

        VBox mainbox = new VBox();
        HBox buttons = new HBox(5);
        buttons.getChildren().addAll(this.refreshteams, this.openentities, this.leftbutton, this.rightbutton, this.finishbutton);

        VBox refbox = new VBox(3);
        refbox.setAlignment(Pos.CENTER);
        HBox upperrefs = new HBox(3);
        upperrefs.setAlignment(Pos.CENTER);
        upperrefs.getChildren().addAll(this.firstreferee, this.secondreferee);
        VBox.setVgrow(this.inspector, Priority.NEVER);
        VBox.setVgrow(upperrefs, Priority.NEVER);
        refbox.getChildren().addAll(upperrefs, this.inspector);
        HBox teams = new HBox(3);
        teams.setAlignment(Pos.CENTER);
        teams.getChildren().addAll(this.leftteam, this.currentname, this.rightteam);
        mainbox.getChildren().addAll(buttons, refbox, teams);
        mainbox.setAlignment(Pos.TOP_CENTER);
        this.getChildren().add(mainbox);
    }

    public void showNext() {
        if (this.currentpos < this.introductionlist.size() - 1) {
            this.currentpos++;
            if (!this.showCurrent()){
                this.showNext();
            }
        }
    }

    public void showPrev() {
        if (this.currentpos > 0) {
            this.currentpos--;
            if (!this.showCurrent()){
                this.showPrev();
            }
        }
    }

    private boolean showCurrent() {
        if (this.introductionlist.get(currentpos) != null) {
            String t = this.introductionlist.get(currentpos).getClass().getSimpleName();
            Integer id = this.introductionlist.get(currentpos).getIDInt();
            this.currentname.setText(this.introductionlist.get(currentpos).getName());
            if (t.equals("PlayerFX")) {
                ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).showPlayerInfo(id);
            } else if (t.equals("CoachFX")) {
                ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).showCoachInfo(id);
            } else if (t.equals("RefereeFX")) {
                ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).showRefereeInfo(id);
            }
            return true;
        } else {
            return false;
        }
    }

    public void loadTeams(Integer leftteamid, Integer rightteamid) {
        //this.leftteam.getChildren().clear();
        //this.rightteam.getChildren().clear();
        if (rightteamid != null && leftteamid != null) {

            ServiceHandler.getInstance().getDbService().getActivePlayersOfTeam(leftteamid).stream().forEach(E -> {
                PlayerFX p = new PlayerFX(E);
                this.leftteam.getChildren().add(p);

            });
            Coach coach;
            //Team t = ServiceHandler.getInstance().getDbService().getTeam(leftteamid);
            coach = ServiceHandler.getInstance().getDbService().getCoachOfTeam(leftteamid);
            if (coach != null) {
                CoachFX c = new CoachFX(coach);
                this.leftteam.getChildren().add(c);

            }
            

            ServiceHandler.getInstance().getDbService().getActivePlayersOfTeam(rightteamid).stream().forEach(E -> {
                PlayerFX p = new PlayerFX(E);
                this.rightteam.getChildren().add(p);
            });
            
            coach = ServiceHandler.getInstance().getDbService().getCoachOfTeam(rightteamid);
            if (coach != null) {
                CoachFX c = new CoachFX(coach);
                this.rightteam.getChildren().add(c);

            }            
        }
        //this.populateIntroductionList();
    }

    private void populateIntroductionList(){
        this.introductionlist.clear();
        this.leftteam.getChildren().forEach(E ->{this.introductionlist.add((EntityFX)E);});
        this.rightteam.getChildren().forEach(E ->{this.introductionlist.add((EntityFX)E);});
        this.introductionlist.add(this.firstreferee.getReferee());
        this.introductionlist.add(this.secondreferee.getReferee());
        this.introductionlist.add(this.inspector.getReferee());         
    }
    
    @Override
    public void initScreen() {

    }

    @Override
    public Boolean isAdminOnly() {
        return false;
    }

    @Override
    public SubScreen addContainer(MainPageCommon nd) {
        this.parent = nd;
        return this;
    }

    @Override
    public MainPageCommon getContainer() {
        return this.parent;
    }

}
