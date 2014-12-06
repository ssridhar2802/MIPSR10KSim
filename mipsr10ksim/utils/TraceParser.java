/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import mipsr10ksim.Instruction;

/**
 *
 * @author sridhar
 */
public class TraceParser {
    
    public static ArrayList<Instruction> ParseFile(String inputFile) {
        
        File f=new File(inputFile);
        Scanner sc=null;
        try {
            sc= new Scanner(f);
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<Instruction> instructions = new ArrayList<Instruction>();
            while (sc.hasNextLine()) {
                
                String line = sc.nextLine();
                //System.out.println(line);
                Instruction ins = new Instruction();
                String[] columns = line.split(" ");
                
                for (int i=0; i<columns.length; i++) {
                    if (columns[i].equals("xx"))
                        continue;
                    if (i==0) {
                        ins.setOpcode(columns[i].charAt(0));
                    }
                    else if (i==1) {
                        ins.setRs(Integer.parseInt(columns[1], 16));
                    }
                    else if (i==2) {
                        ins.setRt(Integer.parseInt(columns[2], 16));
                    }
                    else if (i==3) {
                        ins.setRd(Integer.parseInt(columns[3],16));
                    }
                    else if (i==4) {
                        ins.setExtra(Long.parseLong(columns[4], 16));
                    }
                }
                instructions.add(ins);
                //System.out.println(ins);
            }
            return instructions;
    }
    public static void main(String args[]) {
        String inputFile = "/home/sridhar/NetBeansProjects/MIPSR10KSim/src/mipsr10ksim/trace.txt";
        ParseFile(inputFile);
        
    }
}
