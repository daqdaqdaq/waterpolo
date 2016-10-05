/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.texteffects;

import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;

/**
 *
 * @author DAQ
 */
public class TextEffect {

    public final static Effect getNeonEffect() {
        Blend blend = new Blend();
        blend.setMode(BlendMode.MULTIPLY);
              
        DropShadow ds = new DropShadow();
        ds.setColor(Color.rgb(254, 235, 66, 0.3));
        ds.setOffsetX(5);
        ds.setOffsetY(5);
        ds.setRadius(5);
        ds.setSpread(0.2);
        
        blend.setBottomInput(ds);
        
        DropShadow ds1 = new DropShadow();
        ds1.setColor(Color.web("#f13a00"));
        ds1.setRadius(20);
        ds1.setSpread(0.2);
        
        Blend blend2 = new Blend();
        blend2.setMode(BlendMode.MULTIPLY);
        
        InnerShadow is = new InnerShadow();
        is.setColor(Color.web("#feeb42"));
        is.setRadius(9);
        is.setChoke(0.8);
        blend2.setBottomInput(is);
        
        InnerShadow is1 = new InnerShadow();
        is1.setColor(Color.web("#f13a00"));
        is1.setRadius(5);
        is1.setChoke(0.4);
        blend2.setTopInput(is1);
        
        Blend blend1 = new Blend();
        blend1.setMode(BlendMode.MULTIPLY);
        blend1.setBottomInput(ds1);
        blend1.setTopInput(blend2);
        
        blend.setTopInput(blend1);

        return blend;
    }
    
    public final static Effect getNewNeonEffect() {

              
        DropShadow ds = new DropShadow();
        ds.setColor(Color.YELLOWGREEN);
        ds.setOffsetX(5);
        ds.setOffsetY(5);
        ds.setRadius(5);
        ds.setSpread(0.2);


        return ds;
    }    

    public final static Effect getNewerNeonEffect() {
        Blend blend = new Blend(BlendMode.ADD);
        DropShadow ds = new DropShadow();
        ds.setColor(Color.ORANGE);
        ds.setOffsetX(1);
        ds.setOffsetY(1);
        ds.setRadius(2);
        ds.setSpread(0.1);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setContrast(0.1);
        //colorAdjust.setHue(-0.05);
        colorAdjust.setBrightness(0.3);
        colorAdjust.setSaturation(0.8);
        
        blend.setBottomInput(ds);
        blend.setTopInput(colorAdjust);


        return blend;
    }     

}