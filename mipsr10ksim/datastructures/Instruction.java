/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.datastructures;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author sridhar
 */
public class Instruction implements Serializable {
    static int autoincrement =0;
    private char opcode;
    private int rs=-1;
    private int rt=-1;
    private int rd=-1;
    private long extra=-1;
    private int PC=-1;
    private int decodeId;
    
    
    public Instruction(char opcode, int rs, int rt, int rd, long extra) {
        this.opcode = opcode;
        this.rs = rs;
        this.rt = rt;
        this.rd = rd;
        this.extra = extra;
    }
    
    public Instruction(Instruction i) {
        this.opcode = i.opcode;
        this.rs = i.rs;
        this.rt = i.rt;
        this.rd = i.rd;
        this.extra = i.extra;
        this.PC = i.PC;
        decodeId = this.autoincrement++;
    }

    public Instruction() {
       
    }

    public int getDestinationRegister() {
        if(opcode=='L') 
            return rt;
        else
            return rd;
    }
    
    public void resetAutoIncrement() {
        Instruction.autoincrement= 0;
    }
    public void setDestinationRegister(int r) {
        if(opcode=='L') 
            this.rt=r;
        else
            this.rd =r;
    }
    public int getDecodeId() {
        return decodeId;
    }
    public static int getAutoIncrement() {
        return autoincrement;
    }
    @Override
    public String toString() {
        String outputString = String.format("%c %d %d %d %d %d", getOpcode(), getRs(), getRt(), getRd(), getExtra(), getPC());
        return outputString;
    }

    /**
     * @return the opcode
     */
    public char getOpcode() {
        return opcode;
    }

    /**
     * @param opcode the opcode to set
     */
    public void setOpcode(char opcode) {
        this.opcode = opcode;
    }

    /**
     * @return the rs
     */
    public int getRs() {
        return rs;
    }

    /**
     * @param rs the rs to set
     */
    public void setRs(int rs) {
        this.rs = rs;
    }

    /**
     * @return the rt
     */
    public int getRt() {
        return rt;
    }

    /**
     * @param rt the rt to set
     */
    public void setRt(int rt) {
        this.rt = rt;
    }

    /**
     * @return the rd
     */
    public int getRd() {
        return rd;
    }

    /**
     * @param rd the rd to set
     */
    public void setRd(int rd) {
        this.rd = rd;
    }

    /**
     * @return the extra
     */
    public long getExtra() {
        return extra;
    }

    /**
     * @param extra the extra to set
     */
    public void setExtra(long extra) {
        this.extra = extra;
    }
    
    public void setPC(int PC) {
        this.PC=PC;
    }
    
    public int getPC() {
        return this.PC;
    }
}
