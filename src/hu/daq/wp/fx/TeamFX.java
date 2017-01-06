/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

import client.Postgres;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.wp.Player;
import hu.daq.wp.Team;
import hu.daq.wp.fx.commonbuttons.AddPlayerButton;
import hu.daq.wp.fx.commonbuttons.AddTeamButton;
import hu.daq.wp.fx.commonbuttons.GlyphButton;
import hu.daq.wp.fx.commonbuttons.SaveButton;
import hu.daq.wp.fx.screens.entityselector.EntitySelectorWindow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.controlsfx.control.ListSelectionView;

/**
 *
 * @author DAQ
 */
public class TeamFX extends VBox {

    Team team;
    TextField name_field;
    GlyphButton save_button;
    GlyphButton add_player_button;
    GlyphButton add_team_button;
    GlyphButton openentitiesbutton;
    ObservableList<PlayerFX> passive_players;
    ObservableList<PlayerFX> active_players;
    ListSelectionView<PlayerFX> selector;
    private EntitySelectorWindow esw;
    Postgres db;

    static int teamsize = 1;

    public TeamFX(int team_id) {
        this.load(team_id);
    }

    public TeamFX(Team team) {
        this.team = team;
        //this.player = new Player(db);
        this.setMaxWidth(600);
        this.setMinWidth(600);

        this.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        this.setPadding(new Insets(3));
        //Initializing the controls
        this.name_field = new TextField();
        this.save_button = new SaveButton();
        this.add_player_button = new AddPlayerButton();
        this.add_team_button = new AddTeamButton();
        this.openentitiesbutton = new AddPlayerButton();
        this.passive_players = FXCollections.observableList(new ArrayList<PlayerFX>());
        this.active_players = FXCollections.observableList(new ArrayList<PlayerFX>());
        this.esw = new EntitySelectorWindow();
        //Setup the listeners on the active and passive lists to set the incomming players to the aproppriate state
        this.passive_players.addListener((ListChangeListener.Change<? extends PlayerFX> c) -> {
            while (c.next()) {
                c.getAddedSubList().stream().forEach(e -> {
                    //System.out.println(e.player.toString());
                    e.getActive().setValue(Boolean.FALSE);
                    e.save();
                    //System.out.println(e.player.toString());
                });
            }
        });

        this.active_players.addListener((ListChangeListener.Change<? extends PlayerFX> c) -> {
            while (c.next()) {
                //if the last added players exceeds the max teamsize, remove them and add them to the passive players
                if (c.getList().size() > 14) {
                    List<? extends PlayerFX> asl = c.getAddedSubList();
                    this.active_players.removeAll(asl);
                    this.passive_players.addAll(asl);
                } else { // set the player's state to active and save it
                    c.getAddedSubList().stream().forEach(e -> {
                        e.getActive().set(true);
                        e.save();
                    });
                    this.markDupes();
                }
            }
        });

        this.selector = new ListSelectionView<PlayerFX>();
        this.selector.setSourceHeader(new Label("Elérhető"));
        this.selector.setTargetHeader(new Label("Csapatban"));
        this.selector.setSourceFooter(this.add_player_button);
        this.selector.setSourceItems(this.passive_players);
        this.selector.setTargetItems(this.active_players);
        VBox.setVgrow(this.selector, Priority.ALWAYS);
        this.build();
    }

