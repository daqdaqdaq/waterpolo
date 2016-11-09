/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

import client.Postgres;
import hu.daq.draganddrop.DragObjectDecorator;
import hu.daq.wp.Team;
import hu.daq.wp.fx.commonbuttons.AddPlayerButton;
import hu.daq.wp.fx.commonbuttons.AddTeamButton;
import hu.daq.wp.fx.commonbuttons.GlyphButton;
import hu.daq.wp.fx.commonbuttons.SaveButton;
import hu.daq.wp.fx.screens.entityselector.DragEntityMediator;
import hu.daq.wp.fx.screens.entityselector.EntitySelectorWindow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.controlsfx.control.ListSelectionView;

/**
 *
 * @author DAQ
 */
public class AdvancedTeamFX extends VBox {

    Team team;
    TextField name_field;
    GlyphButton save_button;
    GlyphButton add_player_button;
    GlyphButton add_team_button;
    GlyphButton openentitiesbutton;
    ObservableList<PlayerFX> passive_players;
    ListView<PlayerFX> passive_playerlist;
    VBox roster;

    private EntitySelectorWindow esw;
    Postgres db;

    public AdvancedTeamFX(Postgres db) {
        this(new Team(db));
    }

    public AdvancedTeamFX(Postgres db, int team_id) {
        this(db);
        this.load(team_id);
    }

    public AdvancedTeamFX(Team team) {
        this.team = team;
        this.db = this.team.getDb();
        //this.player = new Player(db);
        this.setMaxWidth(600);
        this.setMinWidth(600);

        this.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        this.setPadding(new Insets(3));
        //Initializing the controls
        this.passive_players = FXCollections.observableList(new ArrayList<PlayerFX>());     
        this.roster = new VBox(2);
        this.passive_playerlist = new ListView<PlayerFX>();
        this.passive_playerlist.setItems(passive_players);
        this.passive_playerlist.setCellFactory(new Callback<ListView<PlayerFX>, ListCell<PlayerFX>>() {

            @Override
            public ListCell<PlayerFX> call(ListView<PlayerFX> param) {
                ListCell<PlayerFX> cell = new ListCell<PlayerFX>() {

                    @Override
                    protected void updateItem(PlayerFX item, boolean empty) {
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
                return cell;
            }
        });

        this.name_field = new TextField();
        this.save_button = new SaveButton();
        this.add_player_button = new AddPlayerButton();
        this.add_team_button = new AddTeamButton();
        this.openentitiesbutton = new AddPlayerButton();


        this.esw = new EntitySelectorWindow(this.db);
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

        this.build();
        this.buildRoster();
    }

    //Build the layot and make the bindings
    private void build() {
        HBox teamholder = new HBox();
        teamholder.setPadding(new Insets(10, 10, 10, 10));
        teamholder.setSpacing(10);
        teamholder.getChildren().addAll(this.name_field, this.add_team_button, this.save_button, this.openentitiesbutton);
        HBox selectorholder = new HBox(10);
        selectorholder.getChildren().addAll(this.passive_playerlist,this.roster);
        this.getChildren().addAll(teamholder, selectorholder);
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
  
    }

    private void buildRoster() {
        for (int i = 1; i <= 14; i++) {
            this.roster.getChildren().add(new PlayerRosterPosition(i, this));
        }

    }
    private void clearRoster(){
        this.roster.getChildren().stream().forEach(E -> {
            ((PlayerRosterPosition)E).clearPlayer();
        });
    }
            
    public final boolean load(Integer pk) {
        if (this.team.load(pk)) {
            this.esw.setSelectedTeam(pk);
            this.loadPlayers();
            return true;
        }
        return false;
    }

    public final boolean load(HashMap<String, String> data) {
        if (this.team.load(data)) {
            this.esw.setSelectedTeam(this.team.getID());
            this.loadPlayers();
            return true;
        }
        return false;
    }

    public final boolean save() {
        this.savePlayers();
        return this.team.save();
    }

    public void newTeam() {
        this.team = new Team(this.db);
        this.name_field.setText("Új csapat");

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
        this.passive_players.clear();
        this.clearRoster();
        //Map which holds the active and passive players' list
        Map<Boolean, List<PlayerFX>> m = this.team.getPlayers()
                .stream().map(PlayerFX::new)
                .collect(Collectors.partitioningBy(x -> x.isActive()));
        this.passive_players.addAll(m.get(false));
        m.get(true).stream().forEach(E -> {
            System.out.println("positioning "+E.toString());
            PlayerRosterPosition pos = this.getPlayerPosition(E.getCapnum());
            if (pos != null) {
                pos.setPlayer(E);
            }
        });
    }

    private PlayerRosterPosition getPlayerPosition(Integer capnum) {
        Optional<Node> pos = this.roster.getChildren().stream().filter(E -> ((PlayerRosterPosition) E).getCapnum().equals(capnum)).findFirst();
        if (pos.isPresent()) {
            return (PlayerRosterPosition) pos.get();
        } else {
            return null;
        }
    }

    public PlayerFX getPlayer(Integer playerid) {
        Optional<PlayerFX> player = this.passive_players.stream().filter(E -> E.player.getID().equals(playerid)).findFirst();
        return player.orElse(null);
    }

    /*
     Save the players both in the active and passive list if they changed
     */
    private void savePlayers() {
        this.passive_players.stream()
                .filter(E -> E.isChanged())
                .forEach(e -> {
                    e.save();
                });
    }

    private void addPlayer() {
        PlayerFX pf = new PlayerFX(this.team.getDb());
        pf.player.getTeam_id().setValue(this.team.getTeam_id().getValue());
        pf.editOn();
        pf.name_field.requestFocus();
        this.passive_players.add(pf);

    }

    public Integer getTeamID() {
        return this.team.getID();
    }

    public void handlePlayerRemove(PlayerFX player) {
        player.getPlayer().setCapnum(0);
        player.inactivate();
        player.save();
        this.passive_players.add(player);
    }
}
