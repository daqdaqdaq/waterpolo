/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.control;

/**
 *
 * @author DAQ
 */
public interface Command {

    public ResultWrapper execute(ControlledScreen cs);
}
