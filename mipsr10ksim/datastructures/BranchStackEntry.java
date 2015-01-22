/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.datastructures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mipsr10ksim.MIPSR10KSim;
import static mipsr10ksim.MIPSR10KSim.InstructionBuffer;
import mipsr10ksim.utils.BoundedQueue;
import mipsr10ksim.utils.UnoptimizedDeepCopy;

/**
 *
 * @author sridhar
 */
public class BranchStackEntry {
    public BoundedQueue<Instruction> InstructionBuffer = new BoundedQueue<Instruction>(8);
    public BoundedQueue<ActiveListEntry> ActiveList = new BoundedQueue<>(32);
    public BoundedQueue<QueueEntry> IntegerQueue = new BoundedQueue<>(32);
    public BoundedQueue<QueueEntry> FloatingPointQueue = new BoundedQueue<>(32);
    public BoundedQueue<QueueEntry> AddressQueue = new BoundedQueue<>(32);
    public BoundedQueue<QueueEntry> CommitBuffer = new BoundedQueue<>(64);
    public FreeList freeList = new FreeList();
    public RegisterMap rmap = new RegisterMap();
    public boolean BusyBits[] = new boolean[64]; 
    public int PC;
    
    public BranchStackEntry(BoundedQueue<Instruction> InBuffer, BoundedQueue<ActiveListEntry> ActiveList, 
            BoundedQueue<QueueEntry> IntegerQueue, BoundedQueue<QueueEntry> FloatingPointQueue, BoundedQueue<QueueEntry> AddressQueue, BoundedQueue<QueueEntry> CommitBuffer, 
            FreeList fList, RegisterMap rmap, boolean BusyBits[], int PC, List<Integer> markbusy) {
        
        this.rmap = (RegisterMap) rmap.clone();
        this.InstructionBuffer = (BoundedQueue<Instruction>) UnoptimizedDeepCopy.copy(InBuffer);
        Instruction instruction = this.InstructionBuffer.get(0);
        BoundedQueue<Instruction> bi= new BoundedQueue<Instruction>(1);
        bi.add(instruction);
        this.InstructionBuffer = bi;
        this.ActiveList = (BoundedQueue<ActiveListEntry>) MIPSR10KSim.ActiveList.clone();
        /*this.IntegerQueue = (BoundedQueue<QueueEntry>) MIPSR10KSim.IntegerQueue.clone();
        this.FloatingPointQueue = (BoundedQueue<QueueEntry>) MIPSR10KSim.FloatingPointQueue.clone();
        this.AddressQueue = (BoundedQueue<QueueEntry>) MIPSR10KSim.AddressQueue.clone();*/
        for(Integer i: fList.pregisters) {
            freeList.addRegister(i);
        }
        this.CommitBuffer = (BoundedQueue<QueueEntry>) CommitBuffer.clone();
        System.arraycopy(BusyBits, 0, this.BusyBits, 0, 64);
        for (Integer i: markbusy) {
            this.BusyBits[i]=true;
        }
        this.PC=PC;
    }
    
    public void restore() {
        //dumpContents();
        MIPSR10KSim.rmap = this.rmap;
        MIPSR10KSim.InstructionBuffer = (BoundedQueue<Instruction>) UnoptimizedDeepCopy.copy(this.InstructionBuffer);
        MIPSR10KSim.ActiveList = (BoundedQueue<ActiveListEntry>) this.ActiveList.clone();
        /*MIPSR10KSim.IntegerQueue = (BoundedQueue<QueueEntry>) this.IntegerQueue.clone();
        MIPSR10KSim.FloatingPointQueue = (BoundedQueue<QueueEntry>) this.FloatingPointQueue.clone();
        MIPSR10KSim.AddressQueue = (BoundedQueue<QueueEntry>) this.AddressQueue.clone();
        */
        MIPSR10KSim.freeList = this.freeList;
        System.arraycopy(this.BusyBits, 0, MIPSR10KSim.BusyBits, 0, 64);
        MIPSR10KSim.PC = this.PC+InstructionBuffer.size();
        //MIPSR10KSim.InstructionBuffer.get(0).setExtra(0);
       //System.out.println("Test: "+this.InstructionBuffer.size());
        
        //MIPSR10KSim.InstructionBuffer.clear();
        /*MIPSR10KSim.InstructionBuffer = new BoundedQueue<Instruction>(8);
        int count =0;
        Instruction ins=null;
        for (Instruction i: InstructionBuffer) {
            ins = new Instruction(i);
            if(count==0) {
                ins.setExtra(0);
                System.out.println("instruction buffer"+ins);             
            }
            MIPSR10KSim.InstructionBuffer.add(ins);
        }*/
        
    }
    
    public void dumpContents() {
        System.out.println("Dumped contents");
        for(ActiveListEntry ae: ActiveList) {
            System.out.println(ae.getEntryNum());
        }
        
        for (Instruction i: this.InstructionBuffer) {
            System.out.println(i);
        }
        for (int i=0; i<31; i++) {
            //System.out.println(i+"->"+rmap.getMapping(i));
            //System.out.println(rmap.getMapping(i));
        }
    }
}
