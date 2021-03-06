/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.team;

import client.Postgres;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.wp.Team;
import hu.daq.wp.fx.display.player.PlayerDisplayFX;
import hu.daq.wp.fx.display.player.PlayerDisplayLeftFX;
import hu.daq.wp.fx.display.player.PlayerDisplayLeftFiveFX;
import java.util.stream.Collectors;
import javafx.collections.transformation.SortedList;

/**
 *
 * @author DAQ
 */
public class TeamDisplayLeftFiveFX extends TeamDisplayFiveFX{

    public TeamDisplayLeftFiveFX() {
        super();
    }

    public TeamDisplayLeftFiveFX(int team_id) {
        super(team_id);
    }

    public TeamDisplayLeftFiveFX(Team team) {
        super(team);
    }
    
    
    
   @Override
    protected void loadPlayers() {
        this.active_players.clear();
        this.active_players.addAll(ServiceHandler.getInstance().getDbService().getActivePlayersOfTeam(this.getTeamId())
                .stream().map(PlayerDisplayLeftFiveFX::new)
                .collect(Collectors.toList()));
        SortedList<PlayerDisplayFX> p = new SortedList<PlayerDisplayFX>(this.active_players); 
        p.setComparator((l, r) -> l.compareTo(r));
    }    
}
