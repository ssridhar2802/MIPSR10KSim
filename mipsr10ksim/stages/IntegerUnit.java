/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.stages;

import java.util.ArrayList;
import java.util.List;
import mipsr10ksim.MIPSR10KSim;
import mipsr10ksim.datastructures.BranchStack;
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
                    if(alu1Allocation!=null&&alu1Allocation.getInstructionType()=='B')
                        continue;
                    if(alu1Allocation==null) {
                        alu1Allocation = qe;
                    }
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
        if(alu1Allocation!=null) {
            //System.out.println("ALU1 allotted to :"+alu1Allocation.getInstruction()+mipsr10ksim.MIPSR10KSim.clock);
            mipsr10ksim.MIPSR10KSim.State.addEntry(alu1Allocation.getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "E");
        }
            //
        if(alu2Allocation!=null) {
            //System.out.println("ALU2 allotted to :"+alu2Allocation.getInstruction()+mipsr10ksim.MIPSR10KSim.clock);
            mipsr10ksim.MIPSR10KSim.State.addEntry(alu2Allocation.getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "E");
        }
    }
    public static void edge() {
        
        List<QueueEntry> toRemove=new ArrayList<>();
        for(QueueEntry qe: MIPSR10KSim.IntegerQueue) {
            if(qe.getInstruction().getPC()>MIPSR10KSim.mispredictPC) {
                toRemove.add(qe);
            }
        }

        for (QueueEntry qe: toRemove) {
            MIPSR10KSim.IntegerQueue.remove(qe);
        }

        mipsr10ksim.MIPSR10KSim.IntegerQueue.remove(alu1Allocation);
        mipsr10ksim.MIPSR10KSim.IntegerQueue.remove(alu2Allocation);
        
        if(alu1Allocation!=null) {
            //System.out.println("Alu1allocation :" +alu1Allocation.getInstruction());
            //TODO: Handle misprediction if instruction is a branch
            if(alu1Allocation.getInstructionType()=='B') {
                if(alu1Allocation.getInstruction().getExtra()==0) {
                    //System.out.println("Branch prediction success:  removing entry from queue: "+alu1Allocation.getInstruction().getPC()+ " :"+mipsr10ksim.MIPSR10KSim.branchStack.size());
                    mipsr10ksim.MIPSR10KSim.branchStack.removeEntry(alu1Allocation.getInstruction().getPC());
                }
                else {
                   // System.out.println("Branch prediction failure: restoring branch stack for PC "+alu1Allocation.getInstruction().getPC());
                    MIPSR10KSim.exception = true;
                    //MIPSR10KSim.clearDataStructures();
                    //System.out.println("Dump before restore");
                    MIPSR10KSim.branchStack.dumpContents();
                    MIPSR10KSim.branchStack.restoreBranch(alu1Allocation.getInstruction().getPC());
                    MIPSR10KSim.offsetTemp = MIPSR10KSim.offset+MIPSR10KSim.PC - alu1Allocation.getInstruction().getPC();
                    //System.out.println("OFFSET: "+MIPSR10KSim.offset);
                    System.out.println("System state after restore");
                    MIPSR10KSim.printSystemState();
                    MIPSR10KSim.mispredictPC=alu1Allocation.getInstruction().getPC();
                    mipsr10ksim.MIPSR10KSim.branchStack.removeEntry(alu1Allocation.getInstruction().getPC());
                    return;
                }
            }
            else {
                //System.out.println("Marking as not busy: "+alu1Allocation.getInstruction());
                //mipsr10ksim.MIPSR10KSim.BusyBits[alu1Allocation.getInstruction().getDestinationRegister()]=false;
            }
            if(alu1Allocation.getInstruction().getDestinationRegister()!=-1)
                mipsr10ksim.MIPSR10KSim.BusyBits[alu1Allocation.getInstruction().getDestinationRegister()]=false;
            mipsr10ksim.MIPSR10KSim.CommitBuffer.add(alu1Allocation);
        }
        if(alu2Allocation!=null) {
            //System.out.println("Marking as not busy: "+alu2Allocation.getInstruction());
            mipsr10ksim.MIPSR10KSim.BusyBits[alu2Allocation.getInstruction().getDestinationRegister()]=false;
            mipsr10ksim.MIPSR10KSim.CommitBuffer.add(alu2Allocation);
        }
    }
}
