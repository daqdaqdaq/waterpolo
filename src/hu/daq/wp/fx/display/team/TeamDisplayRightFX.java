/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.team;

import client.Postgres;
import hu.daq.wp.Team;
import hu.daq.wp.fx.display.player.PlayerDisplayFX;
import hu.daq.wp.fx.display.player.PlayerDisplayRightFX;
import java.util.stream.Collectors;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;

/**
 *
 * @author DAQ
 */
public class TeamDisplayRightFX extends TeamDisplayFX{

    public TeamDisplayRightFX(Postgres db) {
        super(db);
        this.playerlist.setMinWidth(200);         
        this.playerlist.setAlignment(Pos.TOP_RIGHT);        
    }

    public TeamDisplayRightFX(Postgres db, int team_id) {
        super(db, team_id);
        this.playerlist.setMinWidth(200);         
        this.playerlist.setAlignment(Pos.TOP_RIGHT);          
    }

    public TeamDisplayRightFX(Team team) {
        super(team);
        this.playerlist.setMinWidth(200);         
        this.playerlist.setAlignment(Pos.TOP_RIGHT);          
    }

    /**
     *
     */
    @Override
    protected void loadPlayers() {
        this.active_players.clear();
        this.active_players.addAll(this.team.getActivePlayers()
                .stream().map(PlayerDisplayRightFX::new)
                .collect(Collectors.toList()));
        SortedList<PlayerDisplayFX> p = new SortedList<PlayerDisplayFX>(this.active_players); 
        p.setComparator((l, r) -> l.compareTo(r));
        this.playerlist.getChildren().setAll(p);
    }


    
}
