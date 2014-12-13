/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim;

import mipsr10ksim.datastructures.Instruction;
import mipsr10ksim.utils.TraceParser;
import java.util.ArrayList;
import mipsr10ksim.datastructures.ActiveListEntry;
import mipsr10ksim.datastructures.FreeList;
import mipsr10ksim.datastructures.QueueEntry;
import mipsr10ksim.datastructures.RegisterMap;
import mipsr10ksim.utils.BoundedQueue;

/**
 *
 * @author sridhar
 */
public class MIPSR10KSim {
    
    private static String inputFile = "/home/sridhar/NetBeansProjects/MIPSR10KSim/src/mipsr10ksim/trace.txt";
    public static int PC=0;
    public static BoundedQueue<Instruction> InstructionBuffer = new BoundedQueue<Instruction>(8);
    public static ArrayList<Instruction> instructions = TraceParser.ParseFile(inputFile);
    public static BoundedQueue<ActiveListEntry> ActiveList = new BoundedQueue<>(32);
    public static BoundedQueue<QueueEntry> IntegerQueue = new BoundedQueue<>(32);
    public static BoundedQueue<QueueEntry> FloatingPointQueue = new BoundedQueue<>(32);
    public static BoundedQueue<QueueEntry> AddressQueue = new BoundedQueue<>(32);
    public static FreeList freeList = new FreeList();
    public static RegisterMap rmap = new RegisterMap();
    public static boolean BusyBits[] = new boolean[64]; 
    /**
     * @param args the command line arguments
     */
    public static void calc() {
        mipsr10ksim.stages.Fetch.calc();
        mipsr10ksim.stages.Decode.calc();
    }
    public static void edge() {
        mipsr10ksim.stages.Fetch.edge();
        mipsr10ksim.stages.Decode.edge();
    }
    public static void main(String[] args) {
        // TODO code application logic here

        for (int i=0; i<30; i++) {
            calc();
            edge();
        }
    }
}
