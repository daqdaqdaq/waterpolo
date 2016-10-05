/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.control;

/**
 *
 * @author DAQ
 */
public class ErrorWrapper extends ResultWrapper{

    @Override
    public Boolean isError() {
        return Boolean.TRUE;
    }
    
    public void putError(Exception e){
        this.put("error", e);
    }
    
    public Exception getError(){
        return (Exception) this.get("error");
    }

}
