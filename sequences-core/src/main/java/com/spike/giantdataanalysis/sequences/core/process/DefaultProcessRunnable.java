package com.spike.giantdataanalysis.sequences.core.process;

public abstract class DefaultProcessRunnable implements Runnable {

  @Override
  public void run() {
    doRun();
    ProcessManager.unregister(DefaultProcess.currentProcess().id());
  }

  public abstract void doRun();
}
