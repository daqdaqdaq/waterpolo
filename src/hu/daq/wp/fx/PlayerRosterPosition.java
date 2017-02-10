/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

import hu.daq.draganddrop.DropObjectDecorator;
import hu.daq.draganddrop.ObjectReceiver;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.wp.Entity;
import hu.daq.wp.fx.commonbuttons.DeleteButton;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 *
 * @author DAQ
 */
public class PlayerRosterPosition extends HBox implements ObjectReceiver, EntityFXHolder {

    private Integer capnum;
    private DeleteButton delbutton;
    private StackPane playerholder;
    private AdvancedTeamFX parentobj;
    private PlayerFX player;

    public PlayerRosterPosition(Integer capnum, AdvancedTeamFX parent) {
        super(3);
        this.parentobj = parent;
        this.capnum = capnum;
        this.delbutton = new DeleteButton();
        this.playerholder = new StackPane();

        Label sign = new Label("Játékos");
        sign.setTextFill(new Color(0.7, 0.7, 0.7, 1));
        this.playerholder.getChildren().add(sign);
        this.playerholder.setBackground(new Background(new BackgroundFill(new Color(0.9, 0.9, 0.9, 1), null, null)));
        this.build();
    }

    private void build() {
        DropObjectDecorator.decorate(this, this, DataFormat.PLAIN_TEXT, TransferMode.MOVE);
        this.setMinHeight(50);
        this.setAlignment(Pos.CENTER);
        this.playerholder.setMinWidth(250);
        this.delbutton.setOnAction(e -> this.removePlayer());

        Label capnumlabel = new Label(this.capnum.toString());
        capnumlabel.setPrefWidth(20);
        HBox.setHgrow(capnumlabel, Priority.NEVER);
        HBox.setHgrow(this.delbutton, Priority.NEVER);
        HBox.setHgrow(this.playerholder, Priority.SOMETIMES);
        this.getChildren().addAll(capnumlabel, this.playerholder, this.delbutton);
    }

    public void setPlayer(PlayerFX player) {
        if (this.player != null) {
            this.removePlayer();
        }
        if (this.parentobj.getTeamID().equals(0)){
            this.parentobj.save();
        }
        this.player = player;
        this.playerholder.getChildren().add(this.player);
        if (!player.getPlayer().getCapnum().getValue().equals(this.capnum) || !player.getPlayer().getTeam_id().getValue().equals(this.parentobj.getTeamID()) || !player.getActive().getValue()) {
            player.getPlayer().setCapnum(this.capnum);
            player.getPlayer().setTeamid(this.parentobj.getTeamID());
            player.activate();
            player.save();
        }
    }

    public void removePlayer() {
        if (this.player != null) {
            this.parentobj.handlePlayerRemove(this.player);
            this.playerholder.getChildren().remove(this.player);
            this.player = null;
        }
    }

    public void clearPlayer() {
        if (this.player != null) {
            this.playerholder.getChildren().remove(this.player);
            this.player = null;
        }
    }

    public Integer getCapnum() {
        return capnum;
    }

    @Override
    public void handleObject(Object source, String object) {
        System.out.println("Handling...." + object + ":" + source);

        /*
         Drag between windows a bit akward and sets the dragsource to null
         In that case try to get the drag source from the ServiceHandler. Don't forget to store it in there!!!        
         */
        if (source == null) {
            source = ServiceHandler.getInstance().getDragSource();
        }
        EntityFX dsource = ((ListCell<EntityFX>) source).getItem();

        //System.out.println("The source is:" + dsource.toString());
        if (dsource.getType().equals("Player")) {
            //System.out.println("The type is:" + dsource.getType());
            ((ListCell<EntityFX>) source).getListView().getItems().remove(dsource);
            this.setPlayer((PlayerFX) dsource);
        }

    }

    @Override
    public void remove(EntityFX ent) {
        this.clearPlayer();
    }
}
