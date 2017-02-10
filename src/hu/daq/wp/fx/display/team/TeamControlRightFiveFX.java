/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.team;

import client.Postgres;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.thriftconnector.client.WPController;
import hu.daq.wp.Team;
import hu.daq.wp.fx.display.player.PlayerControlFX;
import hu.daq.wp.fx.display.player.PlayerControlRightFiveFX;
import hu.daq.wp.fx.display.player.PlayerDisplayFX;
import java.util.stream.Collectors;
import javafx.collections.transformation.SortedList;

/**
 *
 * @author DAQ
 */
public class TeamControlRightFiveFX extends TeamControlFX {

    public TeamControlRightFiveFX() {
        super();
    }

    public TeamControlRightFiveFX(int team_id) {
        super(team_id);
    }

    public TeamControlRightFiveFX(Team team) {
        super(team);
    }

    /**
     *
     */
    @Override
    protected void loadPlayers() {
        this.active_players.clear();
        this.active_players.addAll(ServiceHandler.getInstance().getDbService().getActivePlayersOfTeam(this.getTeamID())
                .stream().map(PlayerControlRightFiveFX::new)
                .collect(Collectors.toList()));

        SortedList<PlayerDisplayFX> p = new SortedList<PlayerDisplayFX>(this.active_players);
        p.setComparator((l, r) -> l.compareTo(r));
        this.loaded.set(true);

    }

    public void addPlayer(Integer playerid) {
        try {
            if (!this.playerlist.getChildren().remove(this.getPlayer(playerid))) {
                this.playerlist.getChildren().add(this.getPlayer(playerid));
            }            
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).addFivemPlayer(playerid);
        } catch (Exception ex) {
            System.out.println("Add player failed");
            //Fail silently
        }
    }

    @Override
    public void handleObject(Object source, String object) {
        System.out.println("Handling...." + object + ":" + source);
        /*
         Drag between windows a bit awkward and sets the dragsource to null
         In that case try to get the drag source from the ServiceHandler. Don't forget to store it in there!!!        
         */
        //if (source == null) {
        //    source = ServiceHandler.getInstance().getDragSource();
        //}
        this.addPlayer(Integer.parseInt(object));
    }

    public void clearAllPlayer(){
        this.playerlist.getChildren().clear();
    
    }    
}
