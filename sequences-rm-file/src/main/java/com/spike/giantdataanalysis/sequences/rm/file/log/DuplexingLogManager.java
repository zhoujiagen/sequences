package com.spike.giantdataanalysis.sequences.rm.file.log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import com.google.common.primitives.Bytes;
import com.spike.giantdataanalysis.sequences.commons.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.rm.file.IFileSystem;
import com.spike.giantdataanalysis.sequences.rm.file.ILM;
import com.spike.giantdataanalysis.sequences.rm.file.core.AccessMode;
import com.spike.giantdataanalysis.sequences.rm.file.core.FILE;
import com.spike.giantdataanalysis.sequences.rm.file.core.FILEBlock;
import com.spike.giantdataanalysis.sequences.rm.file.core.log.LSN;
import com.spike.giantdataanalysis.sequences.rm.file.core.log.LogAnchor;
import com.spike.giantdataanalysis.sequences.rm.file.core.log.LogRecord;

/**
 * Duplexing log manager.
 */
public class DuplexingLogManager implements ILM {

  private final Configuration configuration;

  private volatile FILE logfile_a;
  private volatile LSN current_lsn_a = LSN.NULL;
  private volatile FILE logfile_b;
  private final IFileSystem fileSystem;
  private Set<FILE> readable = Sets.newConcurrentHashSet();
  private Set<FILE> writable = Sets.newConcurrentHashSet();

  // TODO(zhoujiagen) just a container, should use thereshold to flush out
  private final long lsnCacheThreshold;
  private final Cache<Integer, LSN> lsnCache;

  private LogAnchor logAnchor;

  public DuplexingLogManager(Configuration configuration) {
    this.configuration = configuration;
    fileSystem = configuration.fileSystem;
    logAnchor = new LogAnchor();

    initializeFile();

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

    File tempFile = new File(logfile_a_name);
    if (tempFile.exists()) {
      // find last lsn to set current lsn

    } else {
      try {
        tempFile.createNewFile();
        current_lsn_a = LSN.NULL;
      } catch (IOException e) {
        throw LogManagerException.newE(e);
      }
    }
    logfile_a = new FILE(logfile_a_name);

    String logfile_b_name = configuration.directory1 + filePreixB
        + Strings.padStart(String.valueOf(lastB), configuration.nameLength, '0');
    tempFile = new File(logfile_b_name);
    if (!tempFile.exists()) {
      try {
        tempFile.createNewFile();
      } catch (IOException e) {
        throw LogManagerException.newE(e);
      }
    }
    logfile_b = new FILE(logfile_b_name);
  }

  @Override
  public boolean logtable_open(AccessMode accessMode) {
    Preconditions.checkArgument(accessMode != null);

    OutParameter<FILE> a_FILEID = new OutParameter<>();
    a_FILEID.setValue(logfile_a);
    int resa = fileSystem.open(logfile_a.filename, accessMode, a_FILEID);
    if (IFileSystem.ReturnCode.OK.code() != resa) {
      throw LogManagerException.newE("cannot open inner files!");
    }
    OutParameter<FILE> b_FILEID = new OutParameter<>();
    b_FILEID.setValue(logfile_b);
    int resb = fileSystem.open(logfile_b.filename, accessMode, b_FILEID);
    if (IFileSystem.ReturnCode.OK.code() != resb) {
      throw LogManagerException.newE("cannot open inner files!");
    }

    if (AccessMode.R.equals(accessMode)) {
      readable.add(logfile_a);
      readable.add(logfile_b);
    } else {
      readable.add(logfile_a);
      readable.add(logfile_b);
      writable.add(logfile_a);
      writable.add(logfile_b);
    }

    return true;
  }

  @Override
  public synchronized void logtable_close() {
    for (FILE w : writable) {
      fileSystem.close(w);
    }

    readable.clear();
    writable.clear();
  }

  @Override
  public long log_read_lsn(LSN lsn, OutParameter<LogRecord> header, long offset, byte[] buffer) {

    OutParameter<FILEBlock> BLOCKP = new OutParameter<>();
    BLOCKP.setValue(new FILEBlock());
    int res = fileSystem.readLine(logfile_a, 0, BLOCKP);
    if (IFileSystem.ReturnCode.OK.code() == res) {
      throw LogManagerException.newE("read failed");
    }
    LogRecord logRecord = new LogRecord();
    logRecord = logRecord.fromBytes(BLOCKP.value().contents);
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
    if (!logtable_open(AccessMode.R)) panic();

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

    // save to cache
    LogRecord logRecord = new LogRecord();
    logRecord.lsn = new LSN(current_lsn_a.file, current_lsn_a.rba + current_lsn_a.size());
    logRecord.prev_lsn = LSN.NULL;
    logRecord.timestamp = new Date().getTime();
    logRecord.rmid = rmid;
    logRecord.txnid = txnid;
    logRecord.txn_prev_lsn = LSN.NULL;
    logRecord.length = body.length;
    logRecord.body = body;

    FILEBlock BLOCKP = new FILEBlock();
    BLOCKP.contents = logRecord.toBytes();
    BLOCKP.contents = Bytes.concat(BLOCKP.contents, System.lineSeparator().getBytes());
    fileSystem.write(logfile_a, -1, BLOCKP);
    fileSystem.write(logfile_b, -1, BLOCKP);

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
