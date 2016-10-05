/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.infopopup;

import hu.daq.watch.fx.RingDisplay;
import hu.daq.watch.fx.TimeDisplay;
import hu.daq.watch.utility.WatchFactory;
import hu.daq.wp.Team;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author DAQ
 */
public class TimeOutWindow extends PopupWindow{
    StackPane background;
    Label teamname;
    DummyInstructable di;    

    
    public TimeOutWindow(int duration) {
        super(duration);
        this.background = new StackPane();
        Color bgcolor = new Color(0.2,0.2,0.2,0.9);
        this.background.setBackground(new Background(new BackgroundFill(bgcolor,new CornerRadii(3), new Insets(5))));
        this.background.setBorder(new Border(new BorderStroke(new Color(0.4,0.4,0.4,0.7), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(5)))); 
        this.teamname = new Label();        
        this.build();
        Scene scene = new Scene(this.background, 800, 600);
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
        this.di = new DummyInstructable();        
    }
    
    private void build(){
        VBox vb = new VBox(20);
        vb.setAlignment(Pos.CENTER);
        Label header = new Label("Időkérés");
        header.setFont(new Font(30));
        header.setAlignment(Pos.CENTER);
        this.teamname.setFont(new Font(30));
        this.teamname.setWrapText(true);
        this.teamname.setAlignment(Pos.CENTER);
        RingDisplay td = WatchFactory.getRingDisplay(this.cw, 300, Color.YELLOW);
        //td.setFont(new Font(30));
        VBox.setVgrow(td, Priority.ALWAYS);
        vb.getChildren().addAll(header,this.teamname, td);
        this.background.getChildren().add(vb);
        
    
    }

    public void loadTeam(Team team){
        this.teamname.setText(team.getTeamname().getValue());
    
    }
}
