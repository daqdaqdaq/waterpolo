/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.leginfo;

import hu.daq.timeengine.TimeEngine;
import hu.daq.watch.BaseWatch;
import hu.daq.watch.CountdownWatch;
import hu.daq.watch.TimeoutListener;
import hu.daq.watch.fx.TimeDisplay;
import hu.daq.watch.utility.WatchFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author DAQ
 */
public class LegInfo extends VBox {

    private final BaseWatch legtime;
    private Label legname;

    public LegInfo(TimeEngine te) {
        this.legtime = new CountdownWatch(te);
        this.legname = new Label();
        this.build();
    }

    private void build() {
        //this.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        this.legname.setFont(new Font(30));
        //We set it to a fake amount and let the WatchFactory generate a correct display format
        this.legtime.setTimeToCount(0, 1, 1);
        TimeDisplay td = WatchFactory.getSimpleWatchDisplay(this.legtime);
        //HBox td = new HBox();
        //td.getChildren().addAll(new Label("0"),new Label(":"),new Label("00"));
        td.setFontSize(30);
        //td.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        this.setAlignment(Pos.CENTER);
        //this.add(legname, 0, 0);
        //this.add(td,0,1);
        this.getChildren().addAll(this.legname, td);
        VBox.setVgrow(td, Priority.NEVER);
        //GridPane.setHalignment(legname, HPos.CENTER);
        //GridPane.setHalignment(td, HPos.CENTER);        
        //Set it back to 0. 
        this.legtime.setTimeToCount(0, 0, 0);

        //Immediatelly start the clock. The central time engine will control it
        this.legtime.start();
    }

    public void setTimeoutListener(TimeoutListener tl) {
        this.legtime.addTimeoutListener(tl);
    }

    public TimeoutListener getTimeoutListener() {
        return this.legtime.getTimeoutListener();
    }    
    
    public void setTimeToCount(int hours, int mins, int secs) {
        this.legtime.setTimeToCount(hours, mins, secs);
    }

    public void setTimeToCount(int milisecs) {
        this.legtime.setTimeToCount((double) milisecs);
    }
    
    public int getTimeToCount() {
        return this.legtime.getTimeToCount();
    }
    
    public void resetTime() {
        this.legtime.reset();
    }

    public void setLegName(String legname) {
        this.legname.setText(legname);
    }

    public int getTime() {
        return (int) this.legtime.getComputedmilis().get();
    }

    public void setTime(int milisecs){
        this.legtime.set(milisecs);
    }
    
    public int getRemainingTime() {
        return this.legtime.getRemainingTime();
    }

}
