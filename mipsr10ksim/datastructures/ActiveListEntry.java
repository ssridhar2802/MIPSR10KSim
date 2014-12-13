/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.datastructures;

/**
 *
 * @author sridhar
 */
public class ActiveListEntry {
    static int counter=0;
    
    int entrynum;
    char instructiontype;
    int archregister;
    int physregister;
    int oldmapping;
    
    
    public ActiveListEntry() {
        counter = (++counter)%32;
        entrynum = counter;
    }
    public ActiveListEntry(char type, int aregister, int pregister, int oldmapping) {
        this.instructiontype = type;
        this.archregister = aregister;
        this.physregister = pregister;
        this.oldmapping = oldmapping;
        counter = (++counter)%32;
        this.entrynum = counter;    
    }
    
    public int getEntryNum() {
        return entrynum;
    }
    @Override
    public String toString() {
        String outputString = String.format("%c %X %X %X %X", this.instructiontype, this.archregister, this.physregister, this.oldmapping, this.entrynum);
        return outputString;
    }
}
