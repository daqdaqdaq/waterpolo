/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.draganddrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * It filters nothing
 * @author DAQ
 */
public class PictureFilter extends FileFilter{
    
    ArrayList<String> suitable;
    
    public PictureFilter(){
        this.suitable = new ArrayList<String>(Arrays.asList("jpg","gif","png")); 
        
    }
    
    @Override
    public List<File> filterFiles(List<File> infiles) {
        return infiles.stream().filter(E -> suitable.contains(this.getExtension(E).toLowerCase())).collect(Collectors.toList());
    }
    
}
