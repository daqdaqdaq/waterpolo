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
public class MatchProfiles implements Notifiable {

    Postgres db;
    ObservableList<MatchProfile> profiles;

    public MatchProfiles(Postgres db) {
        this.db = db;
//        this.teams = FXCollections.observableList(new ArrayList<Team>());
        this.profiles = FXCollections.observableArrayList(MatchProfile.extractor());

    }

    public ObservableList<MatchProfile> load() {
        String sendstr = "select * from matchprofile order by profilename";
        try {
            this.profiles.setAll(db.query(sendstr).stream().map(E -> {
                System.out.println(E);
                MatchProfile t = new MatchProfile(db);
                t.load(E);
                return t;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
          System.out.println("baj"+e.toString());
        }
        return this.profiles;
    }

    public ObservableList<MatchProfile> getProfiles() {
        return this.profiles;
    }

    public boolean save() {
        this.profiles.stream().forEach((E) -> {E.save();});
        return true;    
    }    

    @Override
    public void onNotify() {
        this.load();
    }
    
    public void subscribe() {
        this.db.subscribe("matchprofiles", this);
    }

    public void unsubscribe() {
        this.db.unsubscribe("matchprofiles", this);
    }

    @Override
    public void finalize() throws Throwable {
        this.unsubscribe();
        
    }
    
    //Returns the id of the object in a notification mechanism compatible form
    //Teams object has no real id return 0 instead
    @Override
    public Integer getID() {
        return new Integer(0);
    }

}
