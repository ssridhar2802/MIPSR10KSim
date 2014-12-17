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
public class FloatingPointUnit {
    static QueueEntry fpAddAllocation, fpMulAllocation;
    static QueueEntry[] fp2buffer = new QueueEntry[2];
    static QueueEntry[] fp3buffer = new QueueEntry[2];
    
    public static void calc1() {
        fpAddAllocation=null;
        fpMulAllocation=null;
        for (QueueEntry qe: mipsr10ksim.MIPSR10KSim.FloatingPointQueue) {
            if(qe.isMarkedReady()) {
                if(qe.getInstructionType()=='A') {
                    fpAddAllocation = qe;
                }
                else {
                    fpMulAllocation = qe;
                }
            }
        }
    }
    public static void calc2() {
        if(fp2buffer[0]!=null)
        System.out.println("clock:"+mipsr10ksim.MIPSR10KSim.clock+" 2nd stage of float for Instruction: "+fp2buffer[0].getInstruction());
        if(fp2buffer[1]!=null)
        System.out.println("clock:"+mipsr10ksim.MIPSR10KSim.clock+"2nd stage of float for Instruction: "+fp2buffer[1].getInstruction());
    }
    public static void calc3() {
        if(fp3buffer[0]!=null)
        System.out.println("clock:"+mipsr10ksim.MIPSR10KSim.clock+"3rd stage of float for Instruction: "+fp3buffer[0].getInstruction());
        if(fp3buffer[1]!=null)
        System.out.println("clock:"+mipsr10ksim.MIPSR10KSim.clock+"3rd stage of float for Instruction: "+fp3buffer[1].getInstruction());
    }
    public static void calc() {
        calc1();
        calc2();
        calc3();
    }
    public static void edge1() {
        mipsr10ksim.MIPSR10KSim.FloatingPointQueue.remove(fpAddAllocation);
        mipsr10ksim.MIPSR10KSim.FloatingPointQueue.remove(fpMulAllocation);
        fp2buffer[0] = fpAddAllocation;
        fp2buffer[1] = fpMulAllocation;
    }
    public static void edge2() {
        fp3buffer[0] = fp2buffer[0];
        fp3buffer[1] = fp2buffer[1];
    }
    public static void edge3() {
        if(fp3buffer[0]!=null) {
            System.out.println("Marking as not busy: "+fp3buffer[0].getInstruction());
            mipsr10ksim.MIPSR10KSim.BusyBits[fp3buffer[0].getInstruction().getDestinationRegister()]=false;
            mipsr10ksim.MIPSR10KSim.CommitBuffer.add(fp3buffer[0]);
        }
        if(fp3buffer[1]!=null) {
            System.out.println("Marking as not busy: "+fp3buffer[1].getInstruction());
            mipsr10ksim.MIPSR10KSim.BusyBits[fp3buffer[1].getInstruction().getDestinationRegister()]=false;
            mipsr10ksim.MIPSR10KSim.CommitBuffer.add(fp3buffer[1]);
        }
    }
    public static void edge() {
        edge3();
        edge2();
        edge1();
    }
}
