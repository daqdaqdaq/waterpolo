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
        ds.setOffsetY(0.5);
        ds.setBlurType(BlurType.GAUSSIAN);
        ds.setColor(Color.RED);
        ds.setSpread(0.4);
        ds.setRadius(1);
        goal.setEffect(ds);        
        FadeTransition fadein = new FadeTransition(Duration.millis(1000), goal);
        fadein.setFromValue(0);
        fadein.setToValue(1);
        fadein.setCycleCount(1);
        TranslateTransition move = new TranslateTransition(Duration.millis(500),goal);
        move.setToY(100);
        move.setAutoReverse(true);
        move.setCycleCount(2);
        ScaleTransition scale = new ScaleTransition(Duration.millis(1000), goal);
        scale.setToX(20);
        scale.setToY(20);
        scale.setCycleCount(1);
        
        ParallelTransition parallel = new ParallelTransition();
        parallel.getChildren().addAll(move,scale,fadein);
        parallel.setCycleCount(1);
  
        return parallel;
    }
}
