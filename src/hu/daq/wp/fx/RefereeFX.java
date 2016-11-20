/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

import client.Postgres;
import hu.daq.draganddrop.DragAndDropDecorator;
import hu.daq.wp.Player;
import hu.daq.wp.Referee;
import hu.daq.wp.fx.commonbuttons.EditButton;
import hu.daq.wp.fx.commonbuttons.SaveButton;
import hu.daq.wp.fx.controls.NumField;
import hu.daq.wp.fx.image.PlayerPicture;

import java.util.HashMap;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;

/**
 *
 * @author DAQ
 */
public class RefereeFX extends EntityFX{
    
    Referee referee;
    Label name_label;
    TextField name_field;
    Button edit_button;
    Button save_button;
    PlayerPicture picture;
    GridPane basegrid;
    SimpleBooleanProperty duplicated;
    DragAndDropDecorator ddd;
    
    
    public RefereeFX(Postgres db) {
        this(new Referee(db));
     }
    
    public RefereeFX(Postgres db, int referee_id){
        this(db);
        this.load(referee_id);
    }
    
    public RefereeFX(Referee referee){
        this.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        this.setStyle("-fx-background-color: #7788CC;");
        this.referee = referee;
        this.duplicated = new SimpleBooleanProperty();
        //this.referee = new Player(db);
        this.setMaxSize(250, 50);
        this.setMinSize(250, 50);
        //this.setPadding(new Insets(10));
        //Initializing the controls
        
        this.name_label = new Label();
        this.name_field = new TextField();
        
        this.edit_button = new EditButton();
        this.save_button = new SaveButton();    
        this.basegrid = new GridPane();
        this.picture = new PlayerPicture(this,this.referee.getReferee_pic()); // Adding the referee's picture and binding to the referee object
        this.picture.setPreserveRatio(true);
        this.picture.setFitHeight(50);
        this.ddd = new DragAndDropDecorator(this.picture);
        this.ddd.registerFileReceiver(this.picture);
        this.build();        
    }
    
    //Build the layot and make the bindings
    private void build(){
        this.name_field.setPrefWidth(150);
        this.name_label.setPrefWidth(150);
        this.name_field.textProperty().bindBidirectional(this.referee.getName());
        this.name_label.textProperty().bindBidirectional(this.referee.getName());
  
        this.edit_button.setOnAction((ActionEvent event) -> {
            editOn();
        });
        
        this.save_button.setOnAction((ActionEvent event) -> {
            if (save()){
                editOff();
            }
        });
        this.picture.loadPic();
        this.basegrid.setHgap(5);
        this.basegrid.setVgap(5);
        this.basegrid.setPadding(new Insets(5));
        this.basegrid.add(this.picture, 0, 0, 1, 2);
        this.basegrid.add(this.name_label, 1, 0, 3, 1);
        this.basegrid.add(this.edit_button, 4, 0);
        this.getChildren().add(this.basegrid);
    }
    
    public void editOn(){
        this.basegrid.getChildren().remove(this.name_label);
        this.basegrid.add(this.name_field, 1, 0, 3, 1);
        this.basegrid.getChildren().remove(this.edit_button);
        this.basegrid.add(this.save_button, 4, 0);
        
    }
    
    public void editOff(){
        this.basegrid.getChildren().remove(this.name_field);
        this.basegrid.add(this.name_label, 1, 0, 3, 1);
        this.basegrid.getChildren().remove(this.save_button);
        this.basegrid.add(this.edit_button, 4, 0);    
    }
    
    public Referee getReferee(){
        return this.referee;
    }
    
    public final boolean load(Integer pk){
        return this.referee.load(pk);
    }

    public final boolean load(HashMap<String, String> data){
        return this.referee.load(data);

    }    
    
    public final boolean save(){
        this.picture.savePic();
        return this.referee.save();
    }
    
    public SimpleBooleanProperty getChanged(){
        return this.referee.getChanged();
    }

    public SimpleBooleanProperty getDuplicated() {
        return duplicated;
    }
    
    public boolean isChanged(){
        return this.referee.getChanged().get();
    }
    
    public boolean isDuplicated(){
        return this.duplicated.get();
    }
    @Override
    public String getName() {
        return this.referee.getName().getValueSafe();
    }    
    @Override
    public String getID() {
        return this.referee.getID().toString();
    }

    @Override
    public Boolean isDragable() {
        return true;
    }
    @Override
    public String getType() {
       return "Referee";
    }
    
    @Override
    public Integer getTeamID() {
        return 0;
    } 
    @Override
    public Integer getIDInt() {
        return this.referee.getID();
    }
    
}
