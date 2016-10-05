/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp;

import client.Notifiable;
import client.Postgres;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author DAQ
 */
public class Teams implements Notifiable {

    Postgres db;
    ObservableList<Team> teams;

    public Teams(Postgres db) {
        this.db = db;
        this.teams = FXCollections.observableList(new ArrayList<Team>());

    }

    public ObservableList<Team> load() {
        String sendstr = "select *,"
                + " (select count(*) from player where team_id=team.team_id and active='t')=14 as playernumok,"
                + " (select count(distinct capnum) from player where team_id=team.team_id and active='t')=14 as capnumok"
                + " from team order by teamname";
        try {
            this.teams.setAll(db.query(sendstr).stream().map(E -> {
                Team t = new Team(db);
                t.load(E);
                return t;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
          System.out.println("baj"+e.toString());
        }
        return this.teams;
    }

    public ObservableList<Team> getTeams() {
        return teams;
    }

    public boolean save() {
        this.teams.stream().forEach((E) -> {E.save();});
        return true;    
    }    

    @Override
    public void onNotify() {
        this.load();
    }
    
    public void subscribe() {
        this.db.subscribe("teams", this);
    }

    public void unsubscribe() {
        this.db.unsubscribe("teams", this);
    }

    @Override
    public void finalize() throws Throwable {
        this.db.unsubscribe("teams", this);
        
    }
    
    //Returns the id of the object in a notification mechanism compatible form
    //Teams object has no real id return 0 instead
    @Override
    public Integer getID() {
        return new Integer(0);
    }

}
