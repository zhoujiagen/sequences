package com.spike.giantdataanalysis.sequences.locking;

import com.spike.giantdataanalysis.sequences.commons.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.locking.core.lock.LOCK_MODE;
import com.spike.giantdataanalysis.sequences.locking.core.semaphore;

/**
 * Semaphore: share or exclusive.
 */
public interface ISem {

  void sem_get(OutParameter<semaphore> sem, LOCK_MODE mode);

  void sem_give(semaphore sem);
}
