/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.player;

import client.Postgres;
import hu.daq.wp.Player;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

/**
 *
 * @author DAQ
 */
public class PlayerDisplayRightFX extends PlayerDisplayFX {

    public PlayerDisplayRightFX(Postgres db) {
        super(db);
        super.build();
    }

    public PlayerDisplayRightFX(Postgres db, int player_id) {
        super(db, player_id);
        super.build();
    }

    public PlayerDisplayRightFX(Player player) {
        super(player);
        super.build();
    }

    @Override
    protected void buildLayout() {
        GridPane gp = new GridPane();
        this.getChildren().add(gp);
        gp.setAlignment(Pos.CENTER);
        gp.setPadding(new Insets(5));
        gp.getColumnConstraints().addAll(capnumconst, penaltiesconst, goalsconst, nameconst);
        gp.add(this.capnum_label, 0, 0);
        gp.add(this.name_label, 3, 0);
        gp.add(this.penalties, 1, 0);
        gp.add(this.goals_label, 2, 0);
        GridPane.setHalignment(this.capnum_label, HPos.CENTER);
        GridPane.setHalignment(this.name_label, HPos.LEFT);
        GridPane.setHalignment(this.penalties, HPos.CENTER);
        GridPane.setHalignment(this.goals_label, HPos.CENTER);
        //BorderPane.setAlignment(this.capnum_label, Pos.CENTER);
        //BorderPane.setAlignment(this.name_label, Pos.CENTER_RIGHT);
        //BorderPane.setAlignment(this.penalties, Pos.CENTER);
        //BorderPane.setAlignment(this.goals_label, Pos.CENTER);        

    }

}
