/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

import client.Postgres;
import hu.daq.draganddrop.DragObjectDecorator;
import hu.daq.draganddrop.DropObjectDecorator;
import hu.daq.draganddrop.ObjectReceiver;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.utils.MappedList;
import hu.daq.wp.Team;
import hu.daq.wp.Teams;
import hu.daq.wp.fx.screens.entityselector.DragEntityMediator;
import hu.daq.wp.fx.screens.teamselector.DragTeamMediator;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 *
 * @author DAQ
 */
public class EntityListFX extends StackPane implements ObjectReceiver {

    private final Postgres db;
    private final FilteredList<EntityFX> filteredents;
    private final TextField filterfield;
    private final ListView<EntityFX> entitiesview;
    private EntitiesFX entities;
    private ObservableList<EntityFX> entfx;
    private CheckBox playercb;
    private CheckBox coachcb;
    private CheckBox refereecb;
    private CheckBox actteamonly;
    private SimpleIntegerProperty selectedteam;
    private Button addplayer;
    private Button addcoach;
    private Button addreferee;
    Governed target;

    public EntityListFX(Postgres db, Governed target) {

        this.db = db;
        this.entfx = FXCollections.observableArrayList(new ArrayList<EntityFX>());
        this.filterfield = new TextField();
        this.entitiesview = new ListView<EntityFX>();
        this.entities = new EntitiesFX(db);
        this.filteredents = new FilteredList<EntityFX>(entfx, p -> {
            return true;
        });
        this.playercb = new CheckBox("Játékosok");
        this.playercb.setSelected(true);
        this.coachcb = new CheckBox("Edzők");
        this.coachcb.setSelected(true);
        this.refereecb = new CheckBox("Játékvezetők");
        this.refereecb.setSelected(true);
        this.actteamonly = new CheckBox("Csak az aktuális csapat");
        this.actteamonly.setSelected(false);
        this.addplayer = new Button("+Játékos");
        this.addcoach = new Button("+Edző");
        this.addreferee = new Button("+Játékvezető");
        this.selectedteam = new SimpleIntegerProperty();
        this.target = target;
        this.build();
    }

    public EntityListFX(Postgres db) {
        this(db, null);
    }

