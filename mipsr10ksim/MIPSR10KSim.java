/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim;

import mipsr10ksim.datastructures.Instruction;
import mipsr10ksim.utils.TraceParser;
import java.util.ArrayList;
import java.util.HashMap;
import mipsr10ksim.datastructures.ActiveListEntry;
import mipsr10ksim.datastructures.BranchStack;
import mipsr10ksim.datastructures.FreeList;
import mipsr10ksim.datastructures.QueueEntry;
import mipsr10ksim.datastructures.RegisterMap;
import mipsr10ksim.utils.BoundedQueue;
import mipsr10ksim.utils.State;

/**
 *
 * @author sridhar
 */
public class MIPSR10KSim {
    
    private static String inputFile = "/home/sridhar/NetBeansProjects/MIPSR10KSim/src/mipsr10ksim/traces/t5.txt";
    public static int PC=0;
    public static BoundedQueue<Instruction> InstructionBuffer = new BoundedQueue<Instruction>(8);
    public static ArrayList<Instruction> instructions;
    public static BoundedQueue<ActiveListEntry> ActiveList = new BoundedQueue<>(32);
    public static BoundedQueue<QueueEntry> IntegerQueue = new BoundedQueue<>(32);
    public static BoundedQueue<QueueEntry> FloatingPointQueue = new BoundedQueue<>(32);
    public static BoundedQueue<QueueEntry> AddressQueue = new BoundedQueue<>(32);
    public static BoundedQueue<QueueEntry> CommitBuffer = new BoundedQueue<>(64);
    public static BranchStack branchStack = new BranchStack(4);
    public static FreeList freeList = new FreeList();
    public static RegisterMap rmap = new RegisterMap();
    public static boolean BusyBits[] = new boolean[64]; 
    public static int clock=0;
    public static boolean exception=false;
    public static mipsr10ksim.utils.State State=new State();
    public static int offset=0;
    public static int mispredictPC=Integer.MAX_VALUE;
    public static int offsetTemp=0;
    public static HashMap<Integer, Instruction> decodeMap = new HashMap<>();
    public static int num_issue=4;
    public static int num_commits=4;
    public static boolean flag=false;
    /**
     * @param args the command line arguments
     */
    public static void printSystemState() {
        System.out.println("Instruction Buffer: ");
        for (Instruction i: InstructionBuffer) {
            System.out.println(i);
        }
        System.out.println("Active List: ");
        for (ActiveListEntry ae: MIPSR10KSim.ActiveList) {
            System.out.println(ae.toString());
        }
        System.out.println("Integer queues: ");
        for (QueueEntry qe: IntegerQueue) {
            System.out.println(qe.getInstruction());
        }
        System.out.println("Floating Point queues: ");
        for (QueueEntry qe: FloatingPointQueue) {
            System.out.println(qe.getInstruction());
        }
        System.out.println("Address queues: ");
        for (QueueEntry qe: AddressQueue) {
            System.out.println(qe.getInstruction());
        }
        System.out.println("Commit Buffer: ");
        for (QueueEntry qe: CommitBuffer) {
            System.out.println(qe.getInstruction());
        }
        System.out.println("PC: "+PC);
        /*System.out.println("Free List");
        for(Integer i: freeList.pregisters) {
            System.out.println(i);
        }*/
    }
    
    public static void clearDataStructures() {
        MIPSR10KSim.InstructionBuffer.clear();
    }
    
    public static void calc() {
        mipsr10ksim.stages.Fetch.calc();
        mipsr10ksim.stages.Decode.calc();
        mipsr10ksim.stages.Issue.calc();
        mipsr10ksim.stages.IntegerUnit.calc();
        mipsr10ksim.stages.FloatingPointUnit.calc();
        mipsr10ksim.stages.MemoryUnit.calc();    
        mipsr10ksim.stages.Commit.calc();
    }
    public static void edge() {
        mipsr10ksim.stages.Fetch.edge();
        mipsr10ksim.stages.Decode.edge();
        mipsr10ksim.stages.Issue.edge();
        mipsr10ksim.stages.IntegerUnit.edge();
        mipsr10ksim.stages.FloatingPointUnit.edge();
        mipsr10ksim.stages.MemoryUnit.edge();
        mipsr10ksim.stages.Commit.edge();
    }
    public static void main(String[] args) {
        // TODO code application logic here
        if(args.length>0) {
            inputFile = args[0];
        }
        instructions = TraceParser.ParseFile(inputFile);
        for (; clock<1000; clock++) {
            //System.out.println("PC: "+MIPSR10KSim.PC);
            calc();
            edge();
            if(flag==true) {
                flag=false;
            }
            if(exception ==true){
                offset = offsetTemp;
                exception=false;
                mispredictPC = Integer.MAX_VALUE;
                flag=true;
            }
        }
        State.printState();
    }
    
}
