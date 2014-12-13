/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.datastructures;

import java.util.HashMap;

/**
 *
 * @author sridhar
 */
public class RegisterMap extends HashMap {
    HashMap registermap = new HashMap();
    
    public RegisterMap() {
        for(Integer i=0; i<32; i++) {
            registermap.put(i, i);
        }
    }
    
    public int getMapping(int aregister) {
        return (int) registermap.get(aregister);
    }
    
    public void updateMapping(int aregister, int pregister) {
        registermap.put(aregister, pregister);
    }
}
