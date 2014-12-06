/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.stages;
import mipsr10ksim.Instruction;
import mipsr10ksim.MIPSR10KSim;
/**
 *
 * @author sridhar
 */
public class Decode {
    //int counter;
    public static void calc() {
    }
    public static void edge() {
        for (int i=0; i<4; i++) {
            Instruction instruction = MIPSR10KSim.InstructionBuffer.getFirst();
            char instructionType = instruction.getOpcode();
            if (!MIPSR10KSim.ActiveList.hasSpace())
                break;
            if (instructionType == 'L' || instructionType == 'S') {
                if(MIPSR10KSim.AddressQueue.hasSpace()) {
                    MIPSR10KSim.AddressQueue.add(instruction);
                }
                else
                    break;
            }
            else if (instructionType == 'I' || instructionType == 'B') {
                if(MIPSR10KSim.IntegerQueue.hasSpace()) {
                    MIPSR10KSim.IntegerQueue.add(instruction);
                }
                else
                    break;
            }
            else if (instructionType == 'A' || instructionType == 'M') {
                if(MIPSR10KSim.FloatingPointQueue.hasSpace()) {
                    MIPSR10KSim.FloatingPointQueue.add(instruction);
                }
                else
                    break;
            }
        MIPSR10KSim.ActiveList.add(instruction);
        }
    }
}
