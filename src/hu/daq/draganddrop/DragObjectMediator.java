/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.draganddrop;

import javafx.event.Event;
import javafx.scene.input.DataFormat;

/**
 *
 * @author DAQ
 * @param <I>
 */
public interface DragObjectMediator<I extends Object> {
    public String extractDragObject();
    public DataFormat getDataFormat();
    public boolean isDragable();
    public void finishDrag(Event ev);
}
