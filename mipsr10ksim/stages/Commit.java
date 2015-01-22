/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mipsr10ksim.stages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mipsr10ksim.MIPSR10KSim;
import mipsr10ksim.datastructures.ActiveListEntry;
import mipsr10ksim.datastructures.QueueEntry;

/**
 *
 * @author sridhar
 */
public class Commit {
    static int CommitCounter=0;
    static List<QueueEntry> toCommit;
    
    static void removeActiveListEntry(int entryNum) {
            Iterator<ActiveListEntry> iter = mipsr10ksim.MIPSR10KSim.ActiveList.iterator();
        while(iter.hasNext()) {
            ActiveListEntry ae = iter.next();
            if(ae.getEntryNum()==entryNum) {
                iter.remove();
            }
        }
    }
    
    public static void calc() {
        toCommit=new ArrayList<>();
        for(int i=CommitCounter; i<CommitCounter+MIPSR10KSim.num_commits; i++) {
            boolean done=false;
            for (QueueEntry qe: mipsr10ksim.MIPSR10KSim.CommitBuffer) {
                if(qe.getInstruction().getPC()==i) {
                    toCommit.add(qe);
                    //System.out.println("adding to commit: "+qe.getInstruction()+" : "+qe.getInstruction().getPC());
                    mipsr10ksim.MIPSR10KSim.State.addEntry(qe.getInstruction().getDecodeId(), mipsr10ksim.MIPSR10KSim.clock, "C");
                    done=true;
                    break;
                }
            }
            if(done==false) {
                break;
            }
        }
    }
    public static void edge() {
        List<QueueEntry> toRemove=new ArrayList<>();
        for(QueueEntry qe: MIPSR10KSim.CommitBuffer) {
            if(qe.getInstruction().getPC()>MIPSR10KSim.mispredictPC) {
                toRemove.add(qe);
            }
        }

        for (QueueEntry qe: toRemove) {
            MIPSR10KSim.CommitBuffer.remove(qe);
        }
       // if(mipsr10ksim.MIPSR10KSim.exception)
         //   return;
        if(toCommit==null) {
            return;
        }
        List<ActiveListEntry> removelist = new ArrayList<>();
        for(QueueEntry qe: toCommit) {
            for(ActiveListEntry ae: mipsr10ksim.MIPSR10KSim.ActiveList) {
                if(qe.getActiveListEntry()==ae.getEntryNum()) {
                    mipsr10ksim.MIPSR10KSim.freeList.addRegister(ae.getOldMapping());
                    if(qe.getInstructionType()=='L' || qe.getInstructionType()=='S') {
                        mipsr10ksim.MIPSR10KSim.AddressQueue.remove(qe);
                    }
                    //mipsr10ksim.MIPSR10KSim.ActiveList.remove(ae);
                    //removeActiveListEntry(qe.getActiveListEntry());
                    removelist.add(ae);
                    // System.out.println("Committing: "+qe.getInstruction()+" at clock cycle: "+mipsr10ksim.MIPSR10KSim.clock);
                    mipsr10ksim.MIPSR10KSim.CommitBuffer.remove(qe);
                }
            }//System.out.println("Here: "+qe.getInstruction().getPC());
            for(ActiveListEntry ae: removelist) {
                mipsr10ksim.MIPSR10KSim.ActiveList.remove(ae);
            }
            CommitCounter++;
        }
    }
}
