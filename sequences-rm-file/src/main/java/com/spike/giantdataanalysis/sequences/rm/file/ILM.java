package com.spike.giantdataanalysis.sequences.rm.file;

import com.spike.giantdataanalysis.sequences.commons.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.rm.file.core.AccessMode;
import com.spike.giantdataanalysis.sequences.rm.file.core.log.LSN;
import com.spike.giantdataanalysis.sequences.rm.file.core.log.LogRecord;
import com.spike.giantdataanalysis.sequences.rm.file.fs.JavaFileSystem;

/**
 * Log manager.
 */
public interface ILM {
  class Configuration {
    public String directory1;
    public String directory2;
    public String filePreix = "LOG";
    public int nameLength = 10;
    public IFileSystem fileSystem = new JavaFileSystem();

    public long lsnCacheThreshold = Long.MAX_VALUE;
  }

  class LogManagerException extends RuntimeException {
    private static final long serialVersionUID = 5225858589672335027L;

    public static LogManagerException newE() {
      return new LogManagerException();
    }

    public static LogManagerException newE(String message) {
      return new LogManagerException(message);
    }

    public static LogManagerException newE(String message, Throwable cause) {
      return new LogManagerException(message, cause);
    }

    public static LogManagerException newE(Throwable cause) {
      return new LogManagerException(cause);
    }

    public static LogManagerException newE(String message, Throwable cause,
        boolean enableSuppression, boolean writableStackTrace) {
      return new LogManagerException(message, cause, enableSuppression, writableStackTrace);
    }

    public LogManagerException() {
      super();
    }

    public LogManagerException(String message) {
      super(message);
    }

    public LogManagerException(String message, Throwable cause) {
      super(message, cause);
    }

    public LogManagerException(Throwable cause) {
      super(cause);
    }

    public LogManagerException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
    }

  }

  boolean logtable_open(AccessMode accessMode);

  void logtable_close();

  long log_read_lsn(LSN lsn, OutParameter<LogRecord> header, long offset, byte[] buffer);

  LSN log_max_lsn();

  void panic();

  long c_count(int rmid);

  LSN log_insert(int rmid, int txnid, byte[] body);

  LSN log_flush(LSN lsn, boolean lazy);
}
