/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.settings;

import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Properties;
import java.util.Set;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DAQ
 */
public class SettingsHandler {

    private Properties prop;
    private InputStream input = null;
    private OutputStream output = null;
    private String path;

    public SettingsHandler() {
        prop = new Properties();
    }

    public void loadProps(String path) {

        try {
            
            input = new FileInputStream(path);
            prop.load(input);
            
            this.path = path;
        } catch (FileNotFoundException ex) {
            System.out.print("File not found\n");
        } catch (IOException ex) {
            System.out.print("IO error\n");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(SettingsHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void saveProps(String path) {
        try {
            this.output = new FileOutputStream(path);
            prop.store(output, null);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SettingsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SettingsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    Logger.getLogger(SettingsHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

    public String getProperty(String property) {
        return prop.getProperty(property).trim();
    }

    public Integer getIntProperty(String property) {
        return Integer.parseInt(prop.getProperty(property).trim());
    }    
    
    public <T extends Object> void setProperty(String propname, T value) {
        this.prop.setProperty(propname, value.toString());
    }
}
