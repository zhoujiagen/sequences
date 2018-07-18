package com.spike.giantdataanalysis.sequences.core.support;

import com.spike.giantdataanalysis.sequences.core.locking.PCB;

public class TestICJavaAdapter {

  public static void main(String[] args) {
    pointerList();
  }

  static void pointerList() {
    PCB head = PCB.NULL();
    
    PCB pcb1 = PCB.NULL();
    pcb1.pid = 1;
    pcb1.sem_wait = head;

    PCB pcb2 = PCB.NULL();
    pcb2.pid = 2;
    pcb2.sem_wait = pcb1;
    
    System.out.println(PCB.NULL());
    System.out.println(pcb2);
  }
}
