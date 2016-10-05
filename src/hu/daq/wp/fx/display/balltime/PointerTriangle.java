/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.balltime;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 *
 * @author DAQ
 */
public abstract class PointerTriangle extends Polygon{
    
    SimpleBooleanProperty on;
    Color oncolor;
    Color offcolor;

    public PointerTriangle(Color oncolor, Color offcolor) {
        on = new SimpleBooleanProperty(Boolean.TRUE);
        this.oncolor = oncolor;
        this.offcolor = offcolor;
        this.on.addListener((ObservableValue<? extends Boolean> observable,Boolean ov,Boolean nv)->{
            this.fillProperty().set(nv?this.oncolor:this.offcolor);
        });
    }

    public void switchState(){
        this.on.set(!this.on.get());
    }
    
    public void off(){
        this.on.set(false);
    }

    public void on(){
        this.on.set(true);
    }
    
    public Boolean isOn(){
        return this.on.getValue();
    }
}
