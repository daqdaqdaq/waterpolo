/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.screens;

import hu.daq.login.LoginService;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.controlsfx.dialog.LoginDialog;

/**
 *
 * @author DAQ
 */
public class MainPage extends TabPane {

    private Boolean loggedin;
    private LoginDialog protector;


    public MainPage(LoginDialog protector) {
        this.loggedin = false;
        this.protector = protector;


    }

    public void addScreen(SubScreen nd, String caption) {
        if (!this.loggedin) {
            protector.showAndWait();
            this.loggedin = true;
        }
        if (!nd.isAdminOnly() || LoginService.getInst().isAdmin()) {
            nd.addContainer(this);
            nd.initScreen();
            Tab tb = new Tab(caption);
            tb.closableProperty().set(false);
            tb.setContent((Node) nd);
            this.getTabs().add(tb);
        }
    }
    

}
