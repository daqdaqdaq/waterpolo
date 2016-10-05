/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.thriftconnector.server;

import hu.daq.thriftconnector.client.ThriftClient;
import hu.daq.thriftconnector.connector.ThriftConnector;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author DAQ
 * @param <T>
 */
public class ThriftServer <T extends ThriftClient>{

    private TProcessor processor;
    private TServer server;
    private Integer listeningport;
    private ThriftConnector container;

    public ThriftServer(TProcessor p,Integer port, ThriftConnector container) {
        this.processor = p;
        this.listeningport = port;
        this.container = container;
    }

    public ThriftServer(TProcessor p,Integer port) {
        this(p,port,null);
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

    private static TServer makeServer(TProcessor processor, Integer port) throws TTransportException {

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
