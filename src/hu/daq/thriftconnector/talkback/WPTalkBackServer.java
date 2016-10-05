/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.thriftconnector.talkback;

import hu.daq.thriftconnector.server.ThriftServer;
import hu.daq.wp.fx.screens.MatchScreen;

/**
 *
 * @author DAQ
 */
public class WPTalkBackServer extends ThriftServer{

    public WPTalkBackServer(MatchScreen ms,Integer port) {
   
        super(new WPTalkBack.Processor(new WPTalkBackHandler(ms)),port) ;
    
    }
}