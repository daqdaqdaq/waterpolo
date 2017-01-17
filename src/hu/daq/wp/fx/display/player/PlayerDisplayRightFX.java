/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.player;

import client.Postgres;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.wp.Player;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/**
 *
 * @author DAQ
 */
public class PlayerDisplayRightFX extends PlayerDisplayFX {

    public PlayerDisplayRightFX() {
        super();
        this.pfxo = new AdvancedPlayerFXDisplayOverlayRight(this, ServiceHandler.getInstance().getTimeEngine(), 20);
        super.build();
    }

    public PlayerDisplayRightFX(int player_id) {
        super(player_id);
        this.pfxo = new AdvancedPlayerFXDisplayOverlayRight(this, ServiceHandler.getInstance().getTimeEngine(), 20);
        super.build();
    }

    public PlayerDisplayRightFX(Player player) {
        super(player);
        this.pfxo = new AdvancedPlayerFXDisplayOverlayRight(this, ServiceHandler.getInstance().getTimeEngine(), 20);
        super.build();
    }

    @Override
    protected void buildLayout() {
        this.setMinWidth(210);        
        this.setMaxWidth(210); 
        this.setMaxHeight(50);
        this.setMinHeight(50);   
        Font sizeing = new Font(37);
        this.capnum_label.setFont(sizeing);
        this.name_label.setFont(sizeing);
        this.goals_label.setFont(sizeing);         
        capnumconst.setPercentWidth(30);
        penaltiesconst.setPercentWidth(42);
        goalsconst.setPercentWidth(28);         
        GridPane gp = new GridPane();
        this.getChildren().add(gp);
        gp.setAlignment(Pos.CENTER);
        gp.setPadding(new Insets(5));
        gp.getColumnConstraints().addAll(capnumconst, penaltiesconst, goalsconst);
        gp.add(this.capnum_label, 2, 0);
        gp.add(this.penalties, 1, 0);
        gp.add(this.goals_label, 0, 0);
        GridPane.setHalignment(this.capnum_label, HPos.CENTER);
        GridPane.setHalignment(this.penalties, HPos.CENTER);
        GridPane.setHalignment(this.goals_label, HPos.CENTER);
        //BorderPane.setAlignment(this.capnum_label, Pos.CENTER);
        //BorderPane.setAlignment(this.name_label, Pos.CENTER_RIGHT);
        //BorderPane.setAlignment(this.penalties, Pos.CENTER);
        //BorderPane.setAlignment(this.goals_label, Pos.CENTER);        

    }

}
