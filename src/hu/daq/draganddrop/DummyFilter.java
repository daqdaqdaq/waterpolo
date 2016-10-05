/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.draganddrop;

import java.io.File;
import java.util.List;

/**
 * It filters nothing
 * @author DAQ
 */
public class DummyFilter extends FileFilter{

    @Override
    public List<File> filterFiles(List<File> infiles) {
        System.out.println("Filtering...");
        return infiles;
    }
    
}
