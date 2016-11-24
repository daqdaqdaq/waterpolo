/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.infopopup;

import hu.daq.servicehandler.ServiceHandler;
import hu.daq.timeengine.TimeEngine;
import hu.daq.watch.CountdownWatch;
import hu.daq.watch.TimeoutListener;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author DAQ
 */
public abstract class PopupWindow extends Stage implements TimeoutListener {

    CountdownWatch cw;
    PopupCloseListener pcl;
    
    public PopupWindow() {
        if (Platform.isSupported(ConditionalFeature.TRANSPARENT_WINDOW)){
            this.initStyle(StageStyle.TRANSPARENT);
        }
        this.setAlwaysOnTop(true);
        this.setOnCloseRequest((ev)->this.cleanupOnClose());
    }

    public PopupWindow(int duration) {
        this();
        this.setTimer(duration);
    }

    public void setTimer(int duration) {
        if (this.cw == null) {
            TimeEngine te = new TimeEngine();
            te.init();
            this.cw = new CountdownWatch(te, 0, 0, duration);
        } else {
            this.cw.setTimeToCount(0, 0, duration);
        }
        this.cw.addTimeoutListener(this);
    }

    public void showIt() {
        this.show();
        if (this.cw != null) cw.start();
    }

    @Override
    public void timeout() {
        this.close();
    }
    
    private void cleanupOnClose(){
        System.out.println("Popup closing...");
        if (this.pcl!=null) this.pcl.cleanupAfterPopup();
    }
    
    public void setCloseListener(PopupCloseListener pcl){
        this.pcl = pcl;
    }
}
