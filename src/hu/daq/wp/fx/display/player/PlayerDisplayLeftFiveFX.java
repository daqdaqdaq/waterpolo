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
import javafx.scene.layout.StackPane;

/**
 *
 * @author DAQ
 */
public class PlayerDisplayLeftFiveFX extends PlayerDisplayFX{

    public PlayerDisplayLeftFiveFX(Postgres db) {
        super(db);
        super.build();
    }

    public PlayerDisplayLeftFiveFX(Postgres db, int player_id) {
        super(db, player_id);
        super.build();
    }

    public PlayerDisplayLeftFiveFX(Player player) {
        super(player);
        super.build();
    }

    @Override
    protected void buildLayout() {
        
        capnumconst.setPercentWidth(13);
        nameconst.setPercentWidth(67);
        penaltiesconst.setPercentWidth(5);
        goalsconst.setPercentWidth(15);        
        GridPane gp = new GridPane();
        StackPane.setAlignment(gp, Pos.CENTER);
        this.getChildren().add(gp);
        gp.setAlignment(Pos.CENTER);
        gp.setPadding(new Insets(5));
        gp.getColumnConstraints().addAll(nameconst,goalsconst,penaltiesconst,capnumconst);
        gp.add(this.capnum_label, 3, 0);
        gp.add(this.goals_label, 1, 0);
        gp.add(this.name_label, 0, 0);
        GridPane.setHalignment(this.capnum_label, HPos.CENTER);  
        GridPane.setHalignment(this.name_label, HPos.RIGHT);
        
        GridPane.setHalignment(this.goals_label, HPos.CENTER);
        
        //GridPane.setAlignment(this.capnum_label, Pos.CENTER);
        //BorderPane.setAlignment(this.name_label, Pos.CENTER_LEFT);
        //BorderPane.setAlignment(this.penalties, Pos.CENTER);
        //BorderPane.setAlignment(this.goals_label, Pos.CENTER);        
        
    }
    
}
