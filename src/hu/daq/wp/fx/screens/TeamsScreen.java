/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.screens;

import client.Postgres;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.wp.Team;
import hu.daq.wp.fx.AdvancedTeamFX;
import hu.daq.wp.fx.Instructable;
import hu.daq.wp.fx.TeamFX;
import hu.daq.wp.fx.commonbuttons.ResetButton;
import hu.daq.wp.fx.screens.entityselector.EntitySelectorWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 *
 * @author DAQ
 */
public class TeamsScreen extends BorderPane implements SubScreen, Instructable{
    
    private MainPageCommon parent;
    private FilteredList<Team> filteredteams;
    private ObservableList rawteams;
    private final TextField filterfield;
    private final ListView<Team> teamsview;
    private final AdvancedTeamFX tf;
    private final Boolean adminonly;
    private final ResetButton reloadbutton;
    
    
    public TeamsScreen() {
        this.adminonly = false;
        this.filterfield = new TextField();
        this.teamsview = new ListView<Team>();
        this.rawteams = FXCollections.observableArrayList();
        
        this.filteredteams = new FilteredList<Team>(this.rawteams, p -> true);
        this.tf = new AdvancedTeamFX();
        this.tf.setToNotify(this);
        this.reloadbutton = new ResetButton();
        this.build();
    }
//FXCollections.observableArrayList(ServiceHandler.getInstance().getDbService().getTeams())
    private void build() {
        this.reloadbutton.setOnAction((E)->this.loadTeams());
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
                ListCell<Team> cell = new ListCell<Team>()
                {

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
                            tf.load(cell.getItem().getID());
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
        HBox hb = new HBox(2);
        hb.getChildren().addAll(this.filterfield, this.reloadbutton);
        vb.getChildren().addAll(hb, this.teamsview);
        this.setLeft(vb);
        this.setCenter(this.tf);

    }

    public void loadTeams() {
        this.rawteams.setAll(ServiceHandler.getInstance().getDbService().getTeams());
        //this.filteredteams = new FilteredList<Team>(FXCollections.observableArrayList(ServiceHandler.getInstance().getDbService().getTeams()), p -> true);
    }

    @Override
    public SubScreen addContainer(MainPageCommon nd) {
        this.parent = nd;
        return this;
    }

    @Override
    public MainPageCommon getContainer() {
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

    @Override
    public void execute() {
        this.loadTeams();
    }
}
