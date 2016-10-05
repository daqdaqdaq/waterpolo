/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.thriftconnector.server;

import hu.daq.thriftconnector.thrift.WPDisplay;
import hu.daq.wp.fx.display.control.ControllerScreen;

/**
 *
 * @author DAQ
 */
public class WPDisplayServer extends ThriftServer{

    public WPDisplayServer(ControllerScreen cs, Integer port){
        
        super(new WPDisplay.Processor(new WPDisplayHandler(cs)),port) ;
    
    }
}
