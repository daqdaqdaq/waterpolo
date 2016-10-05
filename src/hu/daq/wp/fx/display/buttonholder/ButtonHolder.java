/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.buttonholder;

import hu.daq.wp.fx.commonbuttons.DownButton;
import hu.daq.wp.fx.commonbuttons.UpButton;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 *
 * @author DAQ
 */
public class ButtonHolder extends VBox {

    HBox visiblebuttons;
    HBox hiddenbuttons;
    UpButton upbtn;
    DownButton downbtn;

    public ButtonHolder() {
        this.upbtn = new UpButton();
        this.upbtn.managedProperty().bind(this.upbtn.visibleProperty());
        this.downbtn = new DownButton();
        this.downbtn.managedProperty().bind(this.downbtn.visibleProperty());
        this.visiblebuttons = new HBox();
        this.hiddenbuttons = new HBox();
        this.hiddenbuttons.managedProperty().bind(this.hiddenbuttons.visibleProperty());
        this.visiblebuttons.getChildren().addAll(this.makeSpacer(),this.upbtn,this.downbtn);
        this.upbtn.setVisible(false);
        this.downbtn.setOnAction((ev)->this.showButtons());
        this.upbtn.setOnAction((ev)->this.hideButtons());
        this.hideButtons();
        this.getChildren().addAll(this.visiblebuttons,this.hiddenbuttons);

    }

    public void addVisibleButton(ButtonBase btn) {
        this.visiblebuttons.getChildren().add(this.visiblebuttons.getChildren().size() - 3, btn);
    }

    public void addHiddenButton(ButtonBase btn) {
        this.hiddenbuttons.getChildren().add(btn);
    }
    private Region makeSpacer() {
        Region padder = new Region();
        padder.setMaxWidth(10);
        padder.setMinWidth(10);
        padder.setPrefHeight(2);
        HBox.setHgrow(padder, Priority.NEVER);
        return padder;
    }

    public void addVisibleSpacer() {
        this.visiblebuttons.getChildren().add(this.visiblebuttons.getChildren().size() - 3,this.makeSpacer());
    }

    public void addHiddenSpacer() {
        this.hiddenbuttons.getChildren().add(this.makeSpacer());

    }
    
    private void showButtons(){
        this.hiddenbuttons.setVisible(true);
        this.downbtn.setVisible(false);
        this.upbtn.setVisible(true);
    }

    private void hideButtons(){
        this.hiddenbuttons.setVisible(false);
        this.downbtn.setVisible(true);
        this.upbtn.setVisible(false);
    }    
}
