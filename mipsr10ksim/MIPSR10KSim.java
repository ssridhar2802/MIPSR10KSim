/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim;

import mipsr10ksim.utils.TraceParser;
import java.util.ArrayList;
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
    public static BoundedQueue<Instruction> ActiveList = new BoundedQueue<>(32);
    public static BoundedQueue<Instruction> IntegerQueue = new BoundedQueue<>(32);
    public static BoundedQueue<Instruction> FloatingPointQueue = new BoundedQueue<>(32);
    public static BoundedQueue<Instruction> AddressQueue = new BoundedQueue<>(32);
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
        for (int i=0; i<2; i++) {
            calc();
            edge();
        }
        for (Instruction i: FloatingPointQueue) {
            System.out.println(i);
        }
    }
    
}
