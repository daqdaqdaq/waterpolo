/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.player;

import client.Postgres;
import hu.daq.wp.Player;
import hu.daq.wp.fx.display.buttonholder.ButtonHolder;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

/**
 *
 * @author DAQ
 */
public class PlayerControlRightFX extends PlayerControlFX {

    public PlayerControlRightFX() {
        super();
    }

    public PlayerControlRightFX(int player_id) {
        super(player_id);
    }

    public PlayerControlRightFX(Player player) {
        super(player);
    }

    @Override
    protected void buildLayout() {
        GridPane gp = new GridPane();
        StackPane.setAlignment(gp, Pos.CENTER);
        this.getChildren().add(gp);
        gp.setAlignment(Pos.CENTER);
        GridPane.setVgrow(this.penalties, Priority.NEVER);
        gp.setPadding(new Insets(5));
        gp.getColumnConstraints().addAll(buttonholderconst, capnumconst, penaltiesconst, goalsconst, nameconst);
        ButtonHolder buttons = new ButtonHolder();
        buttons.setSpacing(2);
        buttons.addVisibleButton(this.goalbutton);
        buttons.addVisibleButton(this.penaltybutton);
        buttons.addVisibleButton(this.showplayerbutton);
        buttons.addHiddenButton(this.removegoalbutton);
        buttons.addHiddenButton(this.removepenaltybutton);        
        HBox buttonholder = new HBox();
        buttonholder.setSpacing(10);
        buttonholder.setAlignment(Pos.CENTER);

        
        buttonholder.getChildren().addAll(buttons,this.td);

        gp.add(buttonholder, 0, 0);
        gp.add(this.capnum_label, 1, 0);
        gp.add(this.penalties, 2, 0);
        gp.add(this.goals_label, 3, 0);
        gp.add(this.name_label, 4, 0);

        GridPane.setHalignment(this.capnum_label, HPos.CENTER);
        GridPane.setHalignment(this.name_label, HPos.LEFT);
        GridPane.setHalignment(this.penalties, HPos.CENTER);
        GridPane.setHalignment(this.goals_label, HPos.CENTER);
        //GridPane.setAlignment(this.capnum_label, Pos.CENTER);
        //BorderPane.setAlignment(this.name_label, Pos.CENTER_LEFT);
        //BorderPane.setAlignment(this.penalties, Pos.CENTER);
        //BorderPane.setAlignment(this.goals_label, Pos.CENTER);        

    }

}
