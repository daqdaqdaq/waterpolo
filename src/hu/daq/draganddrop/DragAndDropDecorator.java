/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.draganddrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 *
 * @author DAQ
 */
public class DragAndDropDecorator {
    FileFilter filter;
    Node underlying;
    List<File> lastcatch = new ArrayList<File>();
    ObservableList<File> catchedfiles = FXCollections.observableList(lastcatch);
    FileReceiver filereceiver;
    Boolean singlefilemode = true;
    
    public DragAndDropDecorator(Node underlying){
        this(underlying, new DummyFilter());
    }
    
    public DragAndDropDecorator(Node underlying, FileFilter filter){
        this.underlying = underlying;
        this.underlying.setOnDragDropped((DragEvent ev) -> this.catchFile(ev));
        this.underlying.setOnDragOver((final DragEvent de) -> {
            de.acceptTransferModes(TransferMode.LINK);
            de.consume();
        });
        this.filter = filter;
    }
    
    
    public void setFilter(FileFilter filter){
        this.filter = filter;
    }
    
    public DragAndDropDecorator setSingleFileMode(Boolean sfm){
        this.singlefilemode  = sfm;
        return this;
    }
    
    
    private void catchFile(DragEvent ev){
        System.out.println("Catching file...");
        Dragboard db =ev.getDragboard();
        if (db.hasFiles()){
            System.out.println("File found...");
            this.catchedfiles.clear();
            if (this.singlefilemode){
                System.out.println("Single file...");
                this.catchedfiles.addAll(this.filter.filterFiles(db.getFiles().get(0)));
            } else {
                System.out.println("Multi file");
                this.catchedfiles.addAll(this.filter.filterFiles(db.getFiles()));
            }
            if (filereceiver!=null){
                System.out.println("Calling handler...");
                filereceiver.handleFiles(this.catchedfiles);
            }
        }
        ev.consume();
    }
    
    public List<File> getFiles(){
        return this.catchedfiles;
    }
    
    public void registerFileReceiver(FileReceiver fr){
        this.filereceiver = fr;
    }
    
    public void unregisterFileReceiver(FileReceiver fr){
        this.filereceiver = null;
    }
}
