package com.spike.giantdataanalysis.sequences.locking;

import com.spike.giantdataanalysis.sequences.core.locking.PCB;
import com.spike.giantdataanalysis.sequences.core.locking.xsemaphore;
import com.spike.giantdataanalysis.sequences.core.process.DefaultProcess;
import com.spike.giantdataanalysis.sequences.core.process.DefaultProcessRunnable;
import com.spike.giantdataanalysis.sequences.core.process.ProcessFactory;
import com.spike.giantdataanalysis.sequences.core.process.ProcessManager;

public class TestIXSem {
  public static void main(String[] args) {
    System.out.println(Thread.currentThread().getId());

    final IXSem xSem = new DefaultXSemaphore();

    Thread snapshotThread = new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          System.out.println("\t" + ProcessManager.snapshot());
          try {
            Thread.sleep(2000L);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });
    snapshotThread.start();

    final xsemaphore sem = new xsemaphore(PCB.NULL());
    xSem.Xsem_init(sem);

    DefaultProcessRunnable execution = new DefaultProcessRunnable() {
      @Override
      public void doRun() {
        long mypid = DefaultProcess.MyPID();

        xSem.Xsem_get(sem);
        System.out.println(mypid + " got sem: " + sem + ", " + DefaultProcess.MyPCB());

        try {
          Thread.sleep(5000L);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        xSem.Xsem_give(sem);
        System.out.println(mypid + " gave sem: " + sem + ", " + DefaultProcess.MyPCB());
      }
    };

    DefaultProcess process1 = ProcessFactory.one(execution);
    DefaultProcess process2 = ProcessFactory.one(execution);

    process1.start();
    process2.start();

    // join
    process1.join();
    process2.join();
    try {
      snapshotThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
