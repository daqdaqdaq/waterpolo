/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.thriftconnector.server;

import hu.daq.servicehandler.ServiceHandler;
import hu.daq.thriftconnector.talkback.StatusReport;
import hu.daq.thriftconnector.talkback.WPTalkBackClient;
import hu.daq.thriftconnector.thrift.ClientData;
import hu.daq.thriftconnector.thrift.FailedOperation;
import hu.daq.thriftconnector.thrift.TimeReport;
import hu.daq.thriftconnector.thrift.WPDisplay;
import hu.daq.wp.fx.display.commands.AddFivemPlayer;
import hu.daq.wp.fx.display.commands.BallTimeLeft;
import hu.daq.wp.fx.display.commands.BallTimeReset;
import hu.daq.wp.fx.display.commands.BallTimeRight;
import hu.daq.wp.fx.display.commands.BallTimeSet;
import hu.daq.wp.fx.display.commands.BallTimeSwitch;
import hu.daq.wp.fx.display.commands.ClearScoreBoard;
import hu.daq.wp.fx.display.commands.CloseFivemWindow;
import hu.daq.wp.fx.display.commands.FivemGoal;
import hu.daq.wp.fx.display.commands.Goal;
import hu.daq.wp.fx.display.commands.HidePlayerInfo;
import hu.daq.wp.fx.display.commands.Honk;
import hu.daq.wp.fx.display.commands.LegTimeSet;
import hu.daq.wp.fx.display.commands.LoadTeams;
import hu.daq.wp.fx.display.commands.NextPhase;
import hu.daq.wp.fx.display.commands.PauseMatch;
import hu.daq.wp.fx.display.commands.Penalty;
import hu.daq.wp.fx.display.commands.PrevPhase;
import hu.daq.wp.fx.display.commands.ReadyMatch;
import hu.daq.wp.fx.display.commands.RemoveFivemGoal;
import hu.daq.wp.fx.display.commands.RemoveGoal;
import hu.daq.wp.fx.display.commands.RemovePenalty;
import hu.daq.wp.fx.display.commands.RequestTimeout;
import hu.daq.wp.fx.display.commands.ShowPlayerInfo;
import hu.daq.wp.fx.display.commands.StartMatch;
import hu.daq.wp.fx.display.control.Command;
import hu.daq.wp.fx.display.control.ControllerScreen;
import hu.daq.wp.fx.display.control.ErrorWrapper;
import hu.daq.wp.fx.display.control.ResultWrapper;
import org.apache.thrift.TException;

/**
 *
 * @author DAQ
 */
public class WPDisplayHandler implements WPDisplay.Iface {

    ControllerScreen cs;
    ThriftServer server;

    public WPDisplayHandler(ControllerScreen cs) {
        this.cs = cs;

    }

    @Override
    public String login(String token, ClientData rclient) throws FailedOperation, TException {

        System.out.println("Login request received...");
        String res = cs.login(token);
        if (res != "") {
            WPTalkBackClient wptb = new WPTalkBackClient(rclient.ip, rclient.port, null);
            wptb.connect(token);
            ServiceHandler.getInstance().getThriftConnector().registerClient(wptb);
            return res;
        } else {
            throw new FailedOperation("Unauthorized!");
        }

    }
    @Override
    public void readymatch(String token, int leftteamid, int rightteamid, int matchid) throws FailedOperation, TException {
        //System.out.println("Login request received...");

        if (cs.checkToken(token)) {
            cs.switchScreen("scoreboard");
            Command comm = new ReadyMatch(leftteamid, rightteamid,matchid);
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }
    }


    @Override
    public void startmatch(String token) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new StartMatch();
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }
    }

    @Override
    public void loadteams(String token, int leftteamid, int rightteamid) throws FailedOperation, TException {
        //System.out.println("Login request received...");

        if (cs.checkToken(token)) {
            cs.switchScreen("scoreboard");
            Command comm = new LoadTeams(leftteamid, rightteamid);
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }

    }

    @Override
    public void foul(String token, int playerid) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new Penalty(playerid);
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }
    }

    @Override
    public void goal(String token, int playerid) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new Goal(playerid);
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }
    }

    @Override
    public TimeReport pausematch(String token) throws FailedOperation, TException {
        System.out.println("Pause received");
        if (cs.checkToken(token)) {
            Command comm = new PauseMatch();
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }
        return new TimeReport();
    }

    @Override
    public void removefoul(String token, int playerid) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new RemovePenalty(playerid);
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }
    }

    @Override
    public void removegoal(String token, int playerid) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new RemoveGoal(playerid);
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }
    }

    @Override
    public void balltimeset(String token, int secs) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new BallTimeSet(secs);
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }

    }

    @Override
    public void balltimereset(String token) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new BallTimeReset();
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }
    }

    @Override
    public void balltimeleft(String token) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new BallTimeLeft();
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }
    }

    @Override
    public void balltimeright(String token) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new BallTimeRight();
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }
    }

    @Override
    public void balltimeswitch(String token) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new BallTimeSwitch();
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }
    }

    @Override
    public void switchscreen(String token, String screen) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            cs.switchScreen(screen);
        } else {

            throw new FailedOperation("Unauthorized!");
        }
    }

    //Nudging the db provoking all listeners to refresh
    @Override
    public void dbrefresh(String token) throws FailedOperation, TException {
        cs.getDb().nudgeDB();
    }

    @Override
    public void showplayerinfo(String token, int playerid) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new ShowPlayerInfo(playerid);
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }        
        
    }

    @Override
    public void hideplayerinfo(String token) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new HidePlayerInfo();
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }        
    }

    @Override
    public void requesttimeout(String token, int teamid) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new RequestTimeout(teamid);
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }         
        
    }

    @Override
    public void fivemgoal(String token, int playerid) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new FivemGoal(playerid);
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }       
    }

    @Override
    public void removefivemgoal(String token, int playerid) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new RemoveFivemGoal(playerid);
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }        
    }

    @Override
    public void addfivemplayer(String token, int playerid) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new AddFivemPlayer(playerid);
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }        
    }       

    @Override
    public void closefivemwindow(String token) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new CloseFivemWindow();
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }        
    }

    @Override
    public String logout(String token) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            ServiceHandler.getInstance().getThriftConnector().getClient().disconnect();
            return "";
        } else {

            throw new FailedOperation("Unauthorized!");
        }
    }

    @Override
    public void clearscoreboard(String token) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new ClearScoreBoard();
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }
        } else {

            throw new FailedOperation("Unauthorized!");
        }       
    }

    @Override
    public void honk(String token) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new Honk();
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }            
        } else {

            throw new FailedOperation("Unauthorized!");
        }        
    }

    @Override
    public StatusReport statusreport(String token) throws FailedOperation, TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void nextphase(String token) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new NextPhase();
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }            
        } else {

            throw new FailedOperation("Unauthorized!");
        }
    }

    @Override
    public void prevphase(String token) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new PrevPhase();
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }            
        } else {

            throw new FailedOperation("Unauthorized!");
        }        
    }

    @Override
    public void setlegtime(String token, int milisec) throws FailedOperation, TException {
        if (cs.checkToken(token)) {
            Command comm = new LegTimeSet(milisec);
            ResultWrapper r = cs.sendCommand(comm);
            if (r.isError()) {
                throw new FailedOperation(((ErrorWrapper) r).getError().toString());
            }            
        } else {

            throw new FailedOperation("Unauthorized!");
        }
    }

    @Override
    public void setballtime(String token, int milisec) throws FailedOperation, TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}
