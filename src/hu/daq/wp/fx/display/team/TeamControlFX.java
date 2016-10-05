/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.team;

import client.Postgres;
import hu.daq.draganddrop.ObjectReceiver;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.thriftconnector.client.WPController;
import hu.daq.wp.Team;
import hu.daq.wp.fx.TeamSimpleFX;
import hu.daq.wp.fx.commonbuttons.DeleteButton;
import hu.daq.wp.fx.commonbuttons.GlyphButton;
import hu.daq.wp.fx.display.player.PlayerControlFX;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author DAQ
 */
public abstract class TeamControlFX extends TeamDisplayFX implements ObjectReceiver {

    VBox container = new VBox();
    Label teamname;
    GlyphButton delteambutton;
    TeamSimpleFX dragsource;
    SimpleBooleanProperty loaded;
    ToggleGroup showplayertoggle;

    public TeamControlFX(Postgres db) {
        super(db);
        this.showplayertoggle = new ToggleGroup();
        this.build();

    }

    public TeamControlFX(Postgres db, int team_id) {
        super(db, team_id);
        this.showplayertoggle = new ToggleGroup();        
        this.build();
        

    }

    public TeamControlFX(Team team) {
        super(team);
        this.showplayertoggle = new ToggleGroup();        
        this.build();
    }

    private void build() {
        this.loaded = new SimpleBooleanProperty();
        this.playerlist.setMinHeight(600);
        this.delteambutton = new DeleteButton();
        this.delteambutton.setDisable(true);
        this.teamname = new Label();
        this.teamname.setFont(new Font(20));
        this.teamname.textProperty().bind(this.team.getTeamname());
        HBox namecontainer = new HBox();
        this.delteambutton.setOnAction(E -> {
            this.deleteTeam();
        });
        /*Show player toggle
          Show the selected player, or hide everything if there is no player selected
        */
        this.showplayertoggle.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle ov, Toggle nv) -> {
            if (nv != null) {
                ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).showPlayerInfo((Integer) nv.getUserData());
            } else {
                ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).hidePlayerInfo();
            }
        });

        namecontainer.getChildren().addAll(this.delteambutton, this.teamname);
        this.container.getChildren().addAll(namecontainer, this.playerlist);
        this.container.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
    }

    public void deleteTeam() {
        //this.dragsource.setInuse(Boolean.FALSE);
        this.loaded.set(false);
        this.team.reset();

        this.playerlist.getChildren().clear();
        this.delteambutton.disableProperty().set(true);
        this.dragsource.setInuse(Boolean.FALSE);

    }

    @Override
    public VBox getPlayerListView() {

        return this.container;
    }

    @Override
    public void handleObject(Object source, String object) {
        System.out.println("Handling...." + object + ":" + source);
        /*
         Drag between windows a bit akward and sets the dragsource to null
         In that case try to get the drag source from the ServiceHandler. Don't forget to store it in there!!!        
         */
        if (source == null) {
            source = ServiceHandler.getInstance().getDragSource();
        }
        this.dragsource = ((ListCell<TeamSimpleFX>) source).getItem();
        this.load(Integer.parseInt(object));
        this.delteambutton.disableProperty().set(false);
    }

    public void disable() {
        this.playerlist.setDisable(true);
    }

    public void enable() {
        this.playerlist.setDisable(false);
    }

    public Boolean isLoaded() {
        return !this.playerlist.getChildren().isEmpty();
    }

    public SimpleBooleanProperty getLoaded() {
        return loaded;
    }

    public Integer getTeamID() {
        return this.team.getID();
    }
    
    public List<PlayerControlFX> getPlayers(){
        //ArrayList<PlayerControlFX> players = new ArrayList<PlayerControlFX>();
        return this.active_players.stream().map((E)->((PlayerControlFX)E)).collect(Collectors.toList());
    
    }
}
