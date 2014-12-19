/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.stages;

import java.util.ArrayList;
import java.util.List;
import mipsr10ksim.MIPSR10KSim;
import mipsr10ksim.datastructures.QueueEntry;

/**
 *
 * @author sridhar
 */
public class MemoryUnit {
    static ArrayList<QueueEntry> LS1;
    static QueueEntry LS2;
    static boolean noConflictingInstructions(Long address, int count) {
        int i=0;
        for (QueueEntry qe: mipsr10ksim.MIPSR10KSim.AddressQueue) {
            if (qe.getInstructionType()=='S' && count>i)
                if(qe.isInstructionExecutionIssued()!=true || qe.getInstruction().getExtra()==address)
                    return false;
            i++;
        }
        return true;
    }
    static void calc1() {
        int count=0;
        LS1=new ArrayList<>();
        for (QueueEntry qe: mipsr10ksim.MIPSR10KSim.AddressQueue) {
            if(qe.isMarkedReady()) {
                if(!qe.isInstructionExecutionIssued()) {
                    //if(noConflictingInstructions(qe.getInstruction().getExtra(), count)) {
                        //LS1=qe;
                        mipsr10ksim.MIPSR10KSim.State.addEntry(qe.getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "A");
                        //qe.setInstructionExecutionIssueFlag(true);
                        LS1.add(qe);
                        //System.out.println("In LS1: " +qe.getInstruction());
                        //break;
                   // }
                }
            }
            count++;
        }
    }
    static void calc2() {
        LS2=null;
        int count=0;
        for (QueueEntry qe: mipsr10ksim.MIPSR10KSim.AddressQueue) {
            if(qe.isMarkedReady()) {
                if(qe.isInstructionExecutionIssued()) {
                    if(noConflictingInstructions(qe.getInstruction().getExtra(), count)) {
                        if(!qe.isExecutionDone()) {
                            LS2=qe;
                            qe.setExecutionDone(true);
                            break;
                        }
                }
                    else {
                        System.out.println("Waiting for conflicting store: "+qe.getInstruction()+" :"+qe.getInstruction().getPC());
                    }
            }
            
        }
        count++;
    }
        if(LS2!=null) {
            mipsr10ksim.MIPSR10KSim.State.addEntry(LS2.getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "M");
            //System.out.println("In LS2 stage: "+LS2.getInstruction());
        }
    }
    public static void calc() {
        calc1();
        calc2();
    }
    static void edge1() {
        //LS2=LS1;4
        for(QueueEntry qe: LS1) {
            qe.setInstructionExecutionIssueFlag(true);
        }
    }
    static void edge2() {
        if(LS2!=null) {
            mipsr10ksim.MIPSR10KSim.CommitBuffer.add(LS2);
        }
    }
    public static void edge() {
        List<QueueEntry> toRemove=new ArrayList<>();
        for(QueueEntry qe: MIPSR10KSim.AddressQueue) {
            if(qe.getInstruction().getPC()>MIPSR10KSim.mispredictPC) {
                toRemove.add(qe);
            }
        }
        if(toRemove!=null) {
        for (QueueEntry qe: toRemove) {
            MIPSR10KSim.AddressQueue.remove(qe);
        }
        }
        if(LS2!=null && LS2.getInstruction().getPC()>mipsr10ksim.MIPSR10KSim.mispredictPC) {
            mipsr10ksim.MIPSR10KSim.State.addEntry(LS2.getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "X");
            LS2=null;
        }
            edge2();
            edge1();
    }
}
