/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.penalties;

import javafx.scene.shape.Shape;

/**
 *
 * @author DAQ
 */
public abstract class ShapeFactory {
 
    public abstract Shape getShape(double size);
}
