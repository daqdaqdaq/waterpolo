/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.draganddrop;

import javafx.scene.Node;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 *
 * @author DAQ
 */
public class DropObjectDecorator {

    public static void decorate(Node underlying, ObjectReceiver receiver, DataFormat tocatch, TransferMode tm) {
        System.out.println("decorting drop");
        
        underlying.setOnDragDropped((DragEvent ev) -> {
            System.out.println("Drag dropped");
            Dragboard db = ev.getDragboard();
            System.out.println("Data is: "+db.getContent(DataFormat.PLAIN_TEXT)+" "+db.getString());
            receiver.handleObject(ev.getGestureSource(), db.getString());
           
            ev.setDropCompleted(true);
            ev.consume();
        });

        underlying.setOnDragOver((final DragEvent de) -> {
            System.out.println("Drag over");
            de.acceptTransferModes(tm);
            de.consume();
        });

        underlying.setOnDragEntered((DragEvent event) -> {
            System.out.println("Drag entered");
            underlying.setStyle("-fx-background-color: palegreen");
            event.consume();
        });

        //underlying.setOnDragDropped((DragEvent event) -> {
        //    underlying.setStyle("-fx-background-color: palegreen");
        //});
        underlying.setOnDragExited((DragEvent event) -> {
            System.out.println("Drag exited");
            underlying.setStyle("");
            event.consume();
        });

    }

}
