/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author sridhar
 */
public class State {
    public static ArrayList<HashMap<Integer, String>> state= new ArrayList<>();
    
    public void addEntry(int PC, int clock, String stage) {
        if(PC>state.size()-1) {
            HashMap<Integer,String> hm = new HashMap<Integer, String>();
            hm.put(clock, stage); 
            state.add(hm);
        }
        else {
            state.get(PC).put(clock, stage);
        }
    }
    
    public static void printState() {
        int i=0;
            String s= "instruction";
            String s2= String.format("%-30s",s);
            System.out.print(s2);
            for (Integer j=0;j<250;j++) {
                //String toPrint= String.format("%-30s",mipsr10ksim.MIPSR10KSim.decodeMap.get(i));
                String toprint = String.format("%-5s", j.toString());
                System.out.print(toprint);
            }
            System.out.println("\n");
            
        for (HashMap instruction: state) {
            Iterator it = instruction.entrySet().iterator();

            String toPrint= String.format("%-30s",mipsr10ksim.MIPSR10KSim.decodeMap.get(i));
            System.out.print(toPrint);
            for(int j=0;j<500;j++) {
                if(instruction.get(j)==null) {
                    toPrint = String.format("%-5s", "");
                }
                else {
                    toPrint = String.format("%-5s", instruction.get(j));
                }
                System.out.print(toPrint);
            }
            System.out.print("\n");
           /* while(it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                System.out.println(pairs.getKey()+"="+pairs.getValue());
            }*/
        i++;
        }
    }
}
