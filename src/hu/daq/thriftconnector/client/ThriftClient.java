/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.thriftconnector.client;

import hu.daq.thriftconnector.thrift.FailedOperation;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 *
 * @author DAQ
 */
public abstract class ThriftClient {

    protected TTransport transport;
    protected final String url;
    protected final Integer remoteport;
    protected final Integer listeningport;
    protected String token;
    TProtocol protocol;

    public ThriftClient(String url, Integer remoteport, Integer listeningport) {
        this.remoteport = remoteport;
        this.url = url;
        this.listeningport = listeningport;
        this.token = "";
        this.transport = new TSocket(this.url, this.remoteport);
        this.protocol = new TBinaryProtocol(transport);
    }

    public abstract String connect(String token) throws FailedOperation;

    public void disconnect() {
        this.token = "";
        if (this.transport != null) {
            this.transport.close();
        }
    }

    protected String getLocalIP() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    @Override
    public void finalize() throws Throwable {
        this.disconnect();
        super.finalize();
    }
}
