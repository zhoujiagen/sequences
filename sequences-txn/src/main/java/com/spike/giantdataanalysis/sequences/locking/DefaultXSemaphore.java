package com.spike.giantdataanalysis.sequences.locking;

import com.spike.giantdataanalysis.sequences.commons.NativeOps;
import com.spike.giantdataanalysis.sequences.locking.core.PCB;
import com.spike.giantdataanalysis.sequences.locking.core.xsemaphore;

public class DefaultXSemaphore implements IXSem {

  @Override
  public void Xsem_init(xsemaphore sem) {
    sem.value(null);
  }

  @Override
  public void Xsem_get(xsemaphore sem) {
    xsemaphore _new = xsemaphore.of(PCB.MyPCB());
    xsemaphore old = xsemaphore.of(null);

    do {
      _new.value().sem_wait = old.value();
    } while (!NativeOps.CSO(sem, old, _new));

    if (old.value() != null) {
      PCB._wait();
    }
  }

  @Override
  public void Xsem_give(xsemaphore sem) {
    xsemaphore _new = xsemaphore.of(null);
    xsemaphore old = xsemaphore.of(PCB.MyPCB());
    if (NativeOps.CSO(sem, old, _new)) {
      return;
    }

    while (PCB.MyPCB().equals(old.value().sem_wait)) {
      old.value(old.value().sem_wait);
    }
    old.value().sem_wait = null;
    PCB._wakeup(old.value());
  }

}
