package com.spike.giantdataanalysis.sequences.core.locking;

import com.spike.giantdataanalysis.sequences.core.support.FieldSwapableObject;

/**
 * Exclusive Semaphore abstraction.
 * <p>
 * list of process cb, the last one hold the semaphore, others wait using FIFO
 */
public class xsemaphore implements FieldSwapableObject<Long> {

  public final String id = "1234";// Hashing.sha1().hashLong(new Date().getTime()).toString();
  public PCB pcb;

  public xsemaphore(PCB pcb) {
    this.pcb = pcb;
  }

  public static xsemaphore of(PCB pcb) {
    return new xsemaphore(pcb);
  }

  @Override
  public String toString() {
    return "xsemaphore[id=" + id + ", pcb=" + (pcb == null ? "NULL" : pcb.toString()) + "]";
  }

  @Override
  public Long out() {
    if (pcb != null) {
      return pcb.out();
    }

    return null;
  }

  @Override
  public void in(Long object) {
    if (pcb != null) {
      pcb.in(object);
    }
  }
}
