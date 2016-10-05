/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.penalties;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 *
 * @author DAQ
 */
public class CircleFactory extends ShapeFactory{

    @Override
    public Shape getShape(double size) {
        return new Circle(size/2);
    }
    
}
