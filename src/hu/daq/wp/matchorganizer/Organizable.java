/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.matchorganizer;

import hu.daq.watch.TimeoutListener;
import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author DAQ
 */
public interface Organizable {
    public void setupPhase(MatchPhase mp);
    public void setTimeoutListener(TimeoutListener tl);

}
