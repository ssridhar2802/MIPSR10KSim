/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.stages;

import mipsr10ksim.datastructures.QueueEntry;

/**
 *
 * @author sridhar
 */
public class MemoryUnit {
    static QueueEntry LS1;
    static QueueEntry LS2;
    static boolean noConflictingInstructions(Long address, int count) {
        int i=0;
        for (QueueEntry qe: mipsr10ksim.MIPSR10KSim.AddressQueue) {
            if (qe.getInstructionType()=='S' && !qe.isMarkedReady() && count>i)
                return false;
            i++;
        }
        return true;
    }
    static void calc1() {
        int count=0;
        LS1=null;
        for (QueueEntry qe: mipsr10ksim.MIPSR10KSim.AddressQueue) {
            if(qe.isMarkedReady()) {
                if(!qe.isInstructionExecutionIssued()) {
                    if(noConflictingInstructions(qe.getInstruction().getExtra(), count)) {
                        LS1=qe;
                    }
                }
            }
            count++;
        }
    }
    static void calc2() {
        if(LS2!=null) {
            
        }
    }
    public static void calc() {
        calc1();
        calc2();
    }
    static void edge1() {
        
    }
    static void edge2() {
        
    }
    public static void edge() {
        edge2();
        edge1();
    }
}
