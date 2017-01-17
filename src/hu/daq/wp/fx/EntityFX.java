/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx;

import hu.daq.wp.Entity;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Cell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author DAQ
 * @param <T>
 */
public abstract class EntityFX<T extends Entity> extends StackPane implements Instructable {

    SimpleBooleanProperty inuse = new SimpleBooleanProperty(false);
    protected SimpleBooleanProperty deleted = new SimpleBooleanProperty();

    public EntityFX() {
        this.deleted.set(true);
        this.deleted.addListener((ob, ov, nv) -> {
            if (nv.equals(Boolean.TRUE)) {
                this.removeMe();
            }
        });
    }

    public abstract String getID();

    public abstract Integer getIDInt();

    public abstract Boolean isDragable();

    public abstract String getName();

    public abstract Integer getTeamID();

    public abstract void editOn();

    public abstract String getType();

    protected abstract void onDelete();

    protected abstract void setEntity(T entity);

    public void setInuse(Boolean in) {
        this.inuse.set(in);
    }

    public SimpleBooleanProperty getDeleted() {
        return deleted;
    }

    @Override
    public void execute() {
        this.editOn();
    }

    protected void removeMe() {
        this.searchAndDestroy(this.getParent());
    }

    protected boolean searchAndDestroy(Parent n) {

        if (n instanceof EntityListFX) {
            ((EntityListFX) n).remove(this);
            return true;
        }
        if (n instanceof AdvancedTeamFX) {
            ((AdvancedTeamFX) n).removePlayer(this);
            return true;
        }
        if (n instanceof PlayerRosterPosition) {
            ((PlayerRosterPosition) n).clearPlayer();
            return true;
        }
        if (n instanceof CoachPosition) {
            ((CoachPosition) n).clearCoach();
            return true;
        }
        if (n instanceof RefereePosition) {
            ((RefereePosition) n).clearCoach();
            return true;
        }
        return this.searchAndDestroy(n.getParent());
    }
}
