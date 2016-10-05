/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.screens;

import hu.daq.draganddrop.DropObjectDecorator;
import hu.daq.wp.fx.display.infopopup.*;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.watch.fx.TimeDisplay;
import hu.daq.watch.utility.WatchFactory;
import hu.daq.wp.Team;
import hu.daq.wp.fx.display.player.PlayerDisplayFX;
import hu.daq.wp.fx.display.team.TeamControlLeftFiveFX;
import hu.daq.wp.fx.display.team.TeamControlRightFiveFX;
import hu.daq.wp.fx.display.team.TeamDisplayLeftFiveFX;
import hu.daq.wp.fx.display.team.TeamDisplayRightFiveFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

/**
 *
 * @author DAQ
 */
public class FiversControlWindow extends PopupWindow {

    BorderPane background;
    TeamControlLeftFiveFX leftteam;
    TeamControlRightFiveFX rightteam;
    DummyInstructable di;

    public FiversControlWindow() {
        super();
        this.background = new BorderPane();
        this.leftteam = new TeamControlLeftFiveFX(ServiceHandler.getInstance().getDb());
        this.rightteam = new TeamControlRightFiveFX(ServiceHandler.getInstance().getDb());
        this.build();
        Scene scene = new Scene(this.background);
        this.initStyle(StageStyle.UTILITY);
        //scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
        this.di = new DummyInstructable();

    }

    private void build() {
        this.background.setPadding(new Insets(10,10,10,10));
        DropObjectDecorator.decorate(this.rightteam.getPlayerListView(), this.rightteam, DataFormat.PLAIN_TEXT, TransferMode.MOVE);
        DropObjectDecorator.decorate(this.leftteam.getPlayerListView(), this.leftteam, DataFormat.PLAIN_TEXT, TransferMode.MOVE);        
        VBox vb = new VBox(20);
        vb.setAlignment(Pos.CENTER);
        HBox hb = new HBox(10);
        VBox.setVgrow(hb, Priority.NEVER);
        Text header = new Text("Büntetők");
        header.setFont(new Font(30));
        VBox left = this.leftteam.getPlayerListView();
        VBox right = this.rightteam.getPlayerListView();
        HBox resbox = this.buildResultBox();
        hb.getChildren().addAll(left,resbox,right);
        HBox.setHgrow(left, Priority.SOMETIMES);
        HBox.setHgrow(right, Priority.SOMETIMES);
        HBox.setHgrow(resbox, Priority.SOMETIMES);        
        
        vb.getChildren().addAll(header, hb);
        VBox.setMargin(hb, Insets.EMPTY);
        this.background.setCenter(vb);
        this.sizeToScene();        
       

    }

    private HBox buildResultBox() {
        
        HBox resultbox = new HBox();
        Label left = this.leftteam.getGoalsLabel();
        left.setFont(new Font(30));
        Label right = this.rightteam.getGoalsLabel();
        right.setFont(new Font(30));        
        Label dash = new Label("-");
        dash.setFont(new Font(30));
        
        resultbox.getChildren().addAll(left,dash,right);    
        return resultbox;
    }

    public void loadLeftTeam(Integer teamid) {
        this.leftteam.load(teamid);
    }

    public void loadRightTeam(Integer teamid) {
        this.rightteam.load(teamid);
    }
    
    public PlayerDisplayFX getPlayer(Integer playerid) throws Exception {
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
    
    public void addPlayer(Integer playerid){
        
        //Add a player to the five meters roster.
        //Try to add to both team, the wrong team will fail silently
        this.leftteam.addPlayer(playerid);
        this.rightteam.addPlayer(playerid);
    
    }
    
}
