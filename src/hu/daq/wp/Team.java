/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp;

import client.Postgres;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Callback;

/**
 *
 * @author DAQ
 */
public class Team extends Entity {

    public SimpleStringProperty teamname = new SimpleStringProperty();
    public SimpleBooleanProperty visible;

    public Team() {

        this.id.addListener(changelistener);
        this.teamname.addListener(changelistener);


    }

    public static Callback<Team, Observable[]> extractor() {
        return (Team t) -> new Observable[]{t.getTeamname()};
    }

    public void reset() {
        this.id.set(0);
        this.teamname.set("");
        this.changed.set(false);
    }

    public SimpleStringProperty getTeamname() {
        return teamname;
    }

    @Override
    public String toString() {
        return this.teamname.getValueSafe();
    }

    public SimpleBooleanProperty visibleProperty() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible.set(visible);
    }

    @Override
    public String toInternalString() {
        return "Team::" + this.id.getValue().toString();
    }

}
