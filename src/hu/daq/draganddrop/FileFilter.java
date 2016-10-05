/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.draganddrop;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author DAQ
 */
public abstract class FileFilter {
    protected String getExtension(File f){
        int i = f.getName().lastIndexOf(".");
        if (i>0){
            return f.getName().substring(+1);
        } else{
            return "";
        }
    }
    
    protected List<File> filterFiles(File f){
        return this.filterFiles(Arrays.asList(f));
    }
    
    protected abstract List<File> filterFiles(List<File> infiles);
}
