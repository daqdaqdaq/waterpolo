/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.penalties;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 *
 * @author DAQ
 */
public class PenaltiesFX extends HBox {

    private final static Integer MAX_NUM_PENALTIES = 3;
    private int penalties;
    private ShapeFactory sf;
    private SimpleBooleanProperty finallyout  = new SimpleBooleanProperty(Boolean.FALSE);

    public PenaltiesFX(ShapeFactory sf, double width) {
        this.penalties = 0;
        this.sf = sf;
        this.setMinWidth(width);
        this.build();
    }

    private void build() {
        //System.out.println("Building penalties");
        //this.setMinHeight(100);

        
        //cumulated size of the penalty indicator shapes
        //double psize = this.heightProperty().multiply(0.8).get();
        double psize = 20;
        
        /*if (width>this.heightProperty().getValue()){
            width = this.heightProperty().getValue()*0.90;
        }*/
        //System.out.println("The width is "+width );
        //spacing beetween the indicators
        //double spacing = (this.widthProperty().getValue() - width) / (MAX_NUM_PENALTIES + 1);
        this.setSpacing(5);
        for (int i = 0; i <MAX_NUM_PENALTIES; i++) {
            Shape pen = sf.getShape(psize);
            pen.setFill(Color.LIGHTGREEN);
          
            //System.out.println("Adding shape "+psize);
            this.getChildren().add(pen);
        }
        this.setAlignment(Pos.CENTER);
        //this.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
    }

    public Integer addPenalty() {
        if (!this.finallyout.getValue()) {
            ((Shape) this.getChildren().get(this.penalties)).setFill(Color.RED);
            this.penalties++;
            if (this.penalties == MAX_NUM_PENALTIES) {
                this.finallyout.setValue(Boolean.TRUE);
            }
        }
        return this.penalties;
    }

    public Integer removePenalty() {
        if (this.penalties > 0) {
            ((Shape) this.getChildren().get(this.penalties-1)).setFill(Color.LIGHTGREEN);
            this.penalties--;
            if (this.finallyout.getValue()) {
                this.finallyout.setValue(Boolean.FALSE);
            }
        }
        return this.penalties;
    }

    public SimpleBooleanProperty getFinallyout() {
        return finallyout;
    }

}
