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
    
    public QueueEntry(Instruction instruction, int activeListEntry) {
        this.instruction = instruction;
        this.activeListEntry = activeListEntry;
    }
    
    public void readyForExecution(boolean readyForExecution) {
        this.readyForExecution = readyForExecution;
    }
}
