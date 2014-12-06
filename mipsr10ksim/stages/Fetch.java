/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.stages;

import java.util.ArrayList;
import java.util.List;
import mipsr10ksim.Instruction;
import mipsr10ksim.MIPSR10KSim;

/**
 *
 * @author sridhar
 */
public class Fetch {
    private static List<Instruction> instructions;
    
    public static void calc() {
        int upperPC;
        if (MIPSR10KSim.PC > MIPSR10KSim.instructions.size())
            return;
        if (MIPSR10KSim.PC + 4 > MIPSR10KSim.instructions.size()) {
            upperPC = instructions.size();
        }
        else {
            upperPC = MIPSR10KSim.PC+4;
        }
        instructions = MIPSR10KSim.instructions.subList(MIPSR10KSim.PC, upperPC);
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
