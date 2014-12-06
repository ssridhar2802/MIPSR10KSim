/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.utils;

import java.util.LinkedList;

/**
 *
 * @author sridhar
 */
public class BoundedQueue<E> extends LinkedList<E> {
    private int capacity;
    
    public BoundedQueue(int capacity) {
        this.capacity = capacity;
    }
    
    @Override
    public boolean add(E o) {
        if(size() == capacity)
            return false;
        else
            super.add(o);
        return true;
    }
    
    public boolean hasSpace() {
        if (size() == capacity)
            return false;
        else 
            return true;
    }
}
