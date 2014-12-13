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

/**
 *
 * @author sridhar
 */
public class Fetch {
    private static List<Instruction> instructions;
    
    public static void calc() {
        int PC = MIPSR10KSim.PC;
        instructions = new ArrayList<Instruction>();
        for(int i=0; i<4; i++) {
            if (PC >= MIPSR10KSim.instructions.size()) 
                return;
            else {
                instructions.add(MIPSR10KSim.instructions.get(PC));
                PC++;
            }
        }
    }
    public static void edge() {
        for (Instruction i: instructions) {
            if (MIPSR10KSim.InstructionBuffer.add(i)) {
                MIPSR10KSim.PC++;
            }
            else 
                break;
        }
    }
}
