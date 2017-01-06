/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.team;

import client.Postgres;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.wp.Team;
import hu.daq.wp.fx.EntityFX;
import hu.daq.wp.fx.display.infopopup.TimeOutWindow;
import hu.daq.wp.fx.display.player.PlayerDisplayFX;
import hu.daq.wp.fx.display.timeouts.TimeoutDisplay;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;

/**
 *
 * @author DAQ
 */
public abstract class TeamDisplayFX extends EntityFX<Team>{

    Team team;
    Label goalslabel;
    Label teamnamelabel;
    ObservableList<PlayerDisplayFX> active_players;
    SimpleIntegerProperty teamgoals;
    VBox playerlist;
    TimeoutDisplay tod;
    Integer reaminingtimeouts = 0;

    public TeamDisplayFX() {
        this(new Team());
        
    }

    public TeamDisplayFX(int team_id) {
        this.load(team_id);
    }

    public TeamDisplayFX(Team team) {
        this.teamgoals = new SimpleIntegerProperty(0);
        //this.active_players = new SortedList<PlayerDisplayFX>(FXCollections.observableList(new ArrayList<PlayerDisplayFX>()));
        this.active_players = FXCollections.observableList(new ArrayList<PlayerDisplayFX>());
        //this.active_players .setComparator((l, r) -> l.compareTo(r));
        this.playerlist = new VBox(1);
        this.playerlist.setMinWidth(400);
        //this.playerlist.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        this.playerlist.setPrefHeight(USE_COMPUTED_SIZE);
        this.teamnamelabel = new Label("");
        //this.teamnamelabel.setWrapText(true);
        this.teamnamelabel.setTextAlignment(TextAlignment.CENTER);
        this.teamnamelabel.setFont(new Font(24));
        this.goalslabel = new Label("");
        this.goalslabel.setFont(new Font(24));
        this.setEntity(team);
        tod = new TimeoutDisplay();

    }
   @Override
    protected void setEntity(Team entity) {
        this.team = entity;
        this.teamnamelabel.textProperty().bind(this.getTeamName());
        this.goalslabel.textProperty().bind(Bindings.createStringBinding(() -> this.teamgoals.getValue().toString(), this.teamgoals));
        
    }
    
    
    public Integer getTeamId() {
        return this.team.getID();
    }

    public final boolean load(Integer pk) {
        System.out.println("Loading:" + pk);
        this.setEntity(ServiceHandler.getInstance().getDbService().getTeam(pk));

        this.loadPlayers();
        this.active_players.stream().forEach((PlayerDisplayFX E) -> {
            E.getGoals().addListener((ObservableValue<? extends Number> observable, Number ov, Number nv) -> {
                this.teamgoals.setValue(this.teamgoals.get() - (int) ov + (int) nv);
                //System.out.println("Changed:"+ov.toString()+"->"+nv.toString());
            });
        });
        return true;

    }

    protected abstract void loadPlayers();

    public SimpleStringProperty getTeamName() {
        return this.team.getTeamname();
    }

    public VBox getPlayerListView() {

        return this.playerlist;
    }

    public Label getTeamNameLabel() {
        return this.teamnamelabel;
    }

    public Label getGoalsLabel() {

        return this.goalslabel;
    }

    public SimpleIntegerProperty getTeamgoals() {
        return teamgoals;
    }

    public PlayerDisplayFX getPlayer(Integer id) {
        return this.active_players.stream().filter(E -> E.getPlayerID() == id).findAny().orElse(null);
    }

    public HashMap<Integer, Integer> getPlayersInPenalty() {
        HashMap<Integer, Integer> res = new HashMap<Integer, Integer>();
        //this.active_players.stream().filter(E -> (E.getPenaltyTime() > 0)).forEach(E -> res.put(E.getPlayerID(), E.getPenaltyTime()));
        //New version put every player into the penalties struct. 0 time to the players who aren't in penalty
        this.active_players.stream().forEach(E -> res.put(E.getPlayerID(), E.getPenaltyTime()));
        return res;
    }

    public void removeAllPenalties() {
        this.active_players.stream().filter(E -> (E.getPenaltyTime() > 0)).forEach(E -> {
            E.endPenalty();
        });
    }

    public void setAvailableTimeouts(int timeouts) {
        this.reaminingtimeouts = timeouts;
    }

    public void requestTimeout(Integer phasenum) {
        if (this.reaminingtimeouts > 0) {
            ServiceHandler.getInstance().getTimeEngine().pause();
            ServiceHandler.getInstance().getHorn().honkShort();
            this.reaminingtimeouts--;
            if (phasenum > 10) {
                this.tod.addOvertimeTimeout(phasenum - 10);
            } else {
                this.tod.addMatchTimeout(phasenum);
            }
            TimeOutWindow tow = new TimeOutWindow(60);
            tow.loadTeam(this.team);
            tow.showIt();
        }
    }

    public void clearTeam() {
        this.team.reset();
        this.playerlist.getChildren().clear();
        this.active_players.clear();
        this.teamgoals.set(0);

    }

    public TimeoutDisplay getTimeoutDisplay() {
        return tod;
    }

    @Override
    public String getID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getIDInt() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean isDragable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getTeamID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void editOn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void onDelete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 

}
