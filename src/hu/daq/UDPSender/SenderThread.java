/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.UDPSender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Simple UDP packet sending client
 *
 * @author DAQ
 */
public class SenderThread extends Thread {

    private final InetAddress targetip;
    private final int port;
    private boolean run;
    private byte[] buf;
    private DatagramSocket sendersocket;
    private List<String> stringtosend;
    //private AtomicReference<String> stringtosend;

    public SenderThread(String targetip, int port, int buffersize) throws UnknownHostException, SocketException {
        //  this.stringtosend = new AtomicReference<String>("");
        this.stringtosend = Collections.synchronizedList(new ArrayList<String>());
        this.targetip = InetAddress.getByName(targetip);
        this.port = port;
        this.buf = new byte[buffersize];
        this.sendersocket = new DatagramSocket();
        //this.stringtosend.set("");
        this.run = true;
    }

    public SenderThread(String targetip, int port) throws UnknownHostException, SocketException {
        this(targetip, port, 32);
    }

    public void sendString(String stringtosend) {
        //System.out.println("Prepare string to send"+stringtosend);
        this.stringtosend.add(stringtosend);
    }

    @Override
    public void run() {
        while (this.run) {
            //System.out.println("Running..."+this.stringtosend);
            if (this.stringtosend.size() > 0) {
                //System.out.println("Sending..." + this.stringtosend.get(0));
                try {
                    this.buf = this.stringtosend.get(0).getBytes();
                    if (this.stringtosend.get(0).startsWith("D")){
                        System.out.println("Duda: "+this.stringtosend);
                    }
                    this.sendersocket.send(new DatagramPacket(buf, buf.length, this.targetip, this.port));
                    this.stringtosend.remove(0);
                } catch (IOException ex) {
                    System.out.println("Besult a mutatvany");
                    Logger.getLogger(SenderThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                sleep(50);
            } catch (InterruptedException e) {
            }
        }
    }

    public void stopSender() {
        this.run = false;
        try {
            this.sendersocket.close();
        } catch (Exception ex) {
            //Fail silently
        }
    }

    @Override
    public void finalize() {
        this.sendString("S");
        this.stopSender();
    }
}
