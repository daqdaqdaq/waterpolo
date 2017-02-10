/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.utils;

import hu.daq.thriftconnector.talkback.PenaltyTime;
import hu.daq.thriftconnector.talkback.PlayerStat;

/**
 *
 * @author DAQ
 */
public class PlayerStatus {
    Integer id;
    Integer penaltysec;
    Integer penalties;
    Integer goals;
    
    public PlayerStatus(Integer id, Integer penaltysec, Integer penalties, Integer goals){
        this.id = id;
        this.penalties = penalties;
        this.penaltysec = penaltysec;
        this.goals = goals;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPenaltysec() {
        return penaltysec;
    }

    public void setPenaltysec(Integer penaltysec) {
        this.penaltysec = penaltysec;
    }

    public Integer getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public Integer getPenalties() {
        return penalties;
    }

    public void setPenalties(Integer penalties) {
        this.penalties = penalties;
    }

   public PenaltyTime toPenaltyTime(){
       return new PenaltyTime(this.getId(),this.getPenaltysec());
   } 
   
   public PlayerStat toPlayerStat(){
       PlayerStat p = new PlayerStat();
       p.setPlayerid(this.getId());
       p.setNumgoals(this.getGoals());
       p.setNumpenalties(this.getPenalties());
       return p;
   }   
   
}