    private void build() {
        DropObjectDecorator.decorate(this.entitiesview, this, DataFormat.PLAIN_TEXT, TransferMode.MOVE);
        //wrap the filtered teams list into a sorted list
        SortedList<EntityFX> slt = new SortedList<EntityFX>(this.filteredents);
        slt.setComparator((l, r) -> l.getName().compareTo(r.getName()));
        //if the filterfiled is changeing adjust the filter predicate
        this.filterfield.textProperty().addListener((observable, ov, nv) -> {
            this.filteredents.setPredicate(t -> {
                return this.assesEntity(t);
            });
        });
        this.playercb.selectedProperty().addListener((observable, ov, nv) -> {
            this.filteredents.setPredicate(t -> {
                return this.assesEntity(t);
            });
        });

        this.coachcb.selectedProperty().addListener((observable, ov, nv) -> {
            this.filteredents.setPredicate(t -> {
                return this.assesEntity(t);
            });
        });
        this.refereecb.selectedProperty().addListener((observable, ov, nv) -> {
            this.filteredents.setPredicate(t -> {
                return this.assesEntity(t);
            });
        });

        this.actteamonly.selectedProperty().addListener((observable, ov, nv) -> {
            this.filteredents.setPredicate(t -> {
                return this.assesEntity(t);
            });
        });

        this.selectedteam.addListener((observable, ov, nv) -> {
            this.filteredents.setPredicate(t -> {
                return this.assesEntity(t);
            });
        });

        this.addplayer.setOnAction(ev -> {
            this.addPlayer();
        });
        this.addcoach.setOnAction(ev -> {
            this.addCoach();
        });
        this.addreferee.setOnAction(ev -> {
            this.addReferee();
        });

        this.entitiesview.setCellFactory(new Callback<ListView<EntityFX>, ListCell<EntityFX>>() {

            @Override
            public ListCell<EntityFX> call(ListView<EntityFX> param) {
                ListCell<EntityFX> cell = new ListCell<EntityFX>() {

                    @Override
                    protected void updateItem(EntityFX item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setGraphic(item);
                            //setText(item.getTeam().getTeamname().getValue());
                            DragObjectDecorator.decorate(this, new DragEntityMediator(item), TransferMode.MOVE);
                        } else {
                            setGraphic(null);
                            //setText("");   
                        }
                    }

                };
                /*
                 cell.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent evt) -> {
                 final int index = cell.getIndex();
                 if (evt.getButton().equals(MouseButton.PRIMARY)) {
                 if (teamsview.getSelectionModel().getSelectedIndex() == cell.getIndex()) {
                 if (target!=null){
                 target.fireEvent(cell.getItem().getTeam());
                 }
                 //tf.load(cell.getItem().getTeam_id().getValue());
                 evt.consume();
                 }
                 } else {
                 evt.consume();
                 //Right button code here
                 }
                 });
                 */
                //if (cell.getItem()!=null) DragObjectDecorator.decorate(cell, new DragTeamMediator(cell.getItem()), TransferMode.COPY);
                return cell;
            }

        });
        this.entitiesview.setItems(slt);
        HBox upperbox = new HBox(3);
        upperbox.getChildren().addAll(this.filterfield, this.buildCheckBoxHolder());
        HBox lowerbox = new HBox(3);
        lowerbox.getChildren().addAll(this.addplayer, this.addcoach, this.addreferee);
        VBox vb = new VBox();
        vb.getChildren().addAll(upperbox, this.entitiesview, lowerbox);
        this.getChildren().add(vb);
    }

    private VBox buildCheckBoxHolder() {
        VBox vb = new VBox();
        vb.getChildren().addAll(this.playercb, this.coachcb, this.refereecb, this.actteamonly);

        return vb;

    }

    private boolean assesEntity(EntityFX ent) {
        String nv = this.filterfield.getText();
        return (nv == null || nv.isEmpty() || ent.getName().toLowerCase().contains(nv.toLowerCase())) && ((this.playercb.isSelected() && "Player".equals(ent.getType()))
                || (this.coachcb.isSelected() && "Coach".equals(ent.getType()))
                || (this.refereecb.isSelected() && "Referee".equals(ent.getType())))
                && (!this.actteamonly.isSelected() || (this.actteamonly.isSelected() && ent.getTeamID().equals(this.selectedteam.getValue())));

    }

    public void loadEntities() {
        this.entfx.setAll(this.entities.loadAll());

    }

    private void addPlayer() {
        PlayerFX pf = new PlayerFX(this.db);
        pf.editOn();
        pf.name_field.requestFocus();
        this.entfx.add(pf);
    }

    private void addCoach() {
        CoachFX pf = new CoachFX(this.db);
        pf.editOn();
        pf.name_field.requestFocus();
        this.entfx.add(pf);
    }

    private void addReferee() {
        RefereeFX pf = new RefereeFX(this.db);
        pf.editOn();
        pf.name_field.requestFocus();
        this.entfx.add(pf);
    }

    public ObservableList<EntityFX> getEntities() {
        return this.entfx;
    }

    public void setSelectedTeam(Integer teamid) {
        this.selectedteam.set(teamid);
    }

    @Override
    public void handleObject(Object source, String object) {
        System.out.println("Handling.... another" + object + ":" + source);

        /*
         Drag between windows a bit akward and sets the dragsource to null
         In that case try to get the drag source from the ServiceHandler. Don't forget to store it in there!!!        
         */
        if (source == null) {
            source = ServiceHandler.getInstance().getDragSource();
        }
        //try to handle the source as a player list entity
        try {
            EntityFX dsource = ((ListCell<EntityFX>) source).getItem();

            System.out.println("The source is:" + dsource.toString());
            if (dsource.getType().equals("Player")) {
                System.out.println("The type is:" + dsource.getType() + ": " + ((PlayerFX) dsource).toString());

                ((PlayerFX) dsource).getPlayer().setTeamid(0);
                ((PlayerFX) dsource).getPlayer().setCapnum(0);
                ((PlayerFX) dsource).inactivate();
                ((PlayerFX) dsource).save();

                ((ListCell<EntityFX>) source).getListView().getItems().remove(dsource);
                this.entitiesview.getItems().add(dsource);
            }
        } catch (Exception ex) {

        }
    }
}
