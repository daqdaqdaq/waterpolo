/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp;

import client.Postgres;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;

/**
 *
 * @author DAQ
 */
public class Coaches {
    Postgres db;
    
    public Coaches(Postgres db) {
        this.db = db;
    }
    
    public ArrayList<Coach> load() {
        ArrayList<Coach> relist = new ArrayList<Coach>();
        String sendstr = "select * from coach order by name";
        try {
            relist.addAll(db.query(sendstr).stream().map(E -> {
                Coach t = new Coach(db);
                t.load(E);
                return t;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
          System.out.println("baj"+e.toString());
        }
        return relist;
    }    
    
}
