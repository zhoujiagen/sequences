package com.spike.giantdataanalysis.sequences.rm.file;

import com.spike.giantdataanalysis.sequences.commons.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.rm.file.core.ACCESSMODE;
import com.spike.giantdataanalysis.sequences.rm.file.core.log.LSN;
import com.spike.giantdataanalysis.sequences.rm.file.core.log.LogRecord;
import com.spike.giantdataanalysis.sequences.rm.file.fs.JavaFileSystem;

/**
 * Log manager.
 */
public interface ILogM {
  class Configuration {
    public String directory1;
    public String directory2;
    public String filePreix = "LOG";
    public int nameLength = 10;
    public IFS fileSystem = new JavaFileSystem();

    public long lsnCacheThreshold = Long.MAX_VALUE;

    public int logRecordSizeInOneFile = 10000;
  }

  boolean logtable_open(ACCESSMODE accessMode);

  void logtable_close();

  long log_read_lsn(LSN lsn, OutParameter<LogRecord> header, long offset, byte[] buffer);

  LSN log_max_lsn();

  void panic();

  long c_count(int rmid);

  LSN log_insert(int rmid, int txnid, byte[] body);

  LSN log_flush(LSN lsn, boolean lazy);
}
