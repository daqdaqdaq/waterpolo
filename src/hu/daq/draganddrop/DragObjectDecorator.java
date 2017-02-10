/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.draganddrop;

import hu.daq.servicehandler.ServiceHandler;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

/**
 *
 * @author DAQ
 */
public class DragObjectDecorator {

    Node underlying;
    DragObjectMediator dome;

    public static void decorate(Node underlying, DragObjectMediator mediator, TransferMode tm) {

        underlying.setOnDragDetected((final MouseEvent de) -> {
            //System.out.println("Detecting dragable state...");
            if (mediator.isDragable()){
                //System.out.println("Drag started..."+(String)mediator.extractDragObject());
                Dragboard db = underlying.startDragAndDrop(TransferMode.ANY);
                ClipboardContent cc = new ClipboardContent();
                cc.putString((String)mediator.extractDragObject());
              
                db.setContent(cc);
                ServiceHandler.getInstance().setDragSource(de.getSource());
            }
            de.consume();
        });

        underlying.setOnDragDone((DragEvent event) -> {

            mediator.finishDrag(event);
            //event.consume();

        });

    }

}
