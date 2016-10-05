/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.team;

import client.Postgres;
import hu.daq.wp.Team;
import hu.daq.wp.fx.display.player.PlayerDisplayFX;
import hu.daq.wp.fx.display.player.PlayerDisplayLeftFX;
import java.util.stream.Collectors;
import javafx.collections.transformation.SortedList;

/**
 *
 * @author DAQ
 */
public class TeamDisplayLeftFX extends TeamDisplayFX{

    public TeamDisplayLeftFX(Postgres db) {
        super(db);
    }

    public TeamDisplayLeftFX(Postgres db, int team_id) {
        super(db, team_id);
    }

    public TeamDisplayLeftFX(Team team) {
        super(team);
    }

    /**
     *
     */
    @Override
    protected void loadPlayers() {
        this.active_players.clear();
        this.active_players.addAll(this.team.getActivePlayers()
                .stream().map(PlayerDisplayLeftFX::new)
                .collect(Collectors.toList()));
        SortedList<PlayerDisplayFX> p = new SortedList<PlayerDisplayFX>(this.active_players); 
        p.setComparator((l, r) -> l.compareTo(r));
        this.playerlist.getChildren().setAll(p);

    }


    
}
