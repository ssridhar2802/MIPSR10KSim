/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.datastructures;

import java.util.Queue;
import mipsr10ksim.utils.BoundedQueue;

/**
 *
 * @author sridhar
 */
public class FreeList {
    public BoundedQueue<Integer> pregisters;
    public FreeList() {
         this.pregisters = new BoundedQueue<Integer>(32);
         for(int i=32; i<64; i++) {
             pregisters.add(i);
         }
    }
    
    public Integer getRegister() {
        return pregisters.pop();
    }
    public boolean addRegister(int reg) {
        return pregisters.add(reg);   
    }
    
}

