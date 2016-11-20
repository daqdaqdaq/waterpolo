/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.thriftconnector.talkback;

import hu.daq.thriftconnector.client.ReconnectingThriftClient;
import hu.daq.thriftconnector.client.ThriftClient;
import hu.daq.thriftconnector.talkback.WPTalkBack.Client;
import hu.daq.thriftconnector.thrift.FailedOperation;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

/**
 *
 * @author DAQ
 */
public class WPTalkBackClient extends ThriftClient{
    private WPTalkBack.Iface client;

    public WPTalkBackClient(String url, Integer remoteport, Integer listeningport) {
        //super(url.substring(4), remoteport, listeningport);
        super(url, remoteport, listeningport);        
        System.out.println("Talkback client has been made"+url+":"+remoteport);        
    }
    
    
    @Override
    public String connect(String token) throws FailedOperation {
        System.out.println("Trying to connect talkback");
        try {
            this.transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            client = ReconnectingThriftClient.wrap(new WPTalkBack.Client(protocol), new ReconnectingThriftClient.Options(5,50));
            //this.client = new WPTalkBack.Client(protocol);
            System.out.println("Connection to server has established");
        } catch (TException x) {
            throw new FailedOperation("Connection to server has failed");

        }
        return "";
        
    }
    
    public void pauseReceived(TimeSync tsync) {

        try {
            client.paused(token, tsync);
        } catch (TException ex) {
            Logger.getLogger(WPTalkBackClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void syncTime(TimeSync tsync) {

        try {
            client.synctime(token, tsync);
        } catch (TException ex) {
            Logger.getLogger(WPTalkBackClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void sendLegTimeout() {

        try {
            client.legtimeout(token);
        } catch (TException ex) {
            Logger.getLogger(WPTalkBackClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
    public void nextPhase(){
        try {
            client.nextphase(token);
        } catch (TException ex) {
            Logger.getLogger(WPTalkBackClient.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }

    public void prevPhase(){
        try {
            client.prevphase(token);
        } catch (TException ex) {
            Logger.getLogger(WPTalkBackClient.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }    
    
}
