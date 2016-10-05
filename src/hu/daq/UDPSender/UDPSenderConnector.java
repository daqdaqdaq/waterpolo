/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.UDPSender;

import javafx.beans.value.ObservableValue;

/**
 *
 * @author DAQ
 */
public class UDPSenderConnector{

    public static <T extends ObservableValue> void bindToSender(SenderThread sender, T ob) {
        ob.addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
            sender.sendString(convertValue(newValue));
            //System.out.println(newValue);
        });
        
    }
    
    public static String convertValue(Object invalue){
        return invalue.toString();
    }
    
}
