/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.team;

import client.Postgres;
import hu.daq.wp.Team;
import hu.daq.wp.fx.display.player.PlayerControlFX;
import hu.daq.wp.fx.display.player.PlayerControlLeftFX;
import hu.daq.wp.fx.display.player.PlayerDisplayFX;
import java.util.stream.Collectors;
import javafx.collections.transformation.SortedList;

/**
 *
 * @author DAQ
 */
public class TeamControlLeftFX extends TeamControlFX{

    public TeamControlLeftFX(Postgres db) {
        super(db);
    }

    public TeamControlLeftFX(Postgres db, int team_id) {
        super(db, team_id);
    }

    public TeamControlLeftFX(Team team) {
        super(team);
    }

    /**
     *
     */
    @Override
    protected void loadPlayers() {
        this.active_players.clear();
        this.active_players.addAll(this.team.getActivePlayers()
                .stream().map(PlayerControlLeftFX::new)
                .collect(Collectors.toList()));
        this.active_players.stream().forEach((PlayerDisplayFX E)->{((PlayerControlFX)E).getShowPlayerButton().setToggleGroup(showplayertoggle); });
        SortedList<PlayerDisplayFX> p = new SortedList<PlayerDisplayFX>(this.active_players); 
        p.setComparator((l, r) -> l.compareTo(r));
        
        this.playerlist.getChildren().setAll(p);
        this.disable();
        this.loaded.set(true);

    }


    
}
