/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.stages;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import mipsr10ksim.MIPSR10KSim;
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
                    if(fpAddAllocation==null) {
                    fpAddAllocation = qe;
                    mipsr10ksim.MIPSR10KSim.State.addEntry(fpAddAllocation.getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "FA1"); 
                    if(fpAddAllocation.getInstruction().getDecodeId()==1) {
                        //System.out.println("Entered FA1"+MIPSR10KSim.clock);
                    }
                    }
                }
                else {
                    if(fpMulAllocation==null) {
                    fpMulAllocation = qe;
                    mipsr10ksim.MIPSR10KSim.State.addEntry(fpMulAllocation.getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "FM1");
                    }
                }
            }
        }
    }
    public static void calc2() {
        if(fp2buffer[0]!=null)
            mipsr10ksim.MIPSR10KSim.State.addEntry(fp2buffer[0].getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "FA2");        
        if(fp2buffer[1]!=null)
            mipsr10ksim.MIPSR10KSim.State.addEntry(fp2buffer[1].getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "FM2");        
    }
    public static void calc3() {
        if(fp3buffer[0]!=null)
            mipsr10ksim.MIPSR10KSim.State.addEntry(fp3buffer[0].getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "FA3");        
        if(fp3buffer[1]!=null)
            mipsr10ksim.MIPSR10KSim.State.addEntry(fp3buffer[1].getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "FM3");   
    }
    public static void calc() {
        calc1();
        calc2();
        calc3();
    }
    public static void edge1() {
        fp2buffer[0] = fpAddAllocation;
        fp2buffer[1] = fpMulAllocation;
        if(fpAddAllocation!=null &&  fpAddAllocation.getInstruction().getDecodeId()==1) {
                        //System.out.println("Removed FA1"+MIPSR10KSim.clock);
                    }
        if(fpAddAllocation!=null) {
            mipsr10ksim.MIPSR10KSim.BusyBits[fpAddAllocation.getInstruction().getDestinationRegister()]=false;
        }
        if(fpMulAllocation!=null) {
            mipsr10ksim.MIPSR10KSim.BusyBits[fpMulAllocation.getInstruction().getDestinationRegister()]=false;
        }
        mipsr10ksim.MIPSR10KSim.FloatingPointQueue.remove(fpAddAllocation);
        mipsr10ksim.MIPSR10KSim.FloatingPointQueue.remove(fpMulAllocation);
    }
    public static void edge2() {
        fp3buffer[0] = fp2buffer[0];
        fp3buffer[1] = fp2buffer[1];
        //if(fp2buffer[0]!=null)
            //mipsr10ksim.MIPSR10KSim.BusyBits[fp2buffer[0].getInstruction().getDestinationRegister()]=false;
       // if(fp2buffer[1]!=null)
       //     mipsr10ksim.MIPSR10KSim.BusyBits[fp2buffer[1].getInstruction().getDestinationRegister()]=false;
    }
    public static void edge3() {
        if(fp3buffer[0]!=null) {
            //System.out.println("Marking as not busy: "+fp3buffer[0].getInstruction());
            mipsr10ksim.MIPSR10KSim.BusyBits[fp3buffer[0].getInstruction().getDestinationRegister()]=false;
            mipsr10ksim.MIPSR10KSim.CommitBuffer.add(fp3buffer[0]);
        }
        if(fp3buffer[1]!=null) {
            //System.out.println("Marking as not busy: "+fp3buffer[1].getInstruction());
            
            mipsr10ksim.MIPSR10KSim.CommitBuffer.add(fp3buffer[1]);
        }
    }
    public static void edge() {
        List<QueueEntry> toRemove=new ArrayList<QueueEntry>();
        for(QueueEntry qe: MIPSR10KSim.FloatingPointQueue) {
            if(qe.getInstruction().getPC()>MIPSR10KSim.mispredictPC) {
                toRemove.add(qe);
            }
        }
        for (QueueEntry qe: toRemove) {
            MIPSR10KSim.FloatingPointQueue.remove(qe);
        }
        if(mipsr10ksim.MIPSR10KSim.exception) {
            if(fp3buffer[1]!=null && MIPSR10KSim.mispredictPC<fp3buffer[1].getInstruction().getPC()) {
                mipsr10ksim.MIPSR10KSim.State.addEntry(fp3buffer[1].getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "X");
                fp3buffer[1]=null;
            }
            if(fp3buffer[0]!=null && MIPSR10KSim.mispredictPC<fp3buffer[0].getInstruction().getPC()) {
                mipsr10ksim.MIPSR10KSim.State.addEntry(fp3buffer[0].getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "X");
                fp3buffer[1]=null;
            }
            if(fp2buffer[0]!=null && MIPSR10KSim.mispredictPC<fp2buffer[0].getInstruction().getPC()) {
                mipsr10ksim.MIPSR10KSim.State.addEntry(fp2buffer[0].getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "X");    
                fp2buffer[0]=null;
            }
            if(fp2buffer[1]!=null && MIPSR10KSim.mispredictPC<fp2buffer[1].getInstruction().getPC()) {
                mipsr10ksim.MIPSR10KSim.State.addEntry(fp2buffer[1].getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "X");
                fp2buffer[1]=null;
            }
            if(fpAddAllocation!=null && MIPSR10KSim.mispredictPC<fpAddAllocation.getInstruction().getPC()) {
                mipsr10ksim.MIPSR10KSim.State.addEntry(fpAddAllocation.getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "X");
                fpAddAllocation=null;
            }
            if(fpMulAllocation!=null && MIPSR10KSim.mispredictPC<fpMulAllocation.getInstruction().getPC()) {
                mipsr10ksim.MIPSR10KSim.State.addEntry(fpMulAllocation.getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "X");
                fpMulAllocation=null;
            }
         }
        edge3();
        edge2();
        edge1();
    }
}
