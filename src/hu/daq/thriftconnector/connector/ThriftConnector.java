/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.thriftconnector.connector;

import hu.daq.thriftconnector.client.ThriftClient;
import hu.daq.thriftconnector.server.ThriftServer;

/**
 *
 * @author DAQ
 * @param <T>
 * @param <T1>
 *
 */
public class ThriftConnector<T extends ThriftServer, T1 extends ThriftClient> {

    private T server;
    private T1 client;

    public ThriftConnector(T server, T1 client) {
        this.server = server;
        this.client = client;
    }

    public ThriftConnector(T server) {
        this(server, null);
    }

    public ThriftConnector() {
        this(null, null);
    }

    public T getServer() {
        return this.server;
    }

    public T1 getClient() {
        return this.client;
    }

    public void registerClient(T1 client) {
        this.client = client;
    }

    public void registerServer(T server) {
        this.server = server;
    }

    public void closeConnections() {
       
        if (this.server != null) {
            this.server.stopServer();
        }

        if (this.client != null) {
            this.client.disconnect();
        }
    }

    @Override
    public void finalize() {
        this.client.disconnect();
        this.server.stopServer();
    }
}
