/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.draganddrop;

import java.io.File;
import java.util.List;

/**
 *
 * @author DAQ
 */
public interface FileReceiver {
    public void handleFiles(List<File> files);
}
