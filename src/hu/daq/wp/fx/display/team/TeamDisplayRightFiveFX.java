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
import hu.daq.wp.fx.display.player.PlayerDisplayRightFX;
import hu.daq.wp.fx.display.player.PlayerDisplayRightFiveFX;
import java.util.stream.Collectors;
import javafx.collections.transformation.SortedList;

/**
 *
 * @author DAQ
 */
public class TeamDisplayRightFiveFX extends TeamDisplayFiveFX{

    public TeamDisplayRightFiveFX() {
        super();
    }

    public TeamDisplayRightFiveFX(int team_id) {
        super(team_id);
    }

    public TeamDisplayRightFiveFX(Team team) {
        super(team);
    }
    
    
    
   @Override
    protected void loadPlayers() {
        this.active_players.clear();
        this.active_players.addAll(ServiceHandler.getInstance().getDbService().getActivePlayersOfTeam(this.getTeamId())
                .stream().map(PlayerDisplayRightFiveFX::new)
                .collect(Collectors.toList()));
        SortedList<PlayerDisplayFX> p = new SortedList<PlayerDisplayFX>(this.active_players); 
        p.setComparator((l, r) -> l.compareTo(r));
    }    
}
