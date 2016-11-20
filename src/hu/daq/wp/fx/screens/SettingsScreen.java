/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.screens;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 *
 * @author DAQ
 */
public class SettingsScreen extends StackPane implements SubScreen {

    private MainPageCommon parent;

    private final Boolean adminonly;

    public SettingsScreen() {
        this.adminonly = false;
        this.build();
    }
    
    private void build(){
        this.getChildren().add( new Label("Settings"));
    }
    
    @Override
    public SubScreen addContainer(MainPageCommon nd) {
        this.parent = nd;
        return this;
    }

    @Override
    public MainPageCommon getContainer() {
        return this.parent;
    }

    @Override
    public void initScreen() {

    }

    @Override
    public Boolean isAdminOnly() {
        return this.adminonly;
    } 
    
}
