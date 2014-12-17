/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.datastructures;

/**
 *
 * @author sridhar
 */
public class QueueEntry {
    Instruction instruction;
    int activeListEntry;
    boolean readyForExecution = false;
    boolean executionIssued = false;
    
    public QueueEntry(Instruction instruction, int activeListEntry) {
        this.instruction = instruction;
        this.activeListEntry = activeListEntry;
    }
    
    public void readyForExecution(boolean readyForExecution) {
        this.readyForExecution = readyForExecution;
    }
    
    public int getActiveListEntry() {
        return this.activeListEntry;
    }
    public Instruction getInstruction() {
        return instruction;
    }
    public char getInstructionType() {
        return instruction.getOpcode();
    }
    public boolean isMarkedReady() {
        return readyForExecution;
    }
    public void setInstructionExecutionIssueFlag(boolean ready) {
        executionIssued = ready;
    }
    public boolean isInstructionExecutionIssued() {
        return this.executionIssued;
    }
}
