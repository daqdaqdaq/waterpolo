/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.balltime;

import hu.daq.servicehandler.ServiceHandler;
import hu.daq.thriftconnector.client.WPController;
import javafx.scene.control.TextField;
import org.controlsfx.control.PopOver;

/**
 *
 * @author DAQ
 */
public class TimeSetPopOver extends PopOver {

    TextField timefield;
    BallTime balltime;

    public TimeSetPopOver(BallTime balltime) {
        this.balltime = balltime;
        this.setDetachable(false);
        this.setDetached(false);
        this.setArrowLocation(ArrowLocation.TOP_CENTER);
        this.timefield = new TextField();
        this.setContentNode(this.timefield);

        this.timefield.setOnAction((ev) -> {
            this.setTime();
        });

    }

    private void setTime() {
        if (this.timefield.getText() != null && this.timefield.getText().length() > 0) {
            System.out.println("Time set to: "+this.timefield.getText());
            Double d = Double.parseDouble(this.timefield.getText());
            System.out.println("Time set to: "+this.timefield.getText()+":"+d);
            this.balltime.set((int) Math.round(d*1000));
            ((WPController) ServiceHandler.getInstance().getThriftConnector().getClient()).setBallTime((int) Math.round(d*1000));
        }
        this.hide();

    }

}
