/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.player;

import client.Postgres;
import hu.daq.servicehandler.ServiceHandler;
import hu.daq.watch.TimeoutListener;
import hu.daq.wp.Player;
import hu.daq.wp.fx.display.penalties.CircleFactory;
import hu.daq.wp.fx.display.penalties.PenaltiesFX;

import java.util.HashMap;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 *
 * @author DAQ
 */
public abstract class PlayerDisplayFX extends StackPane implements Comparable, TimeoutListener {

    Player player;
    Label capnum_label;
    Label name_label;
    Label goals_label;
    PenaltiesFX penalties;
    PlayerFXDisplayOverlay pfxo;
    SimpleIntegerProperty goals = new SimpleIntegerProperty();
    ColumnConstraints capnumconst = new ColumnConstraints();
    ColumnConstraints nameconst = new ColumnConstraints();
    ColumnConstraints penaltiesconst = new ColumnConstraints();
    ColumnConstraints goalsconst = new ColumnConstraints();

    public PlayerDisplayFX(Postgres db) {
        this(new Player(db));
    }

    public PlayerDisplayFX(Postgres db, int player_id) {
        this(db);
        this.load(player_id);
    }

    public PlayerDisplayFX(Player player) {
        //this.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        this.setId("playerdisplay");
        this.player = player;
        this.setMaxWidth(980);
        this.setMaxHeight(35);
        this.setMinHeight(35);
        Font sizeing = new Font(33);
        this.capnum_label = new Label();
        this.capnum_label.setFont(sizeing);
        this.name_label = new Label();
        this.name_label.setFont(sizeing);
        this.goals_label = new Label();
        this.goals_label.setFont(sizeing);
        this.penalties = new PenaltiesFX(new CircleFactory(), 80);
        //this.penalties.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        this.pfxo = new PlayerFXDisplayOverlay(this, ServiceHandler.getInstance().getTimeEngine(), 20);
        this.penalties.setFillHeight(true);
        this.goals.set(0);
        //Size constraints
        capnumconst.setPercentWidth(10);
        nameconst.setPercentWidth(55);
        penaltiesconst.setPercentWidth(20);
        goalsconst.setPercentWidth(15);

        //this.build();
    }

    //Only the layout differs in the left and right version
    protected abstract void buildLayout();

    protected void build() {
        this.capnum_label.textProperty().bind(Bindings.createStringBinding(() -> {
            return this.player.getCapnum().getValue().toString() + ".";
        }, this.player.getCapnum()));
        this.name_label.textProperty().bind(this.player.getShortname());
        //this.name_label.setPrefWidth(150);
        this.goals_label.textProperty().bind(Bindings.createStringBinding(() -> {
            return this.goals.getValue().toString();
        }, this.goals));
        /*Make the binding to the penalties. If the player has max number of penalties the whole player gets 
         0.5 opacity
         */
        this.opacityProperty().bind(Bindings.createDoubleBinding(() -> {
            return penalties.getFinallyout().getValue() ? 0.5 : 1;
        }, this.penalties.getFinallyout()));
        this.buildLayout();
    }

    public int addPenalty() {
        //Add a penalty and get the number of penalties
        int p = this.penalties.addPenalty();
        //If this player isn't completely out due to the penalty then add the penalty countdown
        if (!this.penalties.getFinallyout().get()) {
            this.addOverlay();
        }
        return p;
    }

    public int removePenalty() {
        this.pfxo.jumpToEnd();
        return this.penalties.removePenalty();
    }

    public void endPenalty(){
        this.pfxo.jumpToEnd();
    }
            
    public final boolean load(Integer pk) {
        return this.player.load(pk);
    }

    public void addGoal() {
        this.goals.setValue(this.goals.getValue() + 1);
    }

    public void removeGoal() {
        if (this.goals.getValue()>0){
            this.goals.setValue(this.goals.getValue() - 1);
        }

    }

    public final boolean load(HashMap<String, String> data) {
        return this.player.load(data);

    }

    public Boolean isOut() {
        return this.penalties.getFinallyout().getValue();
    }

    public Integer getCapnum() {
        return this.player.getCapnum().getValue();
    }

    /*
     Implementing a comparing algorithm for later sorting 
     First players in the match come before players out because of penalties
     and secondly they capnum is the sorting criteria
     */
    @Override
    public int compareTo(Object o) {

        PlayerDisplayFX f = (PlayerDisplayFX) o;
        int first = 0; //assume they are equal
        if (f.isOut() && !this.isOut()) {
            first = -1; // this is in the other is out
        }
        if (this.isOut() && !f.isOut()) {
            first = 1; //this is out the other is in
        }
        //if they equal compare their capnums
        return first == 0 ? this.player.getCapnum().getValue().compareTo(f.player.getCapnum().getValue()) : first;
    }

    public SimpleIntegerProperty getGoals() {
        return this.goals;
    }

    public Integer getPlayerID() {
        return this.player.getPlayer_id().getValue();
    }
    
    public Player getPlayerModel(){
        return this.player;
    }
    
    protected void addOverlay() {

        //this.pfxo = new PlayerFXDisplayOverlay(this, ServiceHandler.getInstance().getTimeEngine(), 20);
        this.pfxo.reset();
        this.getChildren().add(this.pfxo);
        this.pfxo.startCountdown();
    }

    @Override
    public void timeout() {
        this.getChildren().remove(this.pfxo);
        //this.pfxo.reset();
        //System.out.println("Time is running out");

    }
    
    public int getPenaltyTime(){
        return this.pfxo.getTime();
    }
}
