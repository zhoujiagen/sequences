package com.spike.giantdataanalysis.sequences.core.locking;

import com.spike.giantdataanalysis.sequences.core.support.FieldSwapableObject;

/**
 * Process Control Block.
 */
public class PCB implements FieldSwapableObject<Long> {

  public static final long INVALID_PID = -1;
  public long pid = INVALID_PID; // process id
  public PCB sem_wait = null; // wait for this to finish

  public PCB(long pid) {
    this.pid = pid;
  }

  public static PCB NULL() {
    return new PCB(INVALID_PID);
  }

  @Override
  public String toString() {
    return "PCB[pid=" + pid + ", sem_wait=" + //
        (sem_wait == null ? "NULL" : sem_wait)//
        + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (pid ^ (pid >>> 32));
    result = prime * result + ((sem_wait == null) ? 0 : sem_wait.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    PCB other = (PCB) obj;
    if (pid != other.pid) return false;
    if (sem_wait == null) {
      if (other.sem_wait != null) return false;
    } else if (!sem_wait.equals(other.sem_wait)) return false;
    return true;
  }

  @Override
  public Long out() {
    return pid;
  }

  @Override
  public void in(Long object) {
    this.pid = object != null ? object : INVALID_PID;
  }

}
