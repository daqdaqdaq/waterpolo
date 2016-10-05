/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.thriftconnector.server;

import hu.daq.thriftconnector.thrift.WPDisplay;
import hu.daq.wp.fx.display.control.ControllerScreen;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author DAQ
 */
public class WPDisplayServerT {

    private WPDisplay.Processor processor;
    private TServer server;
    private Integer listeningport;

    public WPDisplayServerT(ControllerScreen cs, Integer port) {
        this.processor = new WPDisplay.Processor(new WPDisplayHandler(cs));
        this.listeningport = port;
    }

    public void startServer() throws TTransportException {
        this.server = makeServer(processor, listeningport);
        Runnable se = () -> {
            server.serve();
        };

        new Thread(se).start();
    }

    public void stopServer() {
        this.server.stop();
    }

    private static TServer makeServer(WPDisplay.Processor processor, Integer port) throws TTransportException {

        try {
            //TNonblockingServerTransport servertransport = new TNonblockingServerSocket(port);
            TServerTransport servertransport = new TServerSocket(port);
            //TServer server = new TSimpleServer(new Args( servertransport).processor(processor));
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(servertransport).processor(processor));
            return server;
        } catch (TTransportException ex) {
            System.out.println("Katasztrof has arrived...");
            throw ex;
        }

    }

    public void finalize() {
        this.stopServer();
    }
}
