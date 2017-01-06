/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.player;

import hu.daq.wp.Player;
import javafx.scene.layout.GridPane;

/**
 *
 * @author DAQ
 */
public class PlayerDisplayLeftFXDummy extends PlayerDisplayLeftFX{

    
    public PlayerDisplayLeftFXDummy(Integer capnum){
       super(new Player());
       this.player.setCapnum(capnum);
       this.setLayout();
    }
    
    protected void setLayout(){
        GridPane gp = (GridPane)this.getChildren().get(0);
        gp.getChildren().remove(this.name_label);
        gp.getChildren().remove(this.goals_label);
        gp.getChildren().remove(this.penalties);
        
    }
}
