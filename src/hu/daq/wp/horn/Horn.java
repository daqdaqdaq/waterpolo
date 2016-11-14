/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.horn;

import hu.daq.UDPSender.SenderThread;

/**
 *
 * @author DAQ
 */
public class Horn {
    private SenderThread st;
    private Integer honktimeshort = 1;
    private Integer honktimelong = 3;    
    
    public Horn(SenderThread st) {
        this.st = st;
    }

    public Horn(SenderThread st, Integer shorthonktime, Integer longhonktime) {
        this(st);
        this.setShortHonkTime(shorthonktime);
        this.setLongHonkTime(longhonktime);
        
    }

    protected void setShortHonkTime(Integer honktime){
        this.honktimeshort = honktime;
    }
    
    protected void setLongHonkTime(Integer honktime){
        this.honktimelong = honktime;
    }    

    public void honkShort(){
        this.honk(this.honktimeshort);
    }

    public void honkLong(){
        this.honk(this.honktimelong);
    }    
    
    public void honk(Integer honktime){
        this.st.sendString("D"+honktime.toString());
    }
}
