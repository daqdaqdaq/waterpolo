/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.StackPane;

/**
 *
 * @author DAQ
 */
public abstract class EntityFX extends StackPane implements Instructable{
    
    SimpleBooleanProperty inuse = new SimpleBooleanProperty(false);
    
    public abstract String getID();
    public abstract Boolean isDragable();
    public abstract String getName();
    public abstract Integer getTeamID();
    public abstract void editOn();
    public abstract String getType();
    
    public void setInuse(Boolean in){
        this.inuse.set(in);
    }
    
    @Override
    public void execute() {
        this.editOn();
    }
    
}
