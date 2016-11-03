/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.thriftconnector.talkback;

import hu.daq.servicehandler.ServiceHandler;
import hu.daq.wp.fx.screens.MatchScreen;
import org.apache.thrift.TException;

/**
 *
 * @author DAQ
 */
public class WPTalkBackHandler implements WPTalkBack.Iface {

    MatchScreen ms;

    public WPTalkBackHandler(MatchScreen ms) {
        this.ms = ms;
    }

    @Override
    public void legtimeout(String token) throws TException {
        ServiceHandler.getInstance().getOrganizer().nextPhase();
    }

    @Override
    public void balltimeout(String token) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void paused(String token, TimeSync tsync) throws TException {
       // Platform.runLater(() -> {
            ms.pauseReceived(tsync);
       // });

    }

    @Override
    public void nextphase(String token) throws TException {
        ms.nextPhase();
    }

    @Override
    public void prevphase(String token) throws TException {
        ms.prevPhase();
    }

}
