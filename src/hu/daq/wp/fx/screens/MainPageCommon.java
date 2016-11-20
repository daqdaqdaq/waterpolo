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
public interface MainPageCommon {

    void addScreen(SubScreen nd, String caption);

    Integer getLeftTeam();

    Integer getRightTeam();

    void setPlayingTeams(Integer leftteam, Integer rightteam);
    
}
