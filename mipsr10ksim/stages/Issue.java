/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.stages;

import java.util.ArrayList;
import java.util.List;
import mipsr10ksim.MIPSR10KSim;
import mipsr10ksim.datastructures.Instruction;
import mipsr10ksim.datastructures.QueueEntry;

/**
 *
 * @author sridhar
 */
public class Issue {
    public static List<QueueEntry> integerReady;
    public static List<QueueEntry> floatingReady;
    public static List<QueueEntry> addressReady;
    
    public static boolean isReady(Instruction instruction) {
        if(instruction.getOpcode()=='L') {
            if(MIPSR10KSim.BusyBits[instruction.getRd()] && MIPSR10KSim.BusyBits[instruction.getRs()]) {
                //System.out.println("Instruction is not ready: "+instruction +MIPSR10KSim.BusyBits[instruction.getRd()]+MIPSR10KSim.BusyBits[instruction.getRs()]);
                return false;
            } else {
                return true;
            }
        }
        else {
            if((instruction.getRt()==-1||MIPSR10KSim.BusyBits[instruction.getRt()]) && (instruction.getRs()==-1||MIPSR10KSim.BusyBits[instruction.getRs()])) {
                //System.out.println("Instruction is not ready: "+instruction+MIPSR10KSim.BusyBits[instruction.getRt()]+MIPSR10KSim.BusyBits[instruction.getRs()]);
                return false;
            } else {
                return true;
            }
        }
    }
    public static void markIntegerInstructions() {
        integerReady = new ArrayList<>();
        for (QueueEntry qe: mipsr10ksim.MIPSR10KSim.IntegerQueue) {
            if(!qe.isMarkedReady()) {
                if(isReady(qe.getInstruction())) {
                    //qe.readyForExecution(true);
                    integerReady.add(qe);
                    mipsr10ksim.MIPSR10KSim.State.addEntry(qe.getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "I");
            }
            }
        }
    }
    public static void markFloatingPointInstructions() {
        floatingReady = new ArrayList<>();
        for(QueueEntry qe: mipsr10ksim.MIPSR10KSim.FloatingPointQueue) {
            if(!qe.isMarkedReady()) {
                if(isReady(qe.getInstruction())) {
                    //qe.readyForExecution(true);
                    //System.out.println("Adding instruction: "+qe.getInstruction());
                    floatingReady.add(qe);
                    mipsr10ksim.MIPSR10KSim.State.addEntry(qe.getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "I");
            }
            
        }
    }
    }
    public static void markAddressInstructions() {
        addressReady = new ArrayList<>();
        for(QueueEntry qe: mipsr10ksim.MIPSR10KSim.AddressQueue) {
            if(!qe.isMarkedReady()) {
                if(isReady(qe.getInstruction())) {
                    //qe.readyForExecution(true);
                    addressReady.add(qe);
                    mipsr10ksim.MIPSR10KSim.State.addEntry(qe.getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "I");
            }
            
        }
        }
    }
    public static void calc() {
        markIntegerInstructions();
        markFloatingPointInstructions();
        markAddressInstructions();  
    }
    public static void edge() {
        if(mipsr10ksim.MIPSR10KSim.exception) {
            return;
        }
        for(QueueEntry entry: integerReady) {
            entry.readyForExecution(true);
            //System.out.println("Ready integer operations: "+entry.getInstruction()+"test"+mipsr10ksim.MIPSR10KSim.clock);
        }
        for(QueueEntry entry: floatingReady) {
            entry.readyForExecution(true);
            //System.out.println("Ready floating point operations: "+entry.getActiveListEntry());
        }
        for(QueueEntry entry: addressReady) {
            entry.readyForExecution(true);
            //System.out.println("Ready address operations: "+entry.getActiveListEntry());
        }
    }
}
