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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;

/**
 *
 * @author DAQ
 */
public abstract class Entity {

    //facilities to follow the state of the object to decide when is it necesarry to save state
    public SimpleIntegerProperty id = new SimpleIntegerProperty();
    protected SimpleBooleanProperty deleted = new SimpleBooleanProperty();
    protected SimpleBooleanProperty changed = new SimpleBooleanProperty();
    protected ChangeListener changelistener;

    public Entity() {

        this.deleted.set(false);
        this.changelistener = (o, ov, nv) -> {
            //System.out.println(o.toString() + "changed");
            if (nv != null) {
                if (!nv.equals(ov)) {
                    changed.set(true);
                }
            }
        };

    }

    public abstract String toInternalString();

    public boolean isChanged() {
        return this.changed.getValue();
    }

    public void setToChanged(boolean changed) {
        this.changed.set(changed);
    }

    public SimpleBooleanProperty getChanged() {
        return changed;
    }

    public SimpleBooleanProperty getDeleted() {
        return deleted;
    }

    public SimpleIntegerProperty getId() {
        return id;
    }

    public void setId(SimpleIntegerProperty id) {
        this.id = id;
    }

    public Integer getID() {
        return id.getValue();
    }

}
