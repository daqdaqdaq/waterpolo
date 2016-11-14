/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.matchorganizer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author DAQ
 */
public class RomanNumber {
    private static final Map<Integer,String> nums;
    static{
        Map<Integer, String> tmap = new HashMap<Integer, String>();
        tmap.put(1, "I");
        tmap.put(2, "II");
        tmap.put(3, "III");
        tmap.put(4, "IV");
        tmap.put(5, "V");
        nums = Collections.unmodifiableMap(tmap);
    }
            
       
    public static String get(Integer i){
        return nums.get(i);
    }
}
