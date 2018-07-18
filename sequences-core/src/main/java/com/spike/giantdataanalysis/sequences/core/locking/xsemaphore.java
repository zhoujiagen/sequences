package com.spike.giantdataanalysis.sequences.core.locking;

import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.CPointer;

/**
 * Exclusive Semaphore abstraction.
 * <p>
 * list of process cb, the last one hold the semaphore, others wait using FIFO
 */
public class xsemaphore extends CPointer<PCB> {

  public xsemaphore(PCB pcb) {
    this.value = pcb;
  }

  public static xsemaphore of(PCB pcb) {
    return new xsemaphore(pcb);
  }

}
