/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.commands;

import hu.daq.wp.fx.display.control.Command;
import hu.daq.wp.fx.display.control.ControlledScreen;
import hu.daq.wp.fx.display.control.ErrorWrapper;
import hu.daq.wp.fx.display.control.ResultWrapper;
import hu.daq.wp.fx.display.screens.ScoreBoardScreen;
import javafx.application.Platform;

/**
 *
 * @author DAQ
 */
public class RemovePenalty implements Command {

    Integer playerid;

    public RemovePenalty(Integer playerid) {
        this.playerid = playerid;

    }

    @Override
    public ResultWrapper execute(ControlledScreen cs) {
        ScoreBoardScreen sbc = (ScoreBoardScreen) cs;
        try {
            Platform.runLater(() -> {
                sbc.removePenalty(this.playerid);
            });
        } catch (Exception e) {
            ErrorWrapper err = new ErrorWrapper();
            err.putError(e);
            return err;
        }
        return new ResultWrapper();
    }

}
