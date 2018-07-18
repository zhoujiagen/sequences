package com.spike.giantdataanalysis.sequences.core.process;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public abstract class ProcessManager {

  private static final Logger LOG = LoggerFactory.getLogger(ProcessManager.class);

  private static Map<Long, DefaultProcess> processes = Maps.newConcurrentMap();
  private static AtomicLong maxPID = new AtomicLong(0L);

  public static long maxPID() {
    return maxPID.get();
  }

  public static void register(DefaultProcess process) {
    if (processes.containsKey(process.id())) {
      throw new RuntimeException("process[" + process.id() + "] already exists!");
    }
    LOG.debug("register Process {}", process.id());

    processes.put(process.id(), process);
    maxPID.incrementAndGet();
  }

  public static void register(long pid, Runnable runnable) {
    if (processes.containsKey(pid)) {
      throw new RuntimeException("process[" + pid + "] already exists!");
    }

    LOG.debug("register Process {}", pid);
    processes.put(pid, new DefaultProcess(runnable));
    maxPID.incrementAndGet();
  }

  public static DefaultProcess unregister(long pid) {
    LOG.debug("unregister Process {}", pid);

    DefaultProcess result = processes.remove(pid);
    return result;
  }

  public static DefaultProcess query(long pid) {
    return processes.get(pid);
  }

  public static void _wait(long pid) {
    LOG.debug("make Process {} to wait", pid);

    DefaultProcess process = query(pid);
    if (process == null) {
      throw new RuntimeException("Process[" + pid + "] dose not exist!");
    }

    process._wait();
  }

  public static void _wakeup(long pid) {
    LOG.debug("wakeup Process {}", pid);

    DefaultProcess process = query(pid);
    if (process == null) {
      throw new RuntimeException("Process[" + pid + "] dose not exist!");
    }

    process._wakeup();
  }

  public static String snapshot() {

    StringBuilder sb = new StringBuilder();
    sb.append(DefaultProcess.currentProcess().id());
    sb.append(", maxPID=").append(maxPID());
    sb.append(", processes=").append(processes);

    return sb.toString();
  }

}
