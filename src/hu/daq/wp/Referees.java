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
public class Referees {
    Postgres db;
    
    public Referees(Postgres db) {
        this.db = db;
    }
    
    public ArrayList<Referee> load() {
        ArrayList<Referee> relist = new ArrayList<Referee>();
        String sendstr = "select * from referee order by name";
        try {
            relist.addAll(db.query(sendstr).stream().map(E -> {
                Referee t = new Referee(db);
                t.load(E);
                return t;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
          System.out.println("baj"+e.toString());
        }
        return relist;
    }    
    
}
