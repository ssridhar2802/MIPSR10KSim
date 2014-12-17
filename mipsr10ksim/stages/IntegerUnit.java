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
public class IntegerUnit {
    static QueueEntry alu1Allocation,alu2Allocation;
    
    public static void calc() {
        alu1Allocation=null;
        alu2Allocation=null;
        
        for(QueueEntry qe: mipsr10ksim.MIPSR10KSim.IntegerQueue) {
            if(qe.isMarkedReady()) {
                if(qe.getInstructionType()=='B') {
                    alu1Allocation = qe;
                }
                else {
                    if(alu2Allocation!=null) {
                        if(alu1Allocation==null)
                            alu1Allocation = qe;
                        else if (alu1Allocation.getInstructionType()!='B')
                            alu1Allocation = qe;
       
                    }
                    else {
                        alu2Allocation = qe;
                    }
                }
            }
            if(alu1Allocation!=null && alu2Allocation!=null && alu1Allocation.getInstructionType()=='B') {
                break;
            }
            
        }
        /*if(alu1Allocation!=null) 
            //System.out.println("ALU1 allotted to :"+alu1Allocation.getInstruction());
        if(alu2Allocation!=null)
            System.out.println("ALU2 allotted to :"+alu2Allocation.getInstruction());*/
    }
    public static void edge() {
        mipsr10ksim.MIPSR10KSim.IntegerQueue.remove(alu1Allocation);
        mipsr10ksim.MIPSR10KSim.IntegerQueue.remove(alu2Allocation);
        if(alu1Allocation!=null) {
            //TODO: Handle misprediction if instruction is a branch
            if(alu1Allocation.getInstructionType()=='B') {
                
            }
            else {
                //System.out.println("Marking as not busy: "+alu1Allocation.getInstruction());
                mipsr10ksim.MIPSR10KSim.BusyBits[alu1Allocation.getInstruction().getDestinationRegister()]=false;
            }
            mipsr10ksim.MIPSR10KSim.CommitBuffer.add(alu1Allocation);
        }
        if(alu2Allocation!=null) {
            //System.out.println("Marking as not busy: "+alu2Allocation.getInstruction());
            mipsr10ksim.MIPSR10KSim.BusyBits[alu2Allocation.getInstruction().getDestinationRegister()]=false;
            mipsr10ksim.MIPSR10KSim.CommitBuffer.add(alu2Allocation);
        }
    }
}
