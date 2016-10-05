/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.player;

import client.Postgres;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.thriftconnector.client.WPController;
import hu.daq.watch.CountdownWatch;
import hu.daq.watch.fx.TimeDisplay;
import hu.daq.watch.utility.WatchFactory;
import hu.daq.wp.Player;
import hu.daq.wp.fx.commonbuttons.GlyphButton;
import hu.daq.wp.fx.commonbuttons.GoalButton;
import hu.daq.wp.fx.commonbuttons.PenaltyButton;
import hu.daq.wp.fx.commonbuttons.RemoveGoalButton;
import hu.daq.wp.fx.commonbuttons.RemovePenaltyButton;
import hu.daq.wp.fx.commonbuttons.ShowPlayerButton;
import hu.daq.wp.fx.commonbuttons.ToggleGlyphButton;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 *
 * @author DAQ
 */
public abstract class PlayerControlFX extends PlayerDisplayFX {

    GlyphButton goalbutton;
    GlyphButton penaltybutton;
    GlyphButton removegoalbutton;
    GlyphButton removepenaltybutton;    
    ToggleGlyphButton showplayerbutton;
    
    CountdownWatch cw;
    TimeDisplay td;
    ColumnConstraints buttonholderconst = new ColumnConstraints();

    public PlayerControlFX(Postgres db) {
        super(db);
        this.setStyle("-fx-background-color: #77AACC;");
        this.goalbutton = new GoalButton();
        this.penaltybutton = new PenaltyButton();
        this.removegoalbutton = new RemoveGoalButton();
        this.removepenaltybutton = new RemovePenaltyButton();  
        this.showplayerbutton = new ShowPlayerButton();
        this.showplayerbutton.setUserData(this.player.getID());        
        this.cw = new CountdownWatch(ServiceHandler.getInstance().getTimeEngine(), 0, 0, 20);
        this.build();
    }

    public PlayerControlFX(Postgres db, int player_id) {
        super(db, player_id);
        this.setStyle("-fx-background-color: #77AACC;");
        this.goalbutton = new GoalButton();
        this.penaltybutton = new PenaltyButton();
        this.removegoalbutton = new RemoveGoalButton();
        this.removepenaltybutton = new RemovePenaltyButton();  
        this.showplayerbutton = new ShowPlayerButton();        
        this.showplayerbutton.setUserData(this.player.getID());
        this.cw = new CountdownWatch(ServiceHandler.getInstance().getTimeEngine(), 0, 0, 20);
        this.build();
    }

    public PlayerControlFX(Player player) {
        super(player);
        this.setStyle("-fx-background-color: #77AACC;");
        this.goalbutton = new GoalButton();
        this.penaltybutton = new PenaltyButton();
        this.removegoalbutton = new RemoveGoalButton();
        this.removepenaltybutton = new RemovePenaltyButton();  
        this.showplayerbutton = new ShowPlayerButton();        
        this.showplayerbutton.setUserData(this.player.getID());        
        this.cw = new CountdownWatch(ServiceHandler.getInstance().getTimeEngine(), 0, 0, 20);
        this.build();
    }

    //Only the layout differs in the left and right version
    @Override
    protected abstract void buildLayout();

    @Override
    protected void build() {
        this.setMaxHeight(USE_COMPUTED_SIZE);
        this.capnum_label.textProperty().bind(Bindings.createStringBinding(() -> {
            return this.player.getCapnum().getValue().toString() + ".";
        }, this.player.getCapnum()));
        this.name_label.textProperty().bind(this.player.getName());
        //this.name_label.setPrefWidth(150);
        this.goals_label.textProperty().bind(Bindings.createStringBinding(() -> {
            return this.goals.getValue().toString();
        }, this.goals));
        /*Make binding to the penalties. If the player has max number of penalties the whole player gets 
         0.5 opacity and control buttons get disabled
         */
        this.penalties.getFinallyout().addListener((ObservableValue<? extends Boolean> observable, Boolean ov, Boolean nv) -> {
            if (nv.equals(nv.equals(true))) {
                this.opacityProperty().set(0.5);
                this.penaltybutton.disableProperty().set(true);
                this.goalbutton.disableProperty().set(true);
            } else {
                this.opacityProperty().set(1);
                this.penaltybutton.disableProperty().set(false);
                this.goalbutton.disableProperty().set(false);
            }

        });
        
        buttonholderconst.setPercentWidth(30);
        capnumconst.setPercentWidth(5);
        nameconst.setPercentWidth(40);
        penaltiesconst.setPercentWidth(20);
        goalsconst.setPercentWidth(5);

        this.penaltybutton.setOnAction((E) -> {
            ((WPController)ServiceHandler.getInstance().getThriftConnector().getClient()).addPenalty(this.getPlayerID());
            this.addPenalty();
        });
        
        this.goalbutton.setOnAction((E) -> {
            ((WPController)ServiceHandler.getInstance().getThriftConnector().getClient()).addGoal(this.getPlayerID());
            this.addGoal();
        });
        this.removepenaltybutton.setOnAction((E) -> {
            ((WPController)ServiceHandler.getInstance().getThriftConnector().getClient()).removePenalty(this.getPlayerID());
            this.removePenalty();
        });
        
        this.removegoalbutton.setOnAction((E) -> {
            ((WPController)ServiceHandler.getInstance().getThriftConnector().getClient()).removeGoal(this.getPlayerID());
            this.removeGoal();
        });        
        this.cw.addTimeoutListener(this);
        this.td = WatchFactory.getWatchDisplay(this.cw);
        this.td.setFontSize(20);
        this.td.setVisible(false);
        this.td.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        this.buildLayout();
    }

    public ToggleGlyphButton getShowPlayerButton() {
        return showplayerbutton;
    }

    @Override
    protected void addOverlay() {
        this.penaltybutton.disableProperty().set(true);
        this.goalbutton.disableProperty().set(true);
        this.cw.reset();
        this.td.setVisible(true);
        this.cw.start();
    }

    @Override
    public void timeout() {
        this.penaltybutton.disableProperty().set(false);
        this.goalbutton.disableProperty().set(false);
        
        this.td.setVisible(false);
        this.cw.reset();
        //System.out.println("Time is running out");
        //this.getChildren().remove(1);
    }

    public void setTime(int milisec){
        this.cw.set(milisec);
    }
    
    @Override
    public int removePenalty(){
        this.cw.jumpToEnd();
        return super.removePenalty();
    }
    
    public Boolean isFinallyOut(){
        return this.penalties.getFinallyout().getValue();
    }
}
