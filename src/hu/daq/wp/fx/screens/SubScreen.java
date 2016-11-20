/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.screens;

/**
 *
 * @author DAQ
 */
public interface SubScreen {

    public void initScreen();
    public Boolean isAdminOnly();
    public SubScreen addContainer(MainPageCommon nd);
    public MainPageCommon getContainer();

    
}
