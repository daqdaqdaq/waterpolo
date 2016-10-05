/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.balltime;

import javafx.scene.paint.Color;

/**
 *
 * @author DAQ
 */
public class PointerTriangleLeft extends PointerTriangle {

    public PointerTriangleLeft(Color oncolor, Color offcolor) {
        super(oncolor, offcolor);
        this.getPoints().addAll(new Double[]{
            10.0, 0.0,
            10.0, 50.0,
            0.0, 25.0
        });
    }

}
