/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.control;

import client.Postgres;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.thriftconnector.server.WPDisplayServer;
import hu.daq.timeengine.TimeEngine;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author DAQ
 */
public class ControllerScreen extends StackPane {

    private String securitytoken;
    private String user;
    private ControlledScreen activescreen;
    private HashMap<String, ControlledScreen> screens;
    private TimeEngine timeengine;
    private Postgres db;
    private WPDisplayServer wpds;

    public ControllerScreen(Postgres db, Stage stg) {
        this.securitytoken = "";
        this.db = db;
        this.screens = new HashMap<String, ControlledScreen>();
        this.timeengine = ServiceHandler.getInstance().getTimeEngine();

        try {
            this.wpds = new WPDisplayServer(this, 19999);
            ServiceHandler.getInstance().getThriftConnector().registerServer(wpds);
            this.wpds.startServer();
        } catch (TTransportException ex) {
            ex.printStackTrace();
        }
        //installing a close request listener to the stage for cleanup purposes
        this.installStageListener(stg);
    }

    public String login(String token) {
        String sendstr = "select checktoken('" + token + "') as username";
        String usr = db.query(sendstr).get(0).get("username");
        if (!usr.equals("")) {
            this.user = usr;
            this.securitytoken = token;
        }
        return this.user;
    }

    public Boolean checkToken(String token) {
        if (!token.equals("")) {
            if (this.securitytoken.equals("")) {
                this.login(token);
            }
            if (this.securitytoken.equals(token)) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    public TimeEngine getTimeEngine() {
        return this.timeengine;
    }

    public void addScreen(ControlledScreen cs, String title) {

        this.screens.put(title, cs);
    }

    public void switchScreen(String title) {
        
        try {
            this.activescreen = this.screens.get(title);
            Platform.runLater(() -> {
                this.getChildren().clear();
                this.getChildren().add((Node) this.activescreen);
            });

        } catch (Exception e) {

        }

    }

    public ControlledScreen getScreen() {
        return this.activescreen;
    }

    public ControlledScreen getScreen(String title) {
        return this.screens.get(title);
    }

    public void removeScreen(String title) {
        this.screens.remove(title);
    }

    public void removeAllScreen() {
        this.screens.clear();
    }

    public ResultWrapper sendCommand(Command com) {

        return this.activescreen.executeCommand(com);

    }

    private void installStageListener(Stage stg) {
        stg.setOnCloseRequest((E) -> this.cleanup());

    }

    public Postgres getDb() {
        return db;
    }

    //cleanup jobs
    private void cleanup() {
        this.wpds.stopServer();
        this.db.disconnect();
    }

}
