/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.UDPSender;

import hu.daq.watch.BaseWatch;
import hu.daq.watch.Time;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author DAQ
 */
public class TimeSender extends UDPSenderConnector {

    SimpleStringProperty tosend;
    BaseWatch watch;
    Time ot;
    private static final int MILISEC = 1000000;

    public TimeSender(BaseWatch watch, SenderThread st) {
        this.tosend = new SimpleStringProperty();
        this.watch = watch;
        this.ot = this.watch.getObservableTime();
        bindToSender(st, this.tosend);
        //Every change in watch state initiate processing and sending the time data
        this.watch.getTimeEngineRunning().addListener((ObservableValue<? extends Boolean> ob, Boolean ov, Boolean nv) -> {
            this.processTime();
        });
        this.ot.tsec.addListener((ObservableValue<? extends Number> ob, Number ov, Number nv) -> {
            this.processTime();
        });

    }

    private void processTime() {
        //System.out.println("Processing time");
        //calculate the secs
        
        Integer secs = this.ot.sec.getValue();

        //convert to string and if it's under 10 concat the tenth secs 
        String s = secs.toString() + (secs < 10 ? "." + this.ot.tsec.getValue().toString() : "");
        //first caracter indicates the state of the time 
        if (s.equals("0.0")){
            this.tosend.set("N"+s);
            this.tosend.set("N"+s);
            this.tosend.set("N"+s);
        } else{
            this.tosend.set("N"+s);
        }
        //this.tosend.set((this.watch.getTimeEngineRunning().get()&&s!="0.0" ? "N" : "I") + s);
    }

}
