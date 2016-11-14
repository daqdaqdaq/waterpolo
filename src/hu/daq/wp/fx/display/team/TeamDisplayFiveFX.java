/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.team;

import client.Postgres;
import hu.daq.wp.Team;

/**
 *
 * @author DAQ
 */
public abstract class TeamDisplayFiveFX extends TeamDisplayFX {

    public TeamDisplayFiveFX(Postgres db) {
        super(db);
        

    }

    public TeamDisplayFiveFX(Postgres db, int team_id) {
        super(db, team_id);

    }

    public TeamDisplayFiveFX(Team team) {
        super(team);

    }

    public void addPlayer(Integer playerid) {
        try {
            this.playerlist.getChildren().add(this.getPlayer(playerid));
        } catch (Exception ex) {
            //Fail silently
        }
    }

    public void removePlayer(Integer playerid) {
        try {
            this.playerlist.getChildren().remove(this.getPlayer(playerid));
        } catch (Exception ex) {
            //Fail silently
        }
    }

}
