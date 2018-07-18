package com.spike.giantdataanalysis.sequences.locking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.sequences.core.locking.PCB;
import com.spike.giantdataanalysis.sequences.core.locking.xsemaphore;
import com.spike.giantdataanalysis.sequences.core.process.DefaultProcess;
import com.spike.giantdataanalysis.sequences.core.process.ProcessManager;
import com.spike.giantdataanalysis.sequences.core.support.NativeOps;

// FIXME(zhoujiagen) restart from here
public class DefaultXSemaphore implements IXSem {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultXSemaphore.class);

  @Override
  public void Xsem_init(xsemaphore sem) {
    LOG.debug("init sem: {}", sem);
  }

  @Override
  public void Xsem_get(xsemaphore sem) {
    LOG.debug("get sem: {}", sem);

    PCB _new = DefaultProcess.MyPCB();
    PCB old = PCB.NULL();

    do {
      _new.sem_wait = old;
    } while (!NativeOps.CSF(sem, old, _new));

    if (old.pid != DefaultProcess.MyPID()) {
      LOG.debug("{} wait on: sem={}", DefaultProcess.currentProcess().id(), sem);
      ProcessManager._wait(DefaultProcess.currentProcess().id());
    }
  }

  @Override
  public void Xsem_give(xsemaphore sem) {
    LOG.debug("give sem: {}", sem);

    PCB _new = PCB.NULL();
    PCB old = DefaultProcess.MyPCB();
    if (NativeOps.CSF(sem, old, _new)) { // the last one
      return;
    }
    // toward head
    while (old != null && !DefaultProcess.MyPCB().equals(old.sem_wait)) {
      old = old.sem_wait;
    }

    if (!PCB.NULL().equals(old) && old != null) {
      old.sem_wait = null;
      ProcessManager._wakeup(old.pid);
    }
  }

}
