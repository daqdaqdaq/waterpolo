/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.infopopup;

import hu.daq.wp.Player;
import hu.daq.wp.Team;
import hu.daq.wp.fx.image.PlayerPicture;
import hu.daq.wp.fx.texteffects.TextEffect;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author DAQ
 */
public class PlayerInfo extends PopupWindow {

    StackPane background;
    PlayerPicture pict;
    Text teamname;
    Text playername;
    DummyInstructable di;
    VBox vb;
        
    
    public PlayerInfo() {
        super();
        this.vb = new VBox();
        this.background = new StackPane();
        
        this.teamname = new Text();
        this.playername = new Text();
      
        this.pict = new PlayerPicture(this.di, new SimpleIntegerProperty(0));
        this.build();
        Scene scene = new Scene(background, 1024, 768);
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
        this.di = new DummyInstructable();
    }

    private void build() {
        this.vb.setAlignment(Pos.CENTER);
        this.teamname.setFont(new Font(30));
        this.teamname.setWrappingWidth(400);
        this.teamname.setFill(Color.WHITE);
        this.playername.setFill(Color.WHITE);
        this.teamname.setEffect(TextEffect.getNewerNeonEffect());
        this.playername.setEffect(TextEffect.getNewerNeonEffect());        
        this.teamname.setTextAlignment(TextAlignment.CENTER);
        this.playername.setFont(new Font(30));
        Color bgcolor = new Color(0.2,0.2,0.2,0.98);
        this.background.setBackground(new Background(new BackgroundFill(bgcolor,new CornerRadii(3), new Insets(5))));
        this.vb.getChildren().addAll(this.teamname,this.pict,this.playername);
        this.background.getChildren().add(vb);
        
    }
    
    public void loadPlayer(Player player){
        Team t = new Team(player.getDb());
        t.load(player.getTeam_id().get());
        this.vb.getChildren().remove(this.pict);        
        this.pict = new PlayerPicture(this.di, player.getPlayer_pic());
        this.pict.loadPic();
        this.vb.getChildren().add(1, this.pict);
        this.teamname.setText(t.getTeamname().get());
        this.playername.setText(player.getName().get());
    
    }
    
    public StackPane getBackground(){
        return this.background;
    }
    
    
}
