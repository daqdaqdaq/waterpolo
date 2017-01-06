/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.screens.teamselector;

import client.Postgres;
import hu.daq.wp.fx.TeamListFX;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author DAQ
 */
public class TeamSelectorWindow extends Stage {

    private Postgres db;
    private TeamListFX tlf;

    public TeamSelectorWindow(Postgres db) {
        this.db = db;
        this.tlf = new TeamListFX();
        this.build();
        
    }

    private void build() {
        Scene scene = new Scene(tlf, 400, 600);
        this.setScene(scene);
        this.setAlwaysOnTop(true);
        //primaryStage.setFullScreen(true);
    }

    public void loadTeams() {
        tlf.loadTeams();
        //tlf.getTeamFX().stream().forEach((E) -> {
        //    System.out.println("Decorating...");
        //    DragObjectDecorator.decorate(E, new DragTeamMediator(E), TransferMode.LINK);
        //});
    }
}
