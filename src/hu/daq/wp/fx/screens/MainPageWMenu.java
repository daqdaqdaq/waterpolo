/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.screens;

import hu.daq.login.LoginService;
import hu.daq.servicehandler.ServiceHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.dialog.LoginDialog;

/**
 *
 * @author DAQ
 */
public class MainPageWMenu extends StackPane implements MainPageCommon {
    Application app;
    private Boolean loggedin;
    private Dialog protector;
    private Integer leftteam;
    private Integer rightteam;
    private MenuBar menubar;
    private StackPane content;
    private HashMap<String,SubScreen> screens;
    private Menu menu;

    public MainPageWMenu(Dialog protector) {
        this.loggedin = false;
        this.protector = protector;
        this.screens = new HashMap<String,SubScreen>();
        this.menubar = new MenuBar();
        this.menu = new Menu("Képernyők");
        this.menubar.getMenus().add(menu);
        this.content = new StackPane();
        VBox vb = new VBox();
        VBox.setVgrow(this.menubar, Priority.NEVER);
        vb.getChildren().addAll(this.menubar,this.content);
        this.getChildren().add(vb);

    }

    @Override
    public void addScreen(SubScreen nd, String caption) {
        if (!this.loggedin) {
            protector.showAndWait().filter(response -> response == ButtonType.CANCEL).ifPresent(response -> this.closeApp());
            this.loggedin = true;
        }
        if (!nd.isAdminOnly() || LoginService.getInst().isAdmin()) {
            nd.addContainer(this);
            nd.initScreen();
            MenuItem mi = new MenuItem(caption);
            mi.setOnAction(ev -> {
                this.switchToScreen(caption);
            });
            this.menu.getItems().add(mi);
            this.screens.put(caption, nd);
        }
    }

    private void switchToScreen(String screencaption) {
        Node node = (Node)this.screens.get(screencaption);
        this.content.getChildren().clear();
        this.content.getChildren().add(node);
        this.content.setPrefHeight(node.getBoundsInParent().getHeight());
        this.content.setPrefWidth(node.getBoundsInParent().getWidth());        
        ((Stage) this.getScene().getWindow()).sizeToScene();
    }

    @Override
    public void setPlayingTeams(Integer leftteam, Integer rightteam) {
        this.leftteam = leftteam;
        this.rightteam = rightteam;

    }

    @Override
    public Integer getLeftTeam() {
        return this.leftteam;
    }

    @Override
    public Integer getRightTeam() {
        return this.rightteam;
    }
    
    public void closeApp(){
        ServiceHandler.getInstance().cleanupServices();
        ((Stage)this.getScene().getWindow()).close();
    }
    
    public void showLogin(){
        if (!this.loggedin) {
            protector.showAndWait().filter(response -> response == ButtonType.CANCEL).ifPresent(response -> this.closeApp());
            this.loggedin = true;
        }     
    }
    
}
