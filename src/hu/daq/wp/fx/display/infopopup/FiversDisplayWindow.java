/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.infopopup;

import hu.daq.servicehandler.ServiceHandler;
import hu.daq.watch.fx.TimeDisplay;
import hu.daq.watch.utility.WatchFactory;
import hu.daq.wp.Team;
import hu.daq.wp.fx.display.player.PlayerDisplayFX;
import hu.daq.wp.fx.display.team.TeamDisplayLeftFiveFX;
import hu.daq.wp.fx.display.team.TeamDisplayRightFiveFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
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

/**
 *
 * @author DAQ
 */
public class FiversDisplayWindow extends PopupWindow {

    StackPane background;
    TeamDisplayLeftFiveFX leftteam;
    TeamDisplayRightFiveFX rightteam;
    DummyInstructable di;

    public FiversDisplayWindow() {
        super();

        this.background = new StackPane();
        this.leftteam = new TeamDisplayLeftFiveFX();
        this.rightteam = new TeamDisplayRightFiveFX();
        this.build();
        Scene scene = new Scene(this.background, 1000,500 );
        Color bgcolor = new Color(0.2,0.2,0.2,0.98);
        this.background.setBackground(new Background(new BackgroundFill(bgcolor,new CornerRadii(3), new Insets(5))));
        this.background.setBorder(new Border(new BorderStroke(new Color(0.4,0.4,0.4,0.7), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(5)))); 
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
        this.moveToSecondaryIfExists(this);          
        this.di = new DummyInstructable();
    }

    private void build() {
        this.background.setPadding(new Insets(30,50,30,50));
        VBox vb = new VBox(20);
        vb.setAlignment(Pos.CENTER);
        HBox hb = new HBox(60);
        Label header = new Label("Büntetők");
        header.setFont(new Font(70));
        VBox left = this.leftteam.getPlayerListView();
        left.setAlignment(Pos.CENTER);
        VBox right = this.rightteam.getPlayerListView();
        right.setAlignment(Pos.CENTER);
        HBox resbox = this.buildResultBox();
        resbox.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(left,right);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);
        vb.getChildren().addAll(header, resbox, hb);
        VBox.setMargin(hb, Insets.EMPTY);
        this.background.getChildren().add(vb);
        this.sizeToScene();

    }

    private HBox buildResultBox() {
        
        HBox resultbox = new HBox(10);
        Label left = this.leftteam.getGoalsLabel();
        left.setFont(new Font(80));
        Label right = this.rightteam.getGoalsLabel();
        right.setFont(new Font(80));        
        Label dash = new Label("-");
        dash.setFont(new Font(50));
        
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
        this.sizeToScene();
    }
    
    @Override
    public void close(){
        this.leftteam.clearAllPlayer();
        this.rightteam.clearAllPlayer();
        super.close();
        
    } 
    
}
