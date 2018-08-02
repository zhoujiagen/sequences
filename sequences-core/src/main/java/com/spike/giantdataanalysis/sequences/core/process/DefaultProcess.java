package com.spike.giantdataanalysis.sequences.core.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.sequences.core.cb.PCB;

/**
 * Thread based Process implementation.
 */
public final class DefaultProcess {
  private static final Logger LOG = LoggerFactory.getLogger(DefaultProcess.class);

  private Thread thread;
  private final Object monitor = new Object();
  private volatile boolean isRunnable = true;

  DefaultProcess() {
  }

  DefaultProcess(Runnable runnable) {
    thread = new Thread(runnable);
    ProcessManager.MyPCB(thread.getId(), new PCB(thread.getId()));
  }

  public static long MyPID() {
    return currentProcess().id();
  }

  public static PCB MyPCB() {
    return ProcessManager.MyPCB(MyPID());
  }

  public static void MyPCB(PCB newPCB) {
    if (newPCB.pid != MyPID()) {
      throw new RuntimeException("invalid operation: cross process inner state update!");
    }
    ProcessManager.MyPCB(MyPID(), newPCB);
  }

  public static DefaultProcess currentProcess() {
    DefaultProcess dp = new DefaultProcess();
    dp.thread = Thread.currentThread();
    return dp;
  }

  public static class DefaultProcessWrapper {
  }

  public long id() {
    return thread.getId();
  }

  public void start() {
    thread.start();
  }

  public void join() {
    try {
      thread.join();
    } catch (InterruptedException e) {
      LOG.error("", e);
      join();
    }
  }

  void _wait() {
    LOG.debug(DefaultProcess.currentProcess().id() + " waiting...");
    isRunnable = false;

    try {
      synchronized (monitor) {

        while (!isRunnable) {
          monitor.wait();
        }
        LOG.debug(DefaultProcess.currentProcess().id() + " wakeuped");
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  void _wakeup() {
    LOG.debug(DefaultProcess.currentProcess().id() + " wakeup others");
    isRunnable = true;

    synchronized (monitor) {
      monitor.notify();
    }
  }

  @Override
  public String toString() {
    return "Process[" + this.id() + "," + isRunnable + "]";
  }
}
