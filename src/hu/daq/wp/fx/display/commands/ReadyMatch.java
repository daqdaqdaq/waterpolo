/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.commands;

import hu.daq.servicehandler.ServiceHandler;
import hu.daq.wp.fx.display.control.Command;
import hu.daq.wp.fx.display.control.ControlledScreen;
import hu.daq.wp.fx.display.control.ErrorWrapper;
import hu.daq.wp.fx.display.control.ResultWrapper;
import hu.daq.wp.fx.display.screens.ScoreBoardScreen;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import org.json.JSONException;

/**
 *
 * @author DAQ
 */
public class ReadyMatch implements Command {

    Integer leftteamid;
    Integer rightteamid;
    Integer matchprofileid;
    Exception extemp;
    
    public ReadyMatch(Integer leftteamid, Integer rightteamid, Integer matchid) {
        this.leftteamid = leftteamid;
        this.rightteamid = rightteamid;
        this.matchprofileid = matchid;
    }

    @Override
    public ResultWrapper execute(ControlledScreen cs) {
        ScoreBoardScreen sbc = (ScoreBoardScreen) cs;
        
        try {
            Platform.runLater(() -> {
                
                sbc.loadLeftTeam(this.leftteamid);
                sbc.loadRightTeam(this.rightteamid);
                try {
                    sbc.loadMatchProfile(this.matchprofileid);
                } catch (JSONException ex) {
                    this.extemp = ex;
                }
            });
            if (this.extemp != null) throw extemp;
        } catch (Exception e) {
            ErrorWrapper err = new ErrorWrapper();
            err.putError(e);
            return err;
        }
        return new ResultWrapper();
    }

}
