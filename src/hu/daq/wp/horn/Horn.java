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
    private Integer honktimemilisecs = 1000;
    
    public Horn(SenderThread st) {
        this.st = st;
    }

    public Horn(SenderThread st, Integer honktime) {
        this(st);
        this.honktimemilisecs = honktime;
    }

    public void setHonkTime(Integer honktime){
        this.honktimemilisecs = honktime;
    }
    
    public void honk(){
        this.honk(this.honktimemilisecs);
    }
    
    public void honk(Integer honktime){
        this.st.sendString("D"+honktime.toString());
    }
}
