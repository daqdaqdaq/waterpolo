/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

import client.Postgres;
import hu.daq.draganddrop.DragAndDropDecorator;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.wp.Player;
import hu.daq.wp.fx.commonbuttons.DeleteButton;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;

/**
 *
 * @author DAQ
 */
public class PlayerFX extends EntityFX<Player> {

    Player player;
    Label name_label;
    TextField name_field;
    Label shortname_label;
    TextField shortname_field;
    Button edit_button;
    Button save_button;
    Button delete_button;
    PlayerPicture picture;
    GridPane basegrid;
    SimpleBooleanProperty duplicated;
    DragAndDropDecorator ddd;
    HBox buttons;

    public PlayerFX(int player_id) {
        this(ServiceHandler.getInstance().getDbService().getPlayer(player_id));
    }

    public PlayerFX(Player player) {
        this.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        this.setStyle("-fx-background-color: #77AACC;");
        this.name_label = new Label();
        this.name_field = new TextField();
        this.shortname_label = new Label();
        this.shortname_field = new TextField();
        this.setEntity(player);
        this.duplicated = new SimpleBooleanProperty();
        //this.player = new Player(db);
        this.setMaxSize(250, 50);
        this.setMinSize(250, 50);
        //this.setPadding(new Insets(10));
        //Initializing the controls

        this.edit_button = new EditButton();
        this.save_button = new SaveButton();
        this.delete_button = new DeleteButton();
        this.buttons = new HBox(3);
        this.buttons.getChildren().addAll(this.edit_button,this.delete_button);
        this.basegrid = new GridPane();
        this.picture = new PlayerPicture(this, this.player.getPlayer_pic()); // Adding the player's picture and binding to the player object
        this.picture.setPreserveRatio(true);
        this.picture.setFitHeight(50);
        this.ddd = new DragAndDropDecorator(this.picture);
        this.ddd.registerFileReceiver(this.picture);
        this.build();
    }

    @Override
    protected void setEntity(Player entity) {
        this.player = entity;
        this.name_field.textProperty().bindBidirectional(this.player.getName());
        this.name_label.textProperty().bindBidirectional(this.player.getName());
        this.shortname_field.textProperty().bindBidirectional(this.player.getShortname());
        this.shortname_label.textProperty().bindBidirectional(this.player.getShortname());
        this.deleted.bind(this.player.getDeleted());
        /*this.deleted.addListener((ob, ov, nv) -> {
            if (nv.equals(Boolean.TRUE)) {
                this.removeMe();
            }
        });*/
    }

//Build the layot and make the bindings
    private void build() {
        this.name_field.setPrefWidth(150);
        this.name_label.setPrefWidth(150);
        this.shortname_field.setPrefWidth(100);
        this.shortname_label.setPrefWidth(100);
        this.edit_button.setOnAction((ActionEvent event) -> {
            editOn();
        });

        this.save_button.setOnAction((ActionEvent event) -> {
            if (save()) {
                editOff();
            }
        });
        this.delete_button.setOnAction((ActionEvent event) -> {
            ServiceHandler.getInstance().getDbService().deletePlayer(this.player);
        });        
        this.picture.loadPic();
        this.basegrid.setHgap(5);
        this.basegrid.setVgap(5);
        this.basegrid.setPadding(new Insets(5));
        this.basegrid.add(this.picture, 0, 0, 1, 2);
        this.basegrid.add(this.name_label, 1, 0, 3, 1);
        this.basegrid.add(this.shortname_label, 2, 1, 2, 1);
        //this.basegrid.add(this.edit_button, 4, 0);
        this.basegrid.add(this.buttons, 4, 0);
        this.getChildren().add(this.basegrid);
    }

    public void editOn() {
        this.basegrid.getChildren().remove(this.name_label);
        this.basegrid.add(this.name_field, 1, 0, 3, 1);
        this.basegrid.getChildren().remove(this.shortname_label);
        this.basegrid.add(this.shortname_field, 2, 1, 2, 1);
        this.basegrid.getChildren().remove(this.buttons);
        this.basegrid.add(this.save_button, 4, 0);

    }

    public void editOff() {
        this.basegrid.getChildren().remove(this.name_field);
        this.basegrid.add(this.name_label, 1, 0, 3, 1);
        this.basegrid.getChildren().remove(this.shortname_field);
        this.basegrid.add(this.shortname_label, 2, 1, 2, 1);
        this.basegrid.getChildren().remove(this.save_button);
        this.basegrid.add(this.buttons, 4, 0);
    }

    public final void load(Integer pk) {
        this.player = ServiceHandler.getInstance().getDbService().getPlayer(pk);
    }

    public final boolean save() {
        this.picture.savePic();
        return ServiceHandler.getInstance().getDbService().save(this.player);
    }

    public SimpleBooleanProperty getChanged() {
        return this.player.getChanged();
    }

    public SimpleBooleanProperty getDuplicated() {
        return duplicated;
    }

    public void setCapnum(Integer capnum) {
        this.player.setCapnum(capnum);
    }

    public Integer getCapnum() {
        return this.player.getCapnum().getValue();
    }

    public boolean isChanged() {
        return this.player.getChanged().get();
    }

    public SimpleBooleanProperty getActive() {
        return this.player.getActive();
    }

    public void activate() {
        this.player.getActive().set(true);
    }

    public void inactivate() {
        this.player.getActive().set(false);
    }

    public boolean isActive() {
        return this.player.getActive().get();
    }

    public boolean isDuplicated() {
        return this.duplicated.get();
    }

    @Override
    public String getName() {
        return this.player.getName().getValueSafe();
    }

    @Override
    public String getID() {
        return this.player.getID().toString();
    }

    @Override
    public Boolean isDragable() {
        return true;
    }

    @Override
    public String getType() {
        return "Player";
    }

    @Override
    public Integer getTeamID() {
        return this.player.getTeam_id().getValue();
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public String toString() {
        return this.player.toString();
    }

    @Override
    public Integer getIDInt() {
        return this.player.getID();
    }

    @Override
    protected void onDelete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
