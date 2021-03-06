/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.servicehandler;

import client.Postgres;
import hu.daq.UDPSender.SenderThread;
import hu.daq.dataaccess.DAO;
import hu.daq.keyevent.KeyEventHandler;
import hu.daq.settings.SettingsHandler;
import hu.daq.thriftconnector.connector.ThriftConnector;
import hu.daq.timeengine.TimeEngine;
import hu.daq.wp.horn.Horn;
import hu.daq.wp.matchorganizer.MatchOrganizer;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author DAQ
 */
public class ServiceHandler {

    private static ServiceHandler instance;
    private Postgres db;
    private DAO dbservice;
    private TimeEngine te;
    private Object dragsource;
    private ThriftConnector thriftconnector;
    private MatchOrganizer organizer;
    private SenderThread senderthread;
    private Horn horn;
    private SettingsHandler settings;
    private KeyEventHandler keyeventhandler;
    

    private ServiceHandler() {
        this.settings = new SettingsHandler();
        this.te = new TimeEngine();
        this.keyeventhandler = new KeyEventHandler();
        te.init();
        te.pause();
    }

    public static ServiceHandler getInstance() {
        if (instance == null) {
            instance = new ServiceHandler();
        }
        return instance;
    }

    public Postgres getDb() {
        return db;
    }

    public void setDb(Postgres db) {
        this.db = db;
        this.setDbService(new DAO(this.db));
    }

    public DAO getDbService() {
        return dbservice;
    }

    public void setDbService(DAO dbservice) {
        this.dbservice = dbservice;
    }    
    
    public TimeEngine getTimeEngine() {
        return te;
    }

    public Object getDragSource() {
        return dragsource;
    }

    public void setDragSource(Object dragsource) {
        this.dragsource = dragsource;
    }

    public ThriftConnector getThriftConnector() {
        return thriftconnector;
    }

    public void setThriftconnector(ThriftConnector thriftconnector) {
        this.thriftconnector = thriftconnector;
    }

    //registers the cleanup method on the Stage for closing time cleanup
    public void registerCleanup(Stage stage) {
        stage.setOnCloseRequest((ev) -> {
            this.cleanupServices();
        });
    }

    public void setOrganizer(MatchOrganizer organizer) {
        this.organizer = organizer;
    }

    
    public MatchOrganizer getOrganizer() {
        return organizer;
    }

    public SenderThread getSenderthread() {
        return senderthread;
    }

    public void setSenderthread(SenderThread senderthread) {
        this.senderthread = senderthread;
        this.senderthread.start();
    }

    public Horn getHorn() {
        return horn;
    }

    public void setHorn(Horn horn) {
        this.horn = horn;
    }

    public KeyEventHandler getKeyEventHandler() {
        return keyeventhandler;
    }

    public void setKeyEventHandler(KeyEventHandler keyeventhandler) {
        this.keyeventhandler = keyeventhandler;
    }

    public void cleanupServices() {
        this.db.disconnect();
        this.thriftconnector.closeConnections();
        if (this.senderthread != null) {
            this.senderthread.stopSender();
        }

    }

    public void finalize() {
        this.cleanupServices();
    }

    public SettingsHandler getSettings() {
        return settings;
    }
    
}
