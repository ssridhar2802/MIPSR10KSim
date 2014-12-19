/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.datastructures;

import mipsr10ksim.utils.BoundedQueue;

/**
 *
 * @author sridhar
 */
public class BranchStack {
    public static BoundedQueue<BranchStackEntry> BranchStack;
    
    public BranchStack(int size) {
        BranchStack = new BoundedQueue<>(4);
    }
    
    public void dumpContents() {
        for (BranchStackEntry be: BranchStack) {
            System.out.println("Dumping contents");
            be.dumpContents();
        }
    }
    
    public void removeEntry(int PC) {
        BranchStackEntry toRemove=null;
        for (BranchStackEntry be: BranchStack) {
            if(be.PC == PC) 
                toRemove = be;
        }
        BranchStack.remove(toRemove);
    }
    
    public void add(BranchStackEntry be) {
        BranchStack.add(be);
    }
    
    public boolean hasSpace() {
        return BranchStack.hasSpace();
    }
    
    public int size() {
        return BranchStack.size();
    }
    
    public void restoreBranch(int PC) {
        for (BranchStackEntry be: BranchStack) {
            if(be.PC == PC) {
                System.out.println("Restoring branch stack: Contents");
                be.dumpContents();
                be.restore();
                break;
            }
        }
    }
}
