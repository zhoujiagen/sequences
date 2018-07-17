package com.spike.giantdataanalysis.sequences.locking;

import com.spike.giantdataanalysis.sequences.locking.core.xsemaphore;

/**
 * Exclusive Semaphore.
 */
public interface IXSem {

  void Xsem_init(xsemaphore sem);

  void Xsem_get(xsemaphore sem);

  void Xsem_give(xsemaphore sem);
}
