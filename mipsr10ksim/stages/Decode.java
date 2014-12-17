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
import mipsr10ksim.datastructures.ActiveListEntry;
import mipsr10ksim.datastructures.FreeList;
import mipsr10ksim.datastructures.QueueEntry;
import mipsr10ksim.datastructures.RegisterMap;
import mipsr10ksim.utils.BoundedQueue;
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
    
    public static Instruction remapInstructions(Instruction i) {
        i.setRd(rmap.getMapping(i.getRd()));
        i.setRt(rmap.getMapping(i.getRt()));
        i.setRs(rmap.getMapping(i.getRs()));
        return i;
    } 
    
    public static void calc() {
        //TODO: Map source operands before destination operands
        
        rmap = MIPSR10KSim.rmap;
        ActiveList = MIPSR10KSim.ActiveList;
        IntegerQueue = MIPSR10KSim.IntegerQueue;
        FloatingPointQueue = MIPSR10KSim.FloatingPointQueue;
        AddressQueue = MIPSR10KSim.AddressQueue;
        freeList = MIPSR10KSim.freeList;
        markbusy = new ArrayList<Integer>();
        
        for (int i=0; i<4; i++) {
            if (MIPSR10KSim.InstructionBuffer.isEmpty())
                break;
            Instruction instruction = MIPSR10KSim.InstructionBuffer.removeFirst();
            char instructionType = instruction.getOpcode();
            if (!ActiveList.hasSpace())
                break;
            
            int destinationregister = instruction.getDestinationRegister();
            
            ActiveListEntry ae = null;
            if(destinationregister != -1) {
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
            instruction = remapInstructions(instruction);
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
                if(IntegerQueue.hasSpace()) {
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
        }
    }
}
