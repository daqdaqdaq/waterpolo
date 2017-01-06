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
public class CoachPosition extends HBox implements ObjectReceiver, EntityFXHolder {

    private DeleteButton delbutton;
    private StackPane coachholder;
    private AdvancedTeamFX parentobj;
    private CoachFX coach;

    public CoachPosition(AdvancedTeamFX parent) {
        super(3);
        this.parentobj = parent;
        this.delbutton = new DeleteButton();
        this.coachholder = new StackPane();
        this.build();
    }

    private void build() {
        DropObjectDecorator.decorate(this, this, DataFormat.PLAIN_TEXT, TransferMode.MOVE);
        this.setMinHeight(50);
        this.coachholder.setMinWidth(250);
        this.setAlignment(Pos.CENTER);
        this.delbutton.setOnAction(e -> this.removeCoach());
        HBox.setHgrow(this.delbutton, Priority.NEVER);
        HBox.setHgrow(this.coachholder, Priority.SOMETIMES);
        Label sign = new Label("Edző");
        sign.setTextFill(new Color(0.7, 0.7, 0.7, 1));
        this.coachholder.getChildren().add(sign);
        this.coachholder.setBackground(new Background(new BackgroundFill(new Color(0.9, 0.9, 0.9, 1), null, null)));
        this.getChildren().addAll(this.coachholder, this.delbutton);
    }

    public void setCoach(CoachFX coach) {
        if (this.coach != null) {
            this.removeCoach();
        }
        this.coach = coach;
        this.coachholder.getChildren().add(this.coach);
        if (!coach.getCoach().getTeam_id().getValue().equals(this.parentobj.getTeamID())) {
            coach.getCoach().setTeamid(this.parentobj.getTeamID());
            coach.save();
        }
    }

    public void removeCoach() {
        if (this.coach != null) {
            this.parentobj.handleCoachRemove(this.coach);
            this.coachholder.getChildren().remove(this.coach);
            this.coach = null;
        }
    }

    public void clearCoach() {
        if (this.coach != null) {
            this.coachholder.getChildren().remove(this.coach);
            this.coach = null;
        }
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

        System.out.println("The source is:" + dsource.toString());
        if (dsource.getType().equals("Coach")) {
            System.out.println("The type is:" + dsource.getType());
            CoachFX p = new CoachFX(((CoachFX) dsource).getCoach());
            //p.load(((CoachFX)dsource).getCoach().getID());
            //((ListCell<EntityFX>) source).getListView().getItems().remove(dsource);
            this.setCoach(p);
        }

    }
    
   @Override
    public void remove(EntityFX ent) {
        this.clearCoach();
    }    
}
