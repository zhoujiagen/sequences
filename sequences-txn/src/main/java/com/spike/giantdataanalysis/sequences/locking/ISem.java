package com.spike.giantdataanalysis.sequences.locking;

import com.spike.giantdataanalysis.sequences.core.locking.lock.LOCK_MODE;
import com.spike.giantdataanalysis.sequences.core.locking.semaphore;
import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.OutParameter;

/**
 * Semaphore: share or exclusive.
 */
public interface ISem {

  void sem_get(OutParameter<semaphore> sem, LOCK_MODE mode);

  void sem_give(semaphore sem);
}
