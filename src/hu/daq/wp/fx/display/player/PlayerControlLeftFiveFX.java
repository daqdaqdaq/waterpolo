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
public class PlayerControlLeftFiveFX extends PlayerControlFiveFX {

    public PlayerControlLeftFiveFX() {
        super();
    }

    public PlayerControlLeftFiveFX(int player_id) {
        super(player_id);
    }

    public PlayerControlLeftFiveFX(Player player) {
        super(player);
    }

    @Override
    protected void buildLayout() {
        this.setMaxWidth(400);
        this.setMinWidth(400);        
        GridPane gp = new GridPane();
        StackPane.setAlignment(gp, Pos.CENTER);
        this.getChildren().add(gp);
        gp.setPadding(new Insets(5));
        gp.setAlignment(Pos.CENTER);
        
        gp.getColumnConstraints().addAll(nameconst, goalsconst, capnumconst, buttonholderconst);
        HBox buttonholder = new HBox();
        buttonholder.setSpacing(10);
        //System.out.println("Adding buttons...");
        buttonholder.setAlignment(Pos.CENTER);
        buttonholder.getChildren().addAll(this.removegoalbutton,this.goalbutton);
        //System.out.println("Buttons added...");

        gp.add(buttonholder, 3, 0);
        gp.add(this.capnum_label, 2, 0);
        gp.add(this.goals_label, 1, 0);
        gp.add(this.name_label, 0, 0);

        GridPane.setHalignment(this.capnum_label, HPos.RIGHT);
        GridPane.setHalignment(this.name_label, HPos.RIGHT);

        GridPane.setHalignment(this.goals_label, HPos.CENTER);
        //GridPane.setAlignment(this.capnum_label, Pos.CENTER);
        //BorderPane.setAlignment(this.name_label, Pos.CENTER_LEFT);
        //BorderPane.setAlignment(this.penalties, Pos.CENTER);
        //BorderPane.setAlignment(this.goals_label, Pos.CENTER);        

    }

}
