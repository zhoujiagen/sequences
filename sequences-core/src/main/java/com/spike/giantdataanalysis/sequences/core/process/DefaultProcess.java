package com.spike.giantdataanalysis.sequences.core.process;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.sequences.core.locking.PCB;

/**
 * Thread based Process implementation.
 */
public final class DefaultProcess {
  private static final Logger LOG = LoggerFactory.getLogger(DefaultProcess.class);

  private Thread thread;
  private final Object monitor = new Object();
  private volatile boolean isRunnable = true;

  // PCB holder
  private static final Map<Long, PCB> pcbs = Maps.newConcurrentMap();

  DefaultProcess() {
  }

  DefaultProcess(Runnable runnable) {
    thread = new Thread(runnable);
    pcbs.put(thread.getId(), new PCB(thread.getId()));
  }

  public static long MyPID() {
    return currentProcess().id();
  }

  public static PCB MyPCB() {
    return pcbs.get(MyPID());
  }

  public static void MyPCB(PCB newPCB) {
    pcbs.put(MyPID(), newPCB);
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
    return "Process[id=" + this.id() + ", pcb=" + pcbs.get(MyPID()) + ", isRunnable=" + isRunnable
        + "]";
  }
}
