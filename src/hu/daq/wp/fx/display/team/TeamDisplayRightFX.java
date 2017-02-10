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
import hu.daq.wp.fx.display.player.PlayerDisplayLeftFXDummy;
import hu.daq.wp.fx.display.player.PlayerDisplayRightFX;
import hu.daq.wp.fx.display.player.PlayerDisplayRightFXDummy;
import java.util.HashMap;
import java.util.stream.Collectors;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;

/**
 *
 * @author DAQ
 */
public class TeamDisplayRightFX extends TeamDisplayFX{

    public TeamDisplayRightFX() {
        super();
        this.playerlist.setMinWidth(200);         
        this.playerlist.setAlignment(Pos.TOP_RIGHT);        
    }

    public TeamDisplayRightFX(Postgres db, int team_id) {
        super(team_id);
        this.playerlist.setMinWidth(200);         
        this.playerlist.setAlignment(Pos.TOP_RIGHT);          
    }

    public TeamDisplayRightFX(Team team) {
        super(team);
        this.playerlist.setMinWidth(200);         
        this.playerlist.setAlignment(Pos.TOP_RIGHT);          
    }


    @Override
    protected void loadPlayers() {
        this.active_players.clear();        
        HashMap<Integer,PlayerDisplayFX> al = new HashMap<Integer,PlayerDisplayFX>();
        ServiceHandler.getInstance().getDbService().getActivePlayersOfTeam(this.getTeamId())
                .stream().map(PlayerDisplayRightFX::new).forEach(E -> al.put(E.getCapnum(), E));
                
        for (int i=1;i<=14;i++){
            try {
                System.out.println(al.get(i).getPlayerID()+":"+al.get(i));
            } catch (Exception e){
            
            }
            this.active_players.add(al.getOrDefault(i, new PlayerDisplayRightFXDummy(i)));
        }
        
/*        this.active_players.addAll(ServiceHandler.getInstance().getDbService().getActivePlayersOfTeam(this.getTeamId())
                .stream().map(PlayerDisplayLeftFX::new)
                .collect(Collectors.toList()));
        */
        SortedList<PlayerDisplayFX> p = new SortedList<PlayerDisplayFX>(this.active_players); 
        p.setComparator((l, r) -> l.compareTo(r));
        this.playerlist.getChildren().setAll(p);

    }    

    
}
