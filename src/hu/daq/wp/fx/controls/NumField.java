/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.controls;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.NumberStringConverter;

/**
 *
 * @author DAQ
 */
public class NumField extends TextField{

    public NumField() {
        this.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
    }

    public NumField(String text) {
        super(text);
        
        this.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
    }
    
}
