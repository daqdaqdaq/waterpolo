/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.animation;


import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 *
 * @author DAQ
 */
public class Animation {
        
    public static Transition addGoalAnimation(Node goal){
        goal.setOpacity(0);

        DropShadow ds = new DropShadow();
        ds.setOffsetX(0);
        ds.setOffsetY(0.2);
        ds.setBlurType(BlurType.GAUSSIAN);
        ds.setColor(Color.RED);
        ds.setSpread(0.1);
        ds.setRadius(0.5);
        goal.setEffect(ds);        
        FadeTransition fadein = new FadeTransition(Duration.millis(1000), goal);
        fadein.setFromValue(0);
        fadein.setToValue(1);
        fadein.setCycleCount(1);
        TranslateTransition movedown = new TranslateTransition(Duration.millis(500),goal);
        movedown.setToY(400);
        movedown.setAutoReverse(false);
        movedown.setCycleCount(1);
        TranslateTransition moveup = new TranslateTransition(Duration.millis(500),goal);
        moveup.setToY(200);
        moveup.setAutoReverse(false);
        moveup.setCycleCount(1); 
        SequentialTransition move = new SequentialTransition();
        move.getChildren().addAll(movedown,moveup);
        ScaleTransition scale = new ScaleTransition(Duration.millis(1000), goal);
        scale.setToX(30);
        scale.setToY(30);
        scale.setCycleCount(1);
        
        ParallelTransition parallel = new ParallelTransition();
        parallel.getChildren().addAll(move,scale,fadein);
        parallel.setCycleCount(1);
  
        return parallel;
    }
}
