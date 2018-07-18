package com.spike.giantdataanalysis.sequences.core.locking;

import com.spike.giantdataanalysis.sequences.core.locking.lock.LOCK_MODE;

/**
 * Semaphore: share, exclusive.
 */
public class semaphore {
  public LOCK_MODE mode;
  public int count; // 0 free, 1 exclusive, >=1 share
  public PCB wait_list;
}
