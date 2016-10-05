/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp;

import client.Notifiable;
import client.Postgres;
import java.util.HashMap;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author DAQ
 */
public abstract class Entity implements Notifiable{
    protected Postgres db;
    //facilities to follow the state of the object to decide when is it necesarry to save state
    protected SimpleBooleanProperty changed = new SimpleBooleanProperty();
    protected InvalidationListener changelistener; 
    
    public Entity(Postgres db){
        this.db = db;
        this.changelistener = (Observable o) -> {
            //System.out.println(o.toString() + "changed");
            changed.set(true);
        };

    }
    
    public abstract boolean load(Integer pk);
    public abstract boolean load(HashMap<String, String> data);
    public abstract boolean save();
    public abstract void subscribe();
    public abstract void unsubscribe();
    @Override
    public abstract void finalize() throws Throwable;
    
    
    public Postgres getDb(){
        return this.db;
    }
            
    public boolean isChanged(){
        return this.changed.getValue();
    }

    public SimpleBooleanProperty getChanged() {
        return changed;
    }

    
    
    
}
