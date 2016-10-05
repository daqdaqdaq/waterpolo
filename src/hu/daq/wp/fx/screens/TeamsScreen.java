/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.screens;

import client.Postgres;
import hu.daq.wp.Team;
import hu.daq.wp.Teams;
import hu.daq.wp.fx.TeamFX;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 *
 * @author DAQ
 */
public class TeamsScreen extends BorderPane implements SubScreen{
    
    private MainPage parent;
    private final Postgres db;
    private final Teams teams;
    private final FilteredList<Team> filteredteams;
    private final TextField filterfield;
    private final ListView<Team> teamsview;
    private final TeamFX tf;
    private final Boolean adminonly;
    
    public TeamsScreen(Postgres db) {
        this.adminonly = false;
        this.db = db;
        this.filterfield = new TextField();
        this.teamsview = new ListView<Team>();
        this.teams = new Teams(db);
        this.filteredteams = new FilteredList<Team>(this.teams.getTeams(), p -> true);
        this.tf = new TeamFX(db);
        
        this.build();
    }

    private void build() {
        //wrap the filtered teams list into a sorted list
        SortedList<Team> slt = new SortedList<Team>(this.filteredteams);
        slt.setComparator((l, r) -> l.getTeamname().getValueSafe().compareTo(r.getTeamname().getValueSafe()));
        //if the filterfiled is changeing adjust the filter predicate
        this.filterfield.textProperty().addListener((observable, ov, nv) -> {
            this.filteredteams.setPredicate(t -> {
                return nv == null || nv.isEmpty() || t.getTeamname().getValueSafe().toLowerCase().contains(nv.toLowerCase());
            });
        });

        this.teamsview.setCellFactory(new Callback<ListView<Team>, ListCell<Team>>() {

            @Override
            public ListCell<Team> call(ListView<Team> param) {
                ListCell<Team> cell = new ListCell<Team>() {

                    @Override
                    protected void updateItem(Team item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getTeamname().getValue());
                        } else {
                            setText("");   
                        }
                    }

                };

                cell.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent evt) -> {
                    final int index = cell.getIndex();
                    if (evt.getButton().equals(MouseButton.PRIMARY)) {
                        if (teamsview.getSelectionModel().getSelectedIndex() == cell.getIndex()) {
                            tf.load(cell.getItem().getTeam_id().getValue());
                            evt.consume();
                        }
                    } else {
                        evt.consume();
                        //Right button code here
                    }
                });

                return cell;
            }

        });
        this.teamsview.setItems(slt);
        VBox vb = new VBox();
        vb.getChildren().addAll(this.filterfield, this.teamsview);
        this.setLeft(vb);
        this.setCenter(this.tf);

    }

    public void loadTeams() {
        this.teams.load();
    }

    @Override
    public SubScreen addContainer(MainPage nd) {
        this.parent = nd;
        return this;
    }

    @Override
    public MainPage getContainer() {
        return this.parent;
    }

    @Override
    public void initScreen() {
        this.loadTeams();
    }

    @Override
    public Boolean isAdminOnly() {
        return this.adminonly;
    }
}
