package com.spike.giantdataanalysis.sequences.core.locking;

import com.spike.giantdataanalysis.sequences.core.support.FieldSwapableObject;
import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.ICUpdateable;

/**
 * Exclusive Semaphore abstraction.
 * <p>
 * list of process cb, the last one hold the semaphore, others wait using FIFO
 */
public class xsemaphore implements FieldSwapableObject<Long>, ICUpdateable<PCB> {

  public final String id = "";// Hashing.sha1().hashLong(new Date().getTime()).toString();
  public PCB pcb;

  public xsemaphore(PCB pcb) {
    this.pcb = pcb;
  }

  public static xsemaphore of(PCB pcb) {
    return new xsemaphore(pcb);
  }

  // ---------------------------------------------------------------------------
  // ICUpdateable
  // ---------------------------------------------------------------------------

  @Override
  public void update(PCB t) {
    pcb.update(t);
  }

  // ---------------------------------------------------------------------------
  // FieldSwapableObject
  // ---------------------------------------------------------------------------

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

  @Override
  public String toString() {
    return "xsemaphore[id=" + id + ", pcb=" + (pcb == null ? "NULL" : pcb.toString()) + "]";
  }

}
