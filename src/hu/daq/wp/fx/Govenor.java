/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

/**
 *
 * @author DAQ
 */
public interface Govenor {
    public void bindGoverned(Governed governed);
    public void unbindGoverned(Governed governed);
    public void fireGovernedEvent();
}
