/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.image;

import hu.daq.draganddrop.FileReceiver;
import hu.daq.fileservice.FileService;
import hu.daq.wp.fx.Instructable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author DAQ
 */
public class PlayerPicture extends ImageView implements FileReceiver{
    
    SimpleIntegerProperty picture_id ;
    File imgfile;
    byte[] imgtmp;
    Instructable instr;
    private boolean mustsave;
    
    
    public PlayerPicture(Instructable instr){
        this.picture_id = new SimpleIntegerProperty();
        this.setSmooth(true);
        this.setCache(true);
        this.instr = instr;
        this.mustsave = false;
    }
    
    public PlayerPicture(Instructable instr, SimpleIntegerProperty img_id){
        this(instr);
        this.picture_id.bindBidirectional(img_id);
    }
    
    
    
    private InputStream loadDefaultPic(){
        
        return this.getClass().getResourceAsStream("profile-placeholder.gif");
    }
    
    public void loadPic(){
        //System.out.println("Loading picture:"+picture_id.toString());
        InputStream tmp;
        if (this.picture_id.get() == 0){
            tmp = this.loadDefaultPic();
        } else{
            try {
                tmp = FileService.getInst().getFile(picture_id.get());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PlayerPicture.class.getName()).log(Level.SEVERE, "Loading picture has failed. Showing placeholder instead", ex);
                tmp = this.loadDefaultPic();
            }
        }
        this.setImage(new Image(tmp));
    }
    
    public SimpleIntegerProperty getPicture_id() {
        return picture_id;
    }

    public void savePic(){
        if (this.mustsave){
            try {
                this.picture_id.set(FileService.getInst().saveFile(this.imgfile));
                this.imgfile = null;
            } catch (IOException ex) {
                Logger.getLogger(PlayerPicture.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    @Override
    public void handleFiles(List<File> files) {
        System.out.println("Handling incomming files");
        try {
            this.imgfile = files.get(0);
            FileInputStream fistmp = new FileInputStream(files.get(0)) ;
            this.setImage(new Image(fistmp));
            this.mustsave = true;
            this.instr.execute();
        } catch (FileNotFoundException ex) {
            System.out.println("Sumtin went wlong");
            System.out.println(ex.toString());
            Logger.getLogger(PlayerPicture.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
