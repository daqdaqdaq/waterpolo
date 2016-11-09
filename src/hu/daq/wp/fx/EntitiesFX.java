/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

import client.Postgres;
import hu.daq.wp.Coaches;
import hu.daq.wp.Players;
import hu.daq.wp.Referees;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;

/**
 *
 * @author DAQ
 */
public class EntitiesFX {

    Postgres db;


    public EntitiesFX(Postgres db) {
        this.db = db;
     
    }
   

    public List<EntityFX> loadAll(){
        ArrayList<EntityFX> re = new ArrayList<EntityFX>();
        re.addAll(this.loadPlayers());
        re.addAll(this.loadCoaches());
        re.addAll(this.loadReferees());
        return re;
    }
    
    public List<EntityFX> loadPlayers(){
        
        Players players = new Players(db);
        return players.load().stream().map(PlayerFX::new).collect(Collectors.toList());

        
    }

    public List<EntityFX> loadReferees(){
        Referees ents = new Referees(db);
        return ents.load().stream().map(RefereeFX::new).collect(Collectors.toList());
    }    

    public List<EntityFX> loadCoaches(){
        Coaches ents = new Coaches(db);
        return ents.load().stream().map(CoachFX::new).collect(Collectors.toList());
    }

}