    //Build the layot and make the bindings
    private void build() {
        HBox teamholder = new HBox();
        teamholder.setPadding(new Insets(10, 10, 10, 10));
        teamholder.setSpacing(10);
        teamholder.getChildren().addAll(this.name_field, this.add_team_button, this.save_button, this.openentitiesbutton);
        this.getChildren().addAll(teamholder, this.selector);
        this.name_field.textProperty().bindBidirectional(this.team.getTeamname());
        this.add_player_button.setOnAction((ActionEvent e) -> {
            this.addPlayer();
        });

        this.save_button.setOnAction((ActionEvent e) -> {
            this.save();
        });

        this.add_team_button.setOnAction((ActionEvent e) -> {
            this.newTeam();
        });
        this.openentitiesbutton.disableProperty().bind(this.esw.showingProperty());
        this.openentitiesbutton.setOnAction((ActionEvent e) -> {
            this.esw.loadEntities();
            this.esw.show();
        });
        this.save_button.disableProperty().bind(Bindings.not(this.getChanged()));
        /*     
         this.picture.setFitWidth(50);
         this.picture.setFitHeight(50);
         this.capnum_field.setPrefColumnCount(1);
         this.capnum_field.textProperty().bindBidirectional(this.player.getCapnum(), new NumberStringConverter());
         this.capnum_label.textProperty().bindBidirectional(this.player.getCapnum(), new NumberStringConverter());

         this.name_field.setPrefColumnCount(60);
         this.name_field.textProperty().bindBidirectional(this.player.getName());

         this.edit_button.setOnAction((ActionEvent event) -> {
         editOn();
         });

         this.save_button.setOnAction((ActionEvent event) -> {
         if (save()) {
         editOff();
         }
         });

         this.basegrid.setHgap(5);
         this.basegrid.setVgap(5);
         this.basegrid.add(this.picture, 0, 0, 1, 2);
         this.basegrid.add(this.name_label, 1, 0, 2, 1);
         this.basegrid.add(this.capnum_label, 1, 1);
         this.basegrid.add(this.edit_button, 3, 1);
         this.getChildren().add(this.basegrid);
         */
    }

    public final void load(Integer pk) {
        this.team = ServiceHandler.getInstance().getDbService().getTeam(pk);
        this.esw.setSelectedTeam(pk);
        this.loadPlayers();
    }

    public final boolean save() {
        this.savePlayers();
        return ServiceHandler.getInstance().getDbService().save(this.team);
    }

    public void newTeam() {
        this.team = new Team();
        this.team.getTeamname().set("Új csapat");
        //this.name_field.setText("Új csapat");

    }

    public SimpleBooleanProperty getChanged() {
        return this.team.getChanged();
    }

    public boolean isChanged() {
        return this.team.getChanged().get();
    }

    /*
     get the players sort them out for active and passive branches and put 
     them to the right observable list
     */
    private void loadPlayers() {
        this.active_players.clear();
        this.passive_players.clear();
        //Map which holds the active and passive players' list
        Map<Boolean, List<PlayerFX>> m = ServiceHandler.getInstance().getDbService().getPlayersOfTeam(this.team.getID())
                .stream().map(PlayerFX::new)
                .collect(Collectors.partitioningBy(x -> x.isActive()));
        this.active_players.addAll(m.get(true));
        this.passive_players.addAll(m.get(false));
    }

    /*
     Save the players both in the active and passive list if they changed
     */
    private void savePlayers() {
        this.active_players.stream()
                .filter(E -> E.isChanged())
                .forEach(e -> {
                    e.save();
                });
        this.passive_players.stream()
                .filter(E -> E.isChanged())
                .forEach(e -> {
                    e.save();
                });
    }

    private void addPlayer() {
        PlayerFX pf = new PlayerFX(new Player());
        pf.player.getTeam_id().setValue(this.team.getID());
        pf.editOn();
        pf.name_field.requestFocus();
        this.passive_players.add(pf);

    }

    private List<Integer> getDuplicatedCapnums() {
        List<Integer> dupes = new ArrayList<Integer>();
        Set<Integer> filterset = new HashSet();

        for (PlayerFX p : this.active_players) {
            if (!filterset.add(p.getCapnum())) {
                dupes.add(p.getCapnum());
            }
        }
        return dupes;
    }

    /*
     Get the duplicated capnums, process the players in the active list and 
     mark the duplicated ones
     */
    private void markDupes() {

        List<Integer> dupes = this.getDuplicatedCapnums();
        this.active_players.parallelStream().forEach(e -> {
            if (dupes.contains(e.getCapnum())) {
                e.getDuplicated().set(true);
            } else {
                e.getDuplicated().set(false);
            }
        }
        );
    }
}
