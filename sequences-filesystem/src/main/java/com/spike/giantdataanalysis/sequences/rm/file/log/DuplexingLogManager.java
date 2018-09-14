package com.spike.giantdataanalysis.sequences.rm.file.log;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.primitives.Bytes;
import com.spike.giantdataanalysis.sequences.core.file.log.LSN;
import com.spike.giantdataanalysis.sequences.core.file.log.LogAnchor;
import com.spike.giantdataanalysis.sequences.core.file.log.LogRecord;
import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.exception.LogManagerException;
import com.spike.giantdataanalysis.sequences.filesystem.IFileSystem;
import com.spike.giantdataanalysis.sequences.filesystem.core.FileAccessModeEnum;
import com.spike.giantdataanalysis.sequences.filesystem.core.FileBlockEntity;
import com.spike.giantdataanalysis.sequences.filesystem.core.FileEntity;
import com.spike.giantdataanalysis.sequences.rm.file.ILogM;

/**
 * Duplexing log manager.
 * <p>
 * TODO(zhoujiagen) ___ restart here!!!
 */
public class DuplexingLogManager implements ILogM {
  private static final Logger LOG = LoggerFactory.getLogger(DuplexingLogManager.class);

  private final Configuration configuration;

  private final IFileSystem fileSystem;
  private volatile FileEntity logfile_a;
  private volatile FileEntity logfile_b;
  private volatile LSN current_lsn_a = LSN.NULL;

  // other log flush strategy???
  private final int logRecordSizeInOneFile;
  private volatile int currentLogRecordSize = 0;
  private final ReadWriteLock fileloopLock = new ReentrantReadWriteLock(true);

  // TODO(zhoujiagen) just a container, should use threshold to flush out
  private final long lsnCacheThreshold;
  private final Cache<Integer, LSN> lsnCache;

  private LogAnchor logAnchor;

  public DuplexingLogManager(Configuration configuration) {
    this.configuration = configuration;
    fileSystem = configuration.fileSystem;
    logAnchor = new LogAnchor();

    initializeFile();

    logRecordSizeInOneFile = configuration.logRecordSizeInOneFile;
    lsnCacheThreshold = configuration.lsnCacheThreshold;
    lsnCache = CacheBuilder.newBuilder()//
        .initialCapacity(100).maximumSize(lsnCacheThreshold).build();
  }

  private void initializeFile() {
    final String filePreixA = configuration.filePreix + "_A";
    final String filePreixB = configuration.filePreix + "_B";
    File dira = Paths.get(configuration.directory1).toFile();
    if (!dira.exists()) {
      throw LogManagerException.newE(configuration.directory1 + " does not exist!");
    }
    File dirb = Paths.get(configuration.directory2).toFile();
    if (!dira.exists()) {
      throw LogManagerException.newE(configuration.directory2 + " does not exist!");
    }
    int lastA = dira.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return dir.isDirectory() && name.startsWith(filePreixA);
      }
    }).length;
    int lastB = dirb.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return dir.isDirectory() && name.startsWith(filePreixB);
      }
    }).length;

    if (lastA != lastB) {
      if (lastA > lastB) {
        lastB = lastA;
      } else if (lastA < lastB) {
        lastA = lastB;
      }
    }
    lastA = Math.max(0, lastA - 1);
    lastB = Math.max(0, lastB - 1);

    String logfile_a_name = configuration.directory1 + filePreixA
        + Strings.padStart(String.valueOf(lastA), configuration.nameLength, '0');
    if (Paths.get(logfile_a_name).toFile().exists()) {
      logfile_a = fileSystem.open(logfile_a_name, FileAccessModeEnum.A);

      // find last lsn to set current lsn
      LSN lsn = new LSN(logfile_a.getNumber(), 0);
      FileBlockEntity res = fileSystem.readLine(logfile_a, 0);
      while (res.getData() != null || res.getData().length != 0) {
        String contentsString = new String(res.getData());
        if (LOG.isDebugEnabled()) {
          LOG.debug(contentsString);
        }
        lsn = LogRecord.NULL.fromString(contentsString).lsn;
        res = fileSystem.readLine(logfile_a, 0);
      }
      current_lsn_a = lsn;
    } else {
      fileSystem.create(logfile_a_name, null);
      logfile_a = fileSystem.open(logfile_a_name, FileAccessModeEnum.A);
    }

    String logfile_b_name = configuration.directory1 + filePreixB
        + Strings.padStart(String.valueOf(lastB), configuration.nameLength, '0');
    if (!Paths.get(logfile_b_name).toFile().exists()) {
      fileSystem.create(logfile_b_name, null);
    }
    logfile_b = fileSystem.open(logfile_b_name, FileAccessModeEnum.A);
  }

  @Override
  public boolean logtable_open(FileAccessModeEnum accessMode) {
    Preconditions.checkArgument(accessMode != null);
    fileSystem.open(logfile_a.getName(), accessMode);
    fileSystem.open(logfile_b.getName(), accessMode);

    return true;
  }

  @Override
  public synchronized void logtable_close() {
    fileSystem.close(logfile_a);
    fileSystem.close(logfile_b);
  }

  @Override
  public long log_read_lsn(LSN lsn, OutParameter<LogRecord> header, long offset, byte[] buffer) {

    FileBlockEntity res = fileSystem.readLine(logfile_a, 0);
    LogRecord logRecord = LogRecord.NULL;
    logRecord = logRecord.fromString(new String(res.getData()));
    if (logRecord.lsn.equals(lsn)) {
      header.setValue(logRecord);
    }

    return 0L;
  }

  @Override
  public LSN log_max_lsn() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void panic() {
    throw new UnsupportedOperationException();
  }

  @Override
  public long c_count(int rmid) {
    if (!logtable_open(FileAccessModeEnum.R)) panic();

    long rec_count = 0;

    OutParameter<LogRecord> header = new OutParameter<>();
    byte[] buffer = new byte[1];
    LSN lsn = log_max_lsn();
    while (!LSN.NULL.equals(lsn)) {
      log_read_lsn(lsn, header, 0, buffer);
      if (header.value().rmid == rmid) {
        rec_count++;
      }
      lsn = header.value().prev_lsn;
    }
    logtable_close();
    return rec_count;
  }

  @Override
  public LSN log_insert(int rmid, int txnid, byte[] body) {

    Lock lock = fileloopLock.writeLock();
    lock.lock();
    if (currentLogRecordSize == this.logRecordSizeInOneFile) {
      // TODO(zhoujiagen) create another file
    }
    lock.unlock();

    // save to cache
    LogRecord logRecord = new LogRecord();
    logRecord.lsn = new LSN(current_lsn_a.file, current_lsn_a.rba);
    logRecord.prev_lsn = LSN.NULL;
    logRecord.timestamp = new Date().getTime();
    logRecord.rmid = rmid;
    logRecord.txnid = txnid;
    logRecord.txn_prev_lsn = LSN.NULL;
    logRecord.length = body.length;
    logRecord.body = body;

    current_lsn_a = new LSN(current_lsn_a.file, logRecord.lsn.rba + current_lsn_a.size());

    byte[] data = Bytes.concat(logRecord.asString().getBytes(), System.lineSeparator().getBytes());
    fileSystem.write(logfile_a, 0, data);
    fileSystem.write(logfile_b, 0, data);

    currentLogRecordSize++;

    return logRecord.lsn;
  }

  @Override
  public LSN log_flush(LSN lsn, boolean lazy) {

    // flush cache to file system

    if (!lazy) {
      fileSystem.flush(logfile_a);
      fileSystem.flush(logfile_b);
    }
    return lsn;
  }
}
