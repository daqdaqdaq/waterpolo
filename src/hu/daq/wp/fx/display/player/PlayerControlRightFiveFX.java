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
public class PlayerControlRightFiveFX extends PlayerControlFiveFX {

    public PlayerControlRightFiveFX(Postgres db) {
        super(db);
    }

    public PlayerControlRightFiveFX(Postgres db, int player_id) {
        super(db, player_id);
    }

    public PlayerControlRightFiveFX(Player player) {
        super(player);
    }

    @Override
    protected void buildLayout() {
        GridPane gp = new GridPane();
        StackPane.setAlignment(gp, Pos.CENTER);
        this.getChildren().add(gp);
        gp.setPadding(new Insets(5));
        gp.setAlignment(Pos.CENTER);
        
        gp.getColumnConstraints().addAll(buttonholderconst, capnumconst, goalsconst, nameconst);
        HBox buttonholder = new HBox();
        buttonholder.setSpacing(10);
        //System.out.println("Adding buttons...");
        buttonholder.setAlignment(Pos.CENTER);
        buttonholder.getChildren().addAll(this.goalbutton,this.removegoalbutton);
        //System.out.println("Buttons added...");

        gp.add(buttonholder, 0, 0);
        gp.add(this.capnum_label, 1, 0);
        gp.add(this.goals_label, 2, 0);
        gp.add(this.name_label, 3, 0);        

        GridPane.setHalignment(this.capnum_label, HPos.CENTER);
        GridPane.setHalignment(this.name_label, HPos.RIGHT);
        GridPane.setHalignment(this.goals_label, HPos.CENTER);
     

    }

}
