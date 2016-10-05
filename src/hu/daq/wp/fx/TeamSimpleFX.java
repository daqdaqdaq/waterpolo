/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

import client.Postgres;
import hu.daq.wp.Team;

import java.util.HashMap;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 *
 * @author DAQ
 */
public class TeamSimpleFX extends Label {

    Team team;
    SimpleBooleanProperty dragable = new SimpleBooleanProperty(true);
    SimpleBooleanProperty inuse = new SimpleBooleanProperty(false);
    GlyphFont fontawesome;
    Glyph attention;

    public TeamSimpleFX(Postgres db) {
        this(new Team(db));
    }

    public TeamSimpleFX(Postgres db, int team_id) {
        this(db);
        this.load(team_id);
    }

    public TeamSimpleFX(Team team) {
        this.team = team;
        this.fontawesome = GlyphFontRegistry.font("FontAwesome");
        this.attention = this.fontawesome.create(FontAwesome.Glyph.EXCLAMATION_TRIANGLE);
        this.attention.setTextFill(Color.YELLOW);
        this.attention.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        this.setContentDisplay(ContentDisplay.RIGHT);
        //this.player = new Player(db);

        this.build();
        this.checkState();
    }

    //Build the layot and make the bindings
    private void build() {
        this.disableProperty().bind(this.dragable.not());
        this.team.getCapnumok().addListener((observable, ov, nv) -> {
            this.checkState();
        });

        this.team.getPlayernumok().addListener((observable, ov, nv) -> {
            this.checkState();
        });

        this.inuse.addListener((ObservableValue<? extends Boolean> observable, Boolean ov, Boolean nv) -> {
            this.checkState();
        });

        this.textProperty().set(this.team.toString());

    }

    private void checkState() {
        if (this.team.getCapnumok().getValue() && this.team.getPlayernumok().getValue() && !this.inuse.getValue()) {
            this.setGraphic(null);
            this.setTooltip(null);
            this.dragable.set(true);


        } else {
            //this.dragable.set(false);            
            this.dragable.set(true);
            String tooltipstr = "";
            if (!this.team.getCapnumok().getValue()) {
                tooltipstr += "Sapka szám ütközés\n";
            }

            if (!this.team.getPlayernumok().getValue()) {
                tooltipstr += "Nem megfelelő számú játékos\n";
            }

            if (this.inuse.getValue()) {
                tooltipstr += "Már használatban\n";
            }
            Tooltip tt = new Tooltip(tooltipstr);
            this.setTooltip(tt);
            this.setGraphic(this.attention);
        }
    }

    public final boolean load(Integer pk) {
        if (this.team.load(pk)) {
            this.checkState();
            return true;
        }
        return false;
    }

    public final boolean load(HashMap<String, String> data) {
        if (this.team.load(data)) {
            this.checkState();
            return true;
        }
        return false;
    }

    public Team getTeam() {
        return this.team;
    }

    public Integer getID() {
        return this.team.getID();
    }

    public void setInuse(Boolean use) {
        this.inuse.set(use);
    }

    public boolean isDragable() {
        return this.dragable.get();
    }
}
