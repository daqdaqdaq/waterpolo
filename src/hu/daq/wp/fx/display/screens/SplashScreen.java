/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.screens;

import hu.daq.wp.fx.display.control.ControlledScreen;
import client.Postgres;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author DAQ
 */
public class SplashScreen  extends StackPane implements ControlledScreen{
    Postgres db;
    
    public SplashScreen(Postgres db){
        this.db = db;
        this.build();
    }

    private void build(){
        this.setBackground(
                new Background(
                        new BackgroundImage(
                                new Image(SplashScreen.class.getResourceAsStream("dfve_background.jpg"),1024,768,true,true)
                                        ,BackgroundRepeat.NO_REPEAT
                                        ,BackgroundRepeat.NO_REPEAT
                                        ,BackgroundPosition.CENTER
                                        ,BackgroundSize.DEFAULT)));
        Label t = new Label("Splash Screen for the win!");
        t.setFont(new Font(50));
        this.getChildren().add(t);
    
    }
    
}
