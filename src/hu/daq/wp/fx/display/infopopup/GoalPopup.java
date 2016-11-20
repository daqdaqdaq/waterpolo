/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.infopopup;

import client.Postgres;
import hu.daq.wp.fx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author DAQ
 */
public class GoalPopup extends PersonInfo{
    StackPane animationoverlay;
    
    public GoalPopup(Postgres db){
        super(db);
        //this.setWidth(600);
        this.vb.setMaxWidth(this.getWidth()*0.7);
        this.animationoverlay = new StackPane();
        this.animationoverlay.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        this.background.getChildren().add(this.animationoverlay);
        
    }
    
    @Override
    public void showIt(){
        super.showIt();
        Label goal = new Label("Góóóól!");
        goal.setFont(new Font("Impact",5));
        goal.setOpacity(0);
        goal.setTextFill(Color.RED);
        goal.setEffect(new DropShadow());
        Transition anim = Animation.addGoalAnimation(goal);
        this.animationoverlay.getChildren().add(goal);
        anim.play();
    }
    
}
