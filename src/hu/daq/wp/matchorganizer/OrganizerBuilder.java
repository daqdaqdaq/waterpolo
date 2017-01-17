/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.matchorganizer;

import hu.daq.wp.MatchProfile;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author DAQ
 */
public class OrganizerBuilder {
    private static final Map<Integer, String> legnames;  
    static{
        Map<Integer,String> tmap =  new HashMap<Integer,String>();
        tmap.put(2, "félidő");
        tmap.put(3, "harmad");
        tmap.put(4, "negyed");
        legnames = tmap;
    }
    
    public static MatchOrganizer build(JSONObject source, Organizable screen) throws JSONException{
        MatchOrganizer mo = new MatchOrganizer();
        //Meccs elötti állapot
        mo.addPhase(new PreMatch(screen));
        mo.setBallTimeInSecs(source.getInt("balltimelength")/1000);
        Integer numlegs = source.getInt("numlegs");
        
        for (int i=0; i<numlegs; i++){
            //build and add a leg
            mo.addPhase(new MatchLeg(source.getInt("legduration"), legnames.get(numlegs), screen, i+1));
            //put a break between the legs
            if (i<numlegs-1){
                if (i==1){
                    mo.addPhase(new Break(screen,source.getInt("midbreakduration")));
                } else {
                    mo.addPhase(new Break(screen,source.getInt("breakduration")));
                }
            }
        }
        if (source.getInt("numovertimes")>0){
            mo.addPhase(new Break(screen,source.getInt("breakduration")));
            for (int i=0; i<source.getInt("numovertimes");i++){
                mo.addPhase(new Overtime(source.getInt("overtimeduration"),screen, i+1));
                mo.addPhase(new Break(screen,source.getInt("breakduration")));                
            }
        }
        mo.addPhase(new Fivers(screen));
        mo.addPhase(new Finished(screen));
        screen.setTimeoutListener(mo);        
        return mo;
    }
    
    public static MatchOrganizer build(String sourcestr, Organizable screen) throws JSONException{
        return build(new JSONObject(sourcestr),screen);
    }
    
    public static MatchOrganizer build(MatchProfile mp, Organizable screen) throws JSONException{
        MatchOrganizer mo = build(mp.getProfileRaw(),screen);
        mo.setId(mp.getID());
        return mo;
    }
}
