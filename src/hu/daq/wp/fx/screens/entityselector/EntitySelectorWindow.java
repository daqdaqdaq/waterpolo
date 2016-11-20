/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.screens.entityselector;

import hu.daq.wp.fx.screens.teamselector.*;
import client.Postgres;
import hu.daq.wp.fx.EntityListFX;
import hu.daq.wp.fx.TeamListFX;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author DAQ
 */
public class EntitySelectorWindow extends Stage {

    private Postgres db;
    private EntityListFX elf;

    public EntitySelectorWindow(Postgres db) {
        this.db = db;
        this.elf = new EntityListFX(this.db);
        this.build();
        
    }

    private void build() {
        Scene scene = new Scene(elf, 400, 600);
        this.setScene(scene);
        this.setAlwaysOnTop(true);
        //primaryStage.setFullScreen(true);
    }

    public void loadEntities() {
        elf.loadEntities();
        
        //tlf.getTeamFX().stream().forEach((E) -> {
        //    System.out.println("Decorating...");
        //    DragObjectDecorator.decorate(E, new DragTeamMediator(E), TransferMode.LINK);
        //});
    }
    
    public void setSelectedTeam(Integer teamid){
        this.elf.setSelectedTeam(teamid);
    }
}
