/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.thriftconnector.client;

import hu.daq.thriftconnector.client.ReconnectingThriftClient.Options;
import hu.daq.thriftconnector.thrift.ClientData;
import hu.daq.thriftconnector.thrift.FailedOperation;
import hu.daq.thriftconnector.thrift.WPDisplay;
//import hu.daq.thriftconnector.thrift.WPDisplay;
import hu.daq.thriftconnector.thrift.WPDisplay.Client;
//import hu.daq.thriftconnector.thrift.WPDisplay;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

/**
 *
 * @author DAQ
 */
public class WPController extends ThriftClient {

    private WPDisplay.Iface client;

    public WPController(String url, Integer remoteport, Integer listeningport) {
        super(url, remoteport, listeningport);
    }

    @Override
    public String connect(String token) throws FailedOperation {
        if (!this.transport.isOpen()) {
            try {
                this.transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                client = ReconnectingThriftClient.wrap(new Client(protocol), new Options(5,50));
                
                //this.client = new WPDisplay.Client(protocol);
                System.out.println("Connection to server has established");
            } catch (TException x) {
                throw new FailedOperation("Connection to server has failed");

            }

            try {
                this.token = token;
                ClientData cd = new ClientData(this.getLocalIP(), this.listeningport);
                System.out.println("Token:"
                        + token);
                return this.client.login(token, cd);
            } catch (UnknownHostException ex) {
                throw new FailedOperation("Can't find local IP");
            } catch (TException ex) {
                throw new FailedOperation("Login has failed");
            }
        } else {
            return "";
        }
    }

    @Override
    public void disconnect() {
        //try to send the a logout to the server thus a server can disconnect the talkback client
        try {

            this.client.logout(this.token);
        } catch (Exception ex) {

        }
        super.disconnect();
    }

    public void soundHorn() {
        try {
            this.client.honk(token);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addGoal(Integer playerid) {
        try {
            this.client.goal(this.token, playerid);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setGoal(Integer playerid) {
        try {
            this.client.setgoal(this.token, playerid);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void removeGoal(Integer playerid) {
        try {
            this.client.removegoal(this.token, playerid);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    
    public void addFivemGoal(Integer playerid) {
        try {
            this.client.fivemgoal(this.token, playerid);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addFivemPlayer(Integer playerid) {
        try {
            this.client.addfivemplayer(token, playerid);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeFivemGoal(Integer playerid) {
        try {
            this.client.removefivemgoal(this.token, playerid);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void attackerPenalty() {
        try {
            this.client.attackerfoul(token);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void defenderPenalty() {
        try {
            this.client.defenderfoul(token);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    public void doublePenalty() {
        try {
            this.client.doublefoul(token);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setPenalty(Integer playerid, Integer milisec) {
        try {
            this.client.setfoul(token, playerid, milisec);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void addPenalty(Integer playerid) {
        try {
            this.client.foul(this.token, playerid);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removePenalty(Integer playerid) {
        try {
            this.client.removefoul(this.token, playerid);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showPlayerInfo(Integer playerid) {
        try {
            this.client.showplayerinfo(this.token, playerid);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showCoachInfo(Integer coachid) {
        try {
            this.client.showcoachinfo(token, coachid);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showRefereeInfo(Integer refereeid) {
        try {
            this.client.showprefereeinfo(token, refereeid);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void hidePlayerInfo() {
        try {
            this.client.hideplayerinfo(this.token);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startMatch() {
        try {
            this.client.startmatch(this.token);;
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void pauseMatch() {
        try {
            this.client.pausematch(this.token);;
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void switchTeam() {
        try {
            this.client.balltimeswitch(token);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void switchTeamToLeft() {
        try {
            this.client.balltimeleft(token);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void switchTeamToRight() {
        try {
            this.client.balltimeright(token);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void resetBallTime() {
        try {
            this.client.balltimereset(token);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setBallTime(Integer secs) {
        try {
            this.client.balltimeset(token, secs);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setLegTime(Integer secs) {
        try {
            this.client.setlegtime(token, secs);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    /*
     Obsolete! Use readymatch instead!
     */
    public void loadTeam(int leftteamid, int rightteamid) {
        try {
            this.client.loadteams(this.token, leftteamid, rightteamid);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void nextPhase() {
        try {
            this.client.nextphase(this.token);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void prevPhase() {
        try {
            this.client.prevphase(this.token);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void readyMatch(int leftteamid, int rightteamid, int matchid) {
        try {
            this.client.readymatch(this.token, leftteamid, rightteamid, matchid);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void requestTimeOut(int teamid) {
        try {
            this.client.requesttimeout(token, teamid);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeFivemWindow() {
        try {
            this.client.closefivemwindow(token);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearScoreBoard() {
        try {
            this.client.clearscoreboard(token);
        } catch (TException ex) {
            Logger.getLogger(WPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public WPDisplay.Iface getClient() {
        return this.client;
    }


}
