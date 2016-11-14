/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

import client.Postgres;
import hu.daq.draganddrop.DragObjectDecorator;
import hu.daq.draganddrop.ObjectReceiver;
import hu.daq.utils.MappedList;
import hu.daq.wp.Team;
import hu.daq.wp.Teams;
import hu.daq.wp.fx.screens.teamselector.DragTeamMediator;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 *
 * @author DAQ
 */
public class TeamListFX extends StackPane {
    

    private final Postgres db;
    private final Teams teams;
    private final FilteredList<TeamSimpleFX> filteredteams;
    private final TextField filterfield;
    private final ListView<TeamSimpleFX> teamsview;
    MappedList<TeamSimpleFX, Team> teamsfx;
    Governed target;


    
    public TeamListFX(Postgres db, Governed target) {
        
        this.db = db;
        this.filterfield = new TextField();
        this.teamsview = new ListView<TeamSimpleFX>();
        this.teams = new Teams(db);
        this.teamsfx = new MappedList<TeamSimpleFX, Team>(this.teams.getTeams(),(E)->{return new TeamSimpleFX(E);});
        this.filteredteams = new FilteredList<TeamSimpleFX>(teamsfx , p -> {return true;});
       
        this.target = target;
        this.build();
    }

    public TeamListFX(Postgres db){
        this(db,null);
    }
    
    private void build() {
        //wrap the filtered teams list into a sorted list
        SortedList<TeamSimpleFX> slt = new SortedList<TeamSimpleFX>(this.filteredteams);
        slt.setComparator((l, r) -> l.getTeam().getTeamname().getValueSafe().compareTo(r.getTeam().getTeamname().getValueSafe()));
        //if the filterfiled is changeing adjust the filter predicate
        this.filterfield.textProperty().addListener((observable, ov, nv) -> {
            this.filteredteams.setPredicate(t -> {
                return nv == null || nv.isEmpty() || t.getTeam().getTeamname().getValueSafe().toLowerCase().contains(nv.toLowerCase());
            });
        });

        this.teamsview.setCellFactory(new Callback<ListView<TeamSimpleFX>, ListCell<TeamSimpleFX>>() {

            @Override
            public ListCell<TeamSimpleFX> call(ListView<TeamSimpleFX> param) {
                ListCell<TeamSimpleFX> cell = new ListCell<TeamSimpleFX>() {

                    @Override
                    protected void updateItem(TeamSimpleFX item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setGraphic(item);
                            //setText(item.getTeam().getTeamname().getValue());
                            DragObjectDecorator.decorate(this, new DragTeamMediator(item), TransferMode.MOVE);
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
        this.teamsview.setItems(slt);
        VBox vb = new VBox();
        vb.getChildren().addAll(this.filterfield, this.teamsview);
        this.getChildren().add(vb);


    }
    
    
    
    public void loadTeams() {
        this.teams.load();
        
    }

    public Teams getTeams(){
        return this.teams;
    }
    
    public ObservableList<TeamSimpleFX> getTeamFX(){
        return this.teamsfx;
    }


}
