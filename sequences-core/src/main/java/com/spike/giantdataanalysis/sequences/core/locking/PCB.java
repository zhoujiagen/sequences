package com.spike.giantdataanalysis.sequences.core.locking;

/**
 * process control block.
 */
public class PCB {
  public long pid; // process id
  public PCB sem_wait;

  public static long MyPID() {
    return Thread.currentThread().getId();
  }

  public static PCB MyPCB() {
    PCB result = new PCB();
    result.pid = MyPID();
    result.sem_wait = null; // ???
    return result;
  }

  public static void _wait() {
    throw new UnsupportedOperationException();
  }

  public static void _wakeup(PCB him) {
    throw new UnsupportedOperationException();
  }

}
