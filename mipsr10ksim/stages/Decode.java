/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.stages;
import java.util.ArrayList;
import java.util.List;
import mipsr10ksim.datastructures.Instruction;
import mipsr10ksim.MIPSR10KSim;
import static mipsr10ksim.MIPSR10KSim.BusyBits;
import mipsr10ksim.datastructures.ActiveListEntry;
import mipsr10ksim.datastructures.BranchStackEntry;
import mipsr10ksim.datastructures.FreeList;
import mipsr10ksim.datastructures.QueueEntry;
import mipsr10ksim.datastructures.RegisterMap;
import mipsr10ksim.utils.BoundedQueue;
import mipsr10ksim.utils.UnoptimizedDeepCopy;
/**
 *
 * @author sridhar
 */
public class Decode {
    //int counter;
    static RegisterMap rmap;
    static BoundedQueue<ActiveListEntry> ActiveList;
    static BoundedQueue<QueueEntry> IntegerQueue;
    static BoundedQueue<QueueEntry> FloatingPointQueue;
    static BoundedQueue<QueueEntry> AddressQueue;
    static FreeList freeList;
    static List<Integer> markbusy;
    
    public static void mutate(BoundedQueue<Instruction> instructionbuffer) {
        Instruction instruction =instructionbuffer.get(0);
        instruction.setExtra(0);
    }
    
    public static void remapSourceRegisters(Instruction i) {
        if(i.getOpcode()=='L') {
            i.setRd(rmap.getMapping(i.getRd()));
            i.setRs(rmap.getMapping(i.getRs()));
        }
        else {
            i.setRt(rmap.getMapping(i.getRt()));
            i.setRs(rmap.getMapping(i.getRs()));
        }
    }
    
    public static void remapDestinationRegisters(Instruction i) {
        if(i.getOpcode()=='L') 
            i.setRt(rmap.getMapping(i.getRt()));
        else
            i.setRd(rmap.getMapping(i.getRd()));
    }
    
 /*  public static Instruction remapInstructions(Instruction i) {
        i.setRd(rmap.getMapping(i.getRd()));
        i.setRt(rmap.getMapping(i.getRt()));
        i.setRs(rmap.getMapping(i.getRs()));
        return i;
    } */
    
    public static void calc() {
        //TODO: Map source operands before destination operands
        
        rmap = (RegisterMap) MIPSR10KSim.rmap.clone();
        ActiveList = (BoundedQueue<ActiveListEntry>) MIPSR10KSim.ActiveList.clone();
        IntegerQueue = (BoundedQueue<QueueEntry>) MIPSR10KSim.IntegerQueue.clone();
        FloatingPointQueue = (BoundedQueue<QueueEntry>) MIPSR10KSim.FloatingPointQueue.clone();
        AddressQueue = (BoundedQueue<QueueEntry>) MIPSR10KSim.AddressQueue.clone();
        freeList = MIPSR10KSim.freeList;
        markbusy = new ArrayList<>();
        
        for (int i=0; i<MIPSR10KSim.num_issue; i++) {
            if (MIPSR10KSim.InstructionBuffer.isEmpty())
                break;
            if (!ActiveList.hasSpace()) {
                System.out.println("Active list out of space");
                break;
            }
            Instruction instruction = new Instruction(MIPSR10KSim.InstructionBuffer.get(0));
            //System.out.println("DecodeId: "+instruction+" ="+instruction.getDecodeId());
            Instruction ins = (Instruction) UnoptimizedDeepCopy.copy(instruction);
            MIPSR10KSim.decodeMap.put(ins.getDecodeId(), ins);
            
            char instructionType = instruction.getOpcode();
            
            int destinationregister = instruction.getDestinationRegister();
            
            ActiveListEntry ae = null;
            //System.out.println("actual: "+instruction);
            remapSourceRegisters(instruction);
            if(destinationregister == 0) {
                ae = new ActiveListEntry(instructionType, 0, 0, 0);
            }
            else if(destinationregister != -1) {
                int newMapping = freeList.getRegister();
                int oldMapping = rmap.getMapping(destinationregister);
                rmap.updateMapping(destinationregister, newMapping);
                markbusy.add(newMapping);
                //instruction.setDestinationRegister(newMapping);
                ae = new ActiveListEntry(instructionType, destinationregister, newMapping, oldMapping);
            }
            else {
                ae = new ActiveListEntry(instructionType, -1, -1, -1);
            }
            int entryNum = ae.getEntryNum();
            //System.out.println("actual: "+instruction);
            remapDestinationRegisters(instruction);
            //System.out.println("remapped: "+instruction);
            QueueEntry qe = new QueueEntry(instruction, entryNum);
            if (instructionType == 'L' || instructionType == 'S') {
                if(AddressQueue.hasSpace()) {
                    AddressQueue.add(qe);
                }
                else
                    break;
            }
            else if (instructionType == 'I' || instructionType == 'B') {
                if(instructionType=='B'&&MIPSR10KSim.branchStack.size()<4&&IntegerQueue.hasSpace()) {
                    BoundedQueue<Instruction> instructionbuffer = (BoundedQueue<Instruction>) UnoptimizedDeepCopy.copy(MIPSR10KSim.InstructionBuffer);
                    mutate(instructionbuffer);
                    //System.out.println("Here: "+MIPSR10KSim.branchStack.size());
                    //System.out.println("Setting branch stack for PC: "+instruction.getPC());
                    BranchStackEntry be = new BranchStackEntry(instructionbuffer, ActiveList, IntegerQueue, FloatingPointQueue, AddressQueue, MIPSR10KSim.CommitBuffer, freeList, rmap, BusyBits, instruction.getPC(), markbusy);
                    MIPSR10KSim.branchStack.add(be);
                    IntegerQueue.add(qe);
                }
                else if(instructionType == 'I'&&IntegerQueue.hasSpace()) {
                    IntegerQueue.add(qe);
                }
                else
                    break;
            }
            else if (instructionType == 'A' || instructionType == 'M') {
                if(FloatingPointQueue.hasSpace()) {
                    FloatingPointQueue.add(qe);
                }
                else
                    break;
            }
            ActiveList.add(ae);
            mipsr10ksim.MIPSR10KSim.InstructionBuffer.removeFirst();
            mipsr10ksim.MIPSR10KSim.State.addEntry(instruction.getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "D");
            mipsr10ksim.MIPSR10KSim.State.addEntry(instruction.getDecodeId(), mipsr10ksim.MIPSR10KSim.clock-1, "F");
            //System.out.println(ae);
            
        }
    }
    public static void edge() {

        MIPSR10KSim.ActiveList = ActiveList;
        MIPSR10KSim.AddressQueue = AddressQueue;
        MIPSR10KSim.FloatingPointQueue = FloatingPointQueue;
        MIPSR10KSim.IntegerQueue = IntegerQueue;
        MIPSR10KSim.freeList = freeList;
        MIPSR10KSim.rmap = rmap;
        for (Integer i: markbusy) {
            MIPSR10KSim.BusyBits[i]=true;
            System.out.println("Register marked as busy: "+i);
        }
    }
}
