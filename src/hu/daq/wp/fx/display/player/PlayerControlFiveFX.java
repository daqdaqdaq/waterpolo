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
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author DAQ
 */
public abstract class PlayerControlFiveFX extends PlayerDisplayFX {

    GlyphButton goalbutton;
    GlyphButton removegoalbutton;
    ColumnConstraints buttonholderconst = new ColumnConstraints();

    public PlayerControlFiveFX(Postgres db) {
        super(db);
        this.goalbutton = new GoalButton();

        this.removegoalbutton = new RemoveGoalButton();
 

        this.build();
    }

    public PlayerControlFiveFX(Postgres db, int player_id) {
        super(db, player_id);
        this.goalbutton = new GoalButton();
        this.removegoalbutton = new RemoveGoalButton();



        this.build();
    }

    public PlayerControlFiveFX(Player player) {
        super(player);
        Font sizeing = new Font(24);
        this.capnum_label = new Label();
        this.capnum_label.setFont(sizeing);
        this.name_label = new Label();
        this.name_label.setFont(sizeing);
        this.goals_label = new Label();
        this.goals_label.setFont(sizeing);
        this.goalbutton = new GoalButton();
        this.removegoalbutton = new RemoveGoalButton();
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

        
        buttonholderconst.setPercentWidth(30);
        capnumconst.setPercentWidth(5);
        nameconst.setPercentWidth(60);
        goalsconst.setPercentWidth(5);

        
        this.goalbutton.setOnAction((E) -> {
            ((WPController)ServiceHandler.getInstance().getThriftConnector().getClient()).addFivemGoal(this.getPlayerID());
            this.addGoal();
        });

        
        this.removegoalbutton.setOnAction((E) -> {
            ((WPController)ServiceHandler.getInstance().getThriftConnector().getClient()).removeFivemGoal(this.getPlayerID());
            this.removeGoal();
        });        
         this.buildLayout();
    }

}
