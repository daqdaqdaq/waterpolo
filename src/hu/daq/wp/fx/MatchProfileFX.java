/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

import hu.daq.servicehandler.ServiceHandler;
import hu.daq.wp.MatchProfile;
import hu.daq.wp.fx.commonbuttons.AddButton;
import hu.daq.wp.fx.commonbuttons.DeleteButton;
import hu.daq.wp.fx.commonbuttons.SaveButton;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author DAQ
 */
public class MatchProfileFX extends StackPane {

    private MatchProfilesFX profiles;
    private MatchProfile currentprofile;
    private SaveButton savebutton;
    private DeleteButton deletebutton;
    private AddButton newbutton;
    private TextField profilename;
    private Spinner balltime;
    private Spinner numlegs;
    private Spinner legduration;
    private Spinner breakduration;    
    private Spinner midbreakduration;
    private Spinner numovertimes;
    private Spinner overtimeduration;    

    public MatchProfileFX() {
        this.currentprofile = new MatchProfile();
        this.profiles = new MatchProfilesFX();
        this.savebutton = new SaveButton();
        this.deletebutton = new DeleteButton();
        this.newbutton = new AddButton();
        this.profilename = new TextField();
        this.balltime = new Spinner(1,60,30);
        this.balltime.setEditable(true);
        this.numlegs = new Spinner(1,6,4);
        this.numlegs.setEditable(true);        
        this.legduration = new Spinner(1,600,480);        
        this.legduration.setEditable(true);        
        this.breakduration = new Spinner(1,600,120);
        this.breakduration.setEditable(true);        
        this.midbreakduration = new Spinner(1,600,300);
        this.midbreakduration.setEditable(true);        
        this.numovertimes = new Spinner(0,5,0);      
        this.numovertimes.setEditable(true);        
        this.overtimeduration = new Spinner(0,600,0);
        this.overtimeduration.setEditable(true);        
  
        
        this.build();
    }

    private void build() {
        this.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));        
        VBox mainbox = new VBox(2);
        HBox controls = new HBox(2);
        controls.setPadding(new Insets(0,10,0,10));
        HBox.setHgrow(this.profiles, Priority.ALWAYS);
        controls.getChildren().addAll(this.profiles, this.savebutton, this.deletebutton, this.newbutton);
        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(0,10,0,10));
        grid.add(this.profilename, 0, 0);
        grid.add(new Label("Játékrészek száma: "), 0, 1);
        grid.add(this.numlegs, 1, 1);
        grid.add(new Label("Játékrész hossza (s): "), 0, 2);
        grid.add(this.legduration, 1, 2);        
        grid.add(new Label("Szünet hossza (s): "), 0, 3);
        grid.add(this.breakduration, 1, 3);        
        grid.add(new Label("Nagyszünet hossza (s): "), 0, 4);
        grid.add(this.midbreakduration, 1, 4);        
        grid.add(new Label("Hosszabítások száma: "), 0, 5);
        grid.add(this.numovertimes, 1, 5);        
        grid.add(new Label("Hosszabitás hossza (s): "), 0, 6);
        grid.add(this.overtimeduration, 1, 6);        
        grid.add(new Label("Támadóidő (s): "), 0, 7);        
        grid.add(this.balltime, 1, 7);        

        mainbox.getChildren().addAll(controls,grid);
        this.getChildren().add(mainbox);
        
        this.newbutton.setOnAction((E)->this.addNew());
        this.deletebutton.setOnAction((E)->this.deleteProfile());
        this.savebutton.setOnAction((E)->this.saveProfile());
        this.profiles.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends MatchProfile> ob, MatchProfile ov, MatchProfile nv) ->this.setEntity(nv));
    }

    private void setEntity(MatchProfile profile){
        this.profilename.textProperty().unbindBidirectional(this.currentprofile.profilename);
        this.profilename.textProperty().bindBidirectional(profile.profilename);

        this.balltime.getValueFactory().valueProperty().unbindBidirectional(this.currentprofile.balltimelength);
        this.balltime.getValueFactory().valueProperty().bindBidirectional(profile.balltimelength);
        
        this.numlegs.getValueFactory().valueProperty().unbindBidirectional(this.currentprofile.numlegs);        
        this.numlegs.getValueFactory().valueProperty().bindBidirectional(profile.numlegs);

        this.legduration.getValueFactory().valueProperty().unbindBidirectional(this.currentprofile.legduration);        
        this.legduration.getValueFactory().valueProperty().bindBidirectional(profile.legduration);

        this.breakduration.getValueFactory().valueProperty().unbindBidirectional(this.currentprofile.breakduration);          
        this.breakduration.getValueFactory().valueProperty().bindBidirectional(profile.breakduration);

        this.midbreakduration.getValueFactory().valueProperty().unbindBidirectional(this.currentprofile.midbreakduration);          
        this.midbreakduration.getValueFactory().valueProperty().bindBidirectional(profile.midbreakduration);
        
        this.numovertimes.getValueFactory().valueProperty().unbindBidirectional(this.currentprofile.numovertimes);          
        this.numovertimes.getValueFactory().valueProperty().bindBidirectional(profile.numovertimes);
        
        this.overtimeduration.getValueFactory().valueProperty().unbindBidirectional(this.currentprofile.overtimeduration);          
        this.overtimeduration.getValueFactory().valueProperty().bindBidirectional(profile.overtimeduration);        
        
        this.currentprofile = profile;
    } 
    
    private void addNew(){
        MatchProfile mp = new MatchProfile();
        mp.profilename.set("Új profil");
        this.profiles.getItems().add(mp);
        this.profiles.getSelectionModel().select(mp);
        this.setEntity(mp);
    }

    private void saveProfile(){
        ServiceHandler.getInstance().getDbService().save(this.currentprofile);
    }
    
    private void deleteProfile(){
        MatchProfile mp = this.profiles.getSelected();
        this.profiles.getSelectionModel().clearAndSelect(0);
        this.setEntity(this.profiles.getSelected());
        this.profiles.getItems().remove(mp);
        ServiceHandler.getInstance().getDbService().deleteMatchProfile(mp);
    }
    
    public void load(){
        this.profiles.load();
        this.profiles.getSelectionModel().select(0);
    }
}
