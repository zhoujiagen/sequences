package com.spike.giantdataanalysis.sequences.rm.file.core.log;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LogAnchor {
  public String tablename;
  public LogFiles log_files;
  public ReadWriteLock lock = new ReentrantReadWriteLock(true); // end write lock
  public LSN prev_lsn;
  public LSN lsn;
  public LSN durable_lsn;
  public LSN TM_anchor_lsn;

  public LogFilePartition[] part; // opened file partition

  public LogAnchor() {
  }

}
