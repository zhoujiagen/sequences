package com.spike.giantdataanalysis.sequences.core.process;

public abstract class ProcessFactory {

  public static DefaultProcess one() {
    DefaultProcess result = new DefaultProcess();
    ProcessManager.register(result);
    return result;
  }

  public static DefaultProcess one(DefaultProcessRunnable runnable) {
    DefaultProcess result = new DefaultProcess(runnable);
    ProcessManager.register(result);
    return result;
  }
}
