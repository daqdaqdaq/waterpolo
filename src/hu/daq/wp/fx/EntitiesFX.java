/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

import client.Postgres;
import hu.daq.servicehandler.ServiceHandler;

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




    public EntitiesFX() {
     
    }
   

    public List<EntityFX> loadAll(){
        ArrayList<EntityFX> re = new ArrayList<EntityFX>();
        re.addAll(this.loadPlayers());
        re.addAll(this.loadCoaches());
        re.addAll(this.loadReferees());
        return re;
    }
    
    public List<EntityFX> loadPlayers(){
        
        return ServiceHandler.getInstance().getDbService().getPlayers().stream().map(PlayerFX::new).collect(Collectors.toList());

        
    }

    public List<EntityFX> loadReferees(){

        return ServiceHandler.getInstance().getDbService().getReferees().stream().map(RefereeFX::new).collect(Collectors.toList());
    }    

    public List<EntityFX> loadCoaches(){

        return ServiceHandler.getInstance().getDbService().getCoaches().stream().map(CoachFX::new).collect(Collectors.toList());
    }

}
