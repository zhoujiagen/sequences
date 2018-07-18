package com.spike.giantdataanalysis.sequences.core.support;

import com.spike.giantdataanalysis.sequences.core.locking.PCB;
import com.spike.giantdataanalysis.sequences.core.locking.xsemaphore;

public class TestFieldSwapableObject {
  public static void main(String[] args) {
    PCB myPCB = new PCB(1L);
    xsemaphore cell = xsemaphore.of(myPCB);
    System.out.println(cell);

    PCB oldPCB = PCB.NULL();
    oldPCB.pid = myPCB.pid;
    xsemaphore old = xsemaphore.of(oldPCB);
    System.out.println(old);

    PCB _newPCB = PCB.NULL();
    _newPCB.pid = myPCB.pid + 1L;
    xsemaphore _new = xsemaphore.of(_newPCB);
    System.out.println(_new);

    System.out.println();

    boolean result = NativeOps.CSF(cell, old, _new);
    System.out.println(result);
    System.out.println(cell); // PCB [pid=2, sem_wait=null]
    System.out.println(old);
    System.out.println(_new);
  }
}
