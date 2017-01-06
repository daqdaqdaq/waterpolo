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
import hu.daq.wp.Coach;
import hu.daq.wp.Entity;
import hu.daq.wp.Player;
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
import javafx.scene.input.DataFormat;
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
public class AdvancedTeamFX extends VBox implements ObjectReceiver {

    Team team;
    TextField name_field;
    GlyphButton save_button;
    GlyphButton add_player_button;
    GlyphButton add_team_button;
    GlyphButton openentitiesbutton;
    ObservableList<PlayerFX> passive_players;
    ListView<PlayerFX> passive_playerlist;
    VBox roster;
    Instructable tonotify;
    CoachPosition coach;

    private EntitySelectorWindow esw;
    
    public AdvancedTeamFX() {
        this(new Team());
    }
    
    public AdvancedTeamFX(int team_id) {
        this(ServiceHandler.getInstance().getDbService().getTeam(team_id));
        //this.load(team_id);
    }

    public AdvancedTeamFX(Team team) {

        //this.player = new Player(db);
        this.setMaxWidth(600);
        this.setMinWidth(600);
        this.name_field = new TextField();
        this.setEntity(team);
        this.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        this.setPadding(new Insets(3));
        //Initializing the controls
        this.coach = new CoachPosition(this);
        this.passive_players = FXCollections.observableList(new ArrayList<PlayerFX>());
        this.roster = new VBox(2);
        this.passive_playerlist = new ListView<PlayerFX>();
        DropObjectDecorator.decorate(this.passive_playerlist, this, DataFormat.PLAIN_TEXT, TransferMode.MOVE);
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


        this.save_button = new SaveButton();
        this.add_player_button = new AddPlayerButton();
        this.add_team_button = new AddTeamButton();
        this.openentitiesbutton = new AddPlayerButton();

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

        this.build();
        this.buildRoster();
    }

    public void setEntity(Team entity){
        this.team = entity;
        this.name_field.textProperty().bindBidirectional(this.team.getTeamname());
        
    }
    
    public void setToNotify(Instructable tonotify) {
        this.tonotify = tonotify;
    }

    //Build the layot and make the bindings
    private void build() {
        HBox teamholder = new HBox();
        teamholder.setPadding(new Insets(10, 10, 10, 10));
        teamholder.setSpacing(10);
        teamholder.getChildren().addAll(this.name_field, this.add_team_button, this.save_button, this.openentitiesbutton, this.coach);
        HBox selectorholder = new HBox(10);
        selectorholder.getChildren().addAll(this.passive_playerlist, this.roster);
        this.getChildren().addAll(teamholder, selectorholder, this.add_player_button);
        this.add_player_button.setOnAction((ActionEvent e) -> {
            this.addPlayer();
        });

        this.save_button.setOnAction((ActionEvent e) -> {
            this.save();
            this.tonotify.execute();
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

    private void clearRoster() {
        this.roster.getChildren().stream().forEach(E -> {
            ((PlayerRosterPosition) E).clearPlayer();
        });
    }

    public final void load(Integer pk) {
        this.setEntity(ServiceHandler.getInstance().getDbService().getTeam(pk));
        this.esw.setSelectedTeam(pk);
        this.loadPlayers();
        this.loadCoach();

    }

    public final boolean save() {
        this.savePlayers();
        return ServiceHandler.getInstance().getDbService().save(this.team);
    }

    public void newTeam() {
        this.setEntity(new Team());
        this.team.teamname.set("Új csapat");
        this.passive_players.clear();
        this.clearRoster();
        this.coach.clearCoach();

    }

    public SimpleBooleanProperty getChanged() {
        return this.team.getChanged();
    }

    public boolean isChanged() {
        return this.team.getChanged().get();
    }

    private void loadCoach() {
        this.coach.clearCoach();
        Coach c = ServiceHandler.getInstance().getDbService().getCoachOfTeam(this.team.getID());
        if (c != null) {
            this.coach.setCoach(new CoachFX(c));
        }
    }

    /*
     get the players sort them out for active and passive branches and put 
     them to the right observable list
     */
    private void loadPlayers() {
        this.passive_players.clear();
        this.clearRoster();
        //Map which holds the active and passive players' list
        Map<Boolean, List<PlayerFX>> m = ServiceHandler.getInstance().getDbService().getPlayersOfTeam(this.team.getID())
                .stream().map(PlayerFX::new)
                .collect(Collectors.partitioningBy(x -> x.isActive()));
        this.passive_players.addAll(m.get(false));
        m.get(true).stream().forEach(E -> {
            //System.out.println("positioning "+E.toString());
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
        PlayerFX pf = new PlayerFX(new Player());
        pf.player.getTeam_id().setValue(this.team.getID());
        pf.setCapnum(0);
        pf.inactivate();
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
        EntityFX dsource = ((ListCell<EntityFX>) source).getItem();

        System.out.println("The source is:" + dsource.toString());
        if (dsource.getType().equals("Player")) {
            System.out.println("The type is:" + dsource.getType() + ": " + ((PlayerFX) dsource).toString());

            ((PlayerFX) dsource).getPlayer().setTeamid(this.getTeamID());
            ((PlayerFX) dsource).getPlayer().setCapnum(0);
            ((PlayerFX) dsource).inactivate();
            ((PlayerFX) dsource).save();

            PlayerFX p = new PlayerFX(((PlayerFX) dsource).getPlayer());
            p.getPlayer().setTeamid(this.getTeamID());
            p.getPlayer().setCapnum(0);
            p.inactivate();
            p.save();

            //((ListCell<EntityFX>) source).getListView().getItems().remove(dsource);
            this.passive_players.add(p);
        }
    }

    void handleCoachRemove(CoachFX coach) {
        coach.getCoach().setTeamid(0);
        coach.save();
    }

}
