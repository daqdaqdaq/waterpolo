/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

import client.Postgres;
import hu.daq.draganddrop.DragAndDropDecorator;
import hu.daq.wp.Player;
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
public class PlayerFX extends AnchorPane implements Instructable{
    
    Player player;
    Label capnum_label;
    NumField capnum_field;
    Label name_label;
    TextField name_field;
    Button edit_button;
    Button save_button;
    PlayerPicture picture;
    GridPane basegrid;
    SimpleBooleanProperty duplicated;
    DragAndDropDecorator ddd;
    
    
    public PlayerFX(Postgres db) {
        this(new Player(db));
     }
    
    public PlayerFX(Postgres db, int player_id){
        this(db);
        this.load(player_id);
    }
    
    public PlayerFX(Player player){
        this.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        this.setStyle("-fx-background-color: #77AACC;");
        this.player = player;
        this.duplicated = new SimpleBooleanProperty();
        //this.player = new Player(db);
        this.setMaxSize(250, 60);
        this.setMinSize(250, 60);
        //this.setPadding(new Insets(10));
        //Initializing the controls

        
        this.capnum_label = new Label();
        this.capnum_field = new NumField();
        
        this.name_label = new Label();
        this.name_field = new TextField();
        
        this.edit_button = new EditButton();
        this.save_button = new SaveButton();    
        this.basegrid = new GridPane();
        this.picture = new PlayerPicture(this,this.player.getPlayer_pic()); // Adding the player's picture and binding to the player object
        this.picture.setPreserveRatio(true);
        this.picture.setFitHeight(50);
        this.ddd = new DragAndDropDecorator(this.picture);
        this.ddd.registerFileReceiver(this.picture);
        this.build();        
    }
    
    //Build the layot and make the bindings
    private void build(){
        
        this.capnum_field.setPrefColumnCount(1) ;
        this.capnum_field.textProperty().bindBidirectional(this.player.getCapnum(),  new NumberStringConverter());
        this.capnum_label.textProperty().bindBidirectional(this.player.getCapnum(),  new NumberStringConverter());

        this.name_field.setPrefWidth(150);
        this.name_label.setPrefWidth(150);
        this.name_field.textProperty().bindBidirectional(this.player.getName());
        this.name_label.textProperty().bindBidirectional(this.player.getName());
        
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
        this.basegrid.add(this.name_label, 1, 0, 2, 1);
        this.basegrid.add(this.capnum_label, 1, 1);
        this.basegrid.add(this.edit_button, 2, 1);
        this.getChildren().add(this.basegrid);
    }
    
    public void editOn(){
        this.basegrid.getChildren().remove(this.name_label);
        this.basegrid.add(this.name_field, 1, 0, 2, 1);
        this.basegrid.getChildren().remove(this.capnum_label);
        this.basegrid.add(this.capnum_field, 1, 1);
        this.basegrid.getChildren().remove(this.edit_button);
        this.basegrid.add(this.save_button, 2, 1);
        
    }
    
    public void editOff(){
        this.basegrid.getChildren().remove(this.name_field);
        this.basegrid.add(this.name_label, 1, 0, 2, 1);
        this.basegrid.getChildren().remove(this.capnum_field);
        this.basegrid.add(this.capnum_label, 1, 1);
        this.basegrid.getChildren().remove(this.save_button);
        this.basegrid.add(this.edit_button, 2, 1);    
    }
    
    
    public final boolean load(Integer pk){
        return this.player.load(pk);
    }

    public final boolean load(HashMap<String, String> data){
        return this.player.load(data);

    }    
    
    public final boolean save(){
        this.picture.savePic();
        return this.player.save();
    }
    
    public SimpleBooleanProperty getChanged(){
        return this.player.getChanged();
    }

    public SimpleBooleanProperty getDuplicated() {
        return duplicated;
    }
    
    public Integer getCapnum(){
        return this.player.getCapnum().getValue();
    }
    
    public boolean isChanged(){
        return this.player.getChanged().get();
    }
    
    public SimpleBooleanProperty getActive(){
        return this.player.getActive();
    }
    
    public boolean isActive(){
        return this.player.getActive().get();
    }
    
    public boolean isDuplicated(){
        return this.duplicated.get();
    }

    @Override
    public void execute() {
        this.editOn();
    }
}
