package com.spike.giantdataanalysis.sequences.rm.file.fs;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.sequences.commons.bytes.MoreBytes;
import com.spike.giantdataanalysis.sequences.core.file.ACCESSMODE;
import com.spike.giantdataanalysis.sequences.core.file.allocparmp;
import com.spike.giantdataanalysis.sequences.core.file.file.FILE;
import com.spike.giantdataanalysis.sequences.core.file.catalog.BLOCK;
import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.rm.file.IFS;
import com.spike.giantdataanalysis.sequences.rm.file.exception.FileSystemException;

/**
 * File System implemented in Java: just a demonstration.
 * <p>
 * BLOCK ORGANIZATION(4KB): (bits)
 * 
 * <pre>
 *  
 * ------------------------------------------------------------------------------------------------
 * BLOCK START | BLOCK_SIZE(2)| FREE offset(12) | RECORD ...
 * ------------------------------------------------------------------------------------------------
 * </pre>
 */
public class LocalFileSystem implements IFS {

  private static final Logger LOG = LoggerFactory.getLogger(LocalFileSystem.class);

  // ---------------------------------------------------------------------------
  // state
  // ---------------------------------------------------------------------------

  private FileSystemConfiguration configuration;
  private int blockSizeInByte;

  /** FILE state cache. */
  private static final Map<FILE, FILEState> FILE_STATE_CACHE = Maps.newConcurrentMap();
  /** FILENO counter. */
  private static final AtomicInteger FILENO_SEQUENCE = new AtomicInteger(0);

  private class FILEState {
    public ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    public RandomAccessFile raf;
    public ACCESSMODE accessMode;
  }

  private class BLOCKState {
    public static final int HEADER_BIT_SIZE = 2 + 12;

    // 0: 4KB, 1: 8KB, 2: 16KB
    public int blockSizeType = 0;
    public int freeOffset = 0;

    public int blockSizeInByte() {
      switch (blockSizeType) {
      case 0:
        return 4 * 1024;
      case 1:
        return 4 * 1024;
      case 2:
        return 4 * 1024;
      default:
        return 4 * 1024;
      }
      
      public byte[] toBytes() {
        
      }
    }
  }

  // ---------------------------------------------------------------------------
  // constructor
  // ---------------------------------------------------------------------------

  public LocalFileSystem(FileSystemConfiguration configuration) {
    this.configuration = configuration;
    this.blockSizeInByte = configuration.catalogConfiguration.blockSizeInByte;

    validateConfiguration();
  }

  private void validateConfiguration() {
    Preconditions.checkArgument(configuration != null);

    // TODO(zhoujiagen) check configuration
  }

  // ---------------------------------------------------------------------------
  // file system methods
  // ---------------------------------------------------------------------------

  @Override
  public int create(String filename, allocparmp allocparmp) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("create file {}", filename);
    }

    File file = Paths.get(filename).toFile();
    if (file.exists()) {
      return ReturnCode.FAIL.code();
    }

    boolean res;
    try {
      res = file.createNewFile();
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    }

    return res ? ReturnCode.OK.code() : ReturnCode.FAIL.code();
  }

  @Override
  public int delete(String filename) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("delete file {}", filename);
    }

    File file = Paths.get(filename).toFile();
    boolean res = file.delete();

    return res ? ReturnCode.OK.code() : ReturnCode.FAIL.code();
  }

  /**
   * Note: cache state only when file is successfuly opened.
   */
  @Override
  public int open(String filename, ACCESSMODE accessMode, OutParameter<FILE> FILEID) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("open {} with mode: {}", filename, accessMode);
    }

    File file = Paths.get(filename).toFile();
    if (!file.exists()) {
      if (ACCESSMODE.R.equals(accessMode) || ACCESSMODE.A.equals(accessMode)) {
        throw FileSystemException.newE("invalid operation: " + filename + " does not exist!");
      }
    }

    FILEState _FILEState = queryFILEState(filename);
    if (_FILEState != null && _FILEState.accessMode != null
        && ACCESSMODE.isCompatible(_FILEState.accessMode, accessMode)) {
      throw FileSystemException.newE("file " + filename + "has been opened with mode: "
          + _FILEState.accessMode + ", so cannot be opened with mode: " + accessMode + "!");
    }

    FILE _FILE = new FILE(FILENO_SEQUENCE.getAndIncrement(), filename);
    try {

      FILEState state = new FILEState();
      state.accessMode = accessMode;
      state.raf = new RandomAccessFile(file, accessMode.rafMode());
      if (ACCESSMODE.A.equals(accessMode)) {
        state.raf.seek(state.raf.length());
      }

      FILE_STATE_CACHE.put(_FILE, state);

    } catch (IOException e) {
      throw FileSystemException.newE(e);
    }

    FILEID.value().filename = filename;
    FILEID.value().fileno = _FILE.fileno;

    return ReturnCode.OK.code();
  }

  @Override
  public int close(FILE FILEID) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("close file {}", FILEID);
    }

    FILEState state = queryFILEState(FILEID);
    try {
      state.raf.close();
      removeFILEState(FILEID);
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    }
    return ReturnCode.OK.code();
  }

  @Override
  public int extend(FILE FILEID, allocparmp allocparmp) {
    throw FileSystemException.newE(new UnsupportedOperationException());
  }

  /**
   * @param FILEID
   * @param BLOCKID -1: read all
   * @param BLOCKP
   * @return
   */
  @Override
  public int read(FILE FILEID, int BLOCKID, OutParameter<BLOCK> BLOCKP) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("read file {}", FILEID);
    }

    FILEState state = FILE_STATE_CACHE.get(FILEID);
    if (state == null) {
      throw FileSystemException.newE("file " + FILEID + " not opened!");
    }

    ReadLock rLock = state.lock.readLock();
    rLock.lock();
    BLOCK fileBlock = new BLOCK();
    try {
      byte[] bytes = new byte[blockSizeInByte];
      state.raf.seek(blockSizeInByte * BLOCKID);
      int readByteCount = state.raf.read(bytes, 0, blockSizeInByte);
      if (readByteCount != -1) {
        fileBlock.contents = MoreBytes.copy(bytes, 0, readByteCount);
      }
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    } finally {
      rLock.unlock();
    }

    fileBlock.header.fileno = FILEID.fileno;
    fileBlock.header.blockno = BLOCKID;
    BLOCKP.setValue(fileBlock);
    return ReturnCode.FAIL.code();
  }

  @Override
  public int readLine(FILE FILEID, int BLOCKID, OutParameter<BLOCK> BLOCKP) {

    if (LOG.isDebugEnabled()) {
      LOG.debug("read file next line {}", FILEID);
    }

    FILEState state = FILE_STATE_CACHE.get(FILEID);
    if (state == null) {
      throw FileSystemException.newE("file " + FILEID + " not opened!");
    }

    ReadLock rLock = state.lock.readLock();
    rLock.lock();
    BLOCK fileBlock = new BLOCK();
    try {
      state.raf.seek(blockSizeInByte * BLOCKID);
      String line = state.raf.readLine();
      if (line != null) {
        fileBlock.contents = line.getBytes();
      }
      fileBlock.header.fileno = FILEID.fileno;
      fileBlock.header.blockno = BLOCKID;
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    } finally {
      rLock.unlock();
    }

    return ReturnCode.FAIL.code();

  }

  @Override
  public int readc(FILE FILEID, int BLOCKID, int blockcount, OutParameter<List<BLOCK>> BLOCKP) {
    OutParameter<BLOCK> _BLOCKP = new OutParameter<BLOCK>();

    FILEState state = FILE_STATE_CACHE.get(FILEID);
    if (state == null) {
      throw FileSystemException.newE("file " + FILEID + " not opened!");
    }

    ReadLock rLock = state.lock.readLock();
    rLock.lock();
    List<BLOCK> blocks = Lists.newArrayList();
    try {
      state.raf.seek(blockSizeInByte * BLOCKID);
      for (int i = 0; i < blockcount; i++) {
        BLOCK fileBlock = new BLOCK();
        byte[] bytes = new byte[blockSizeInByte];
        state.raf.seek(blockSizeInByte * BLOCKID);
        int readByteCount = state.raf.read(bytes, 0, blockSizeInByte);
        if (readByteCount != -1) {
          fileBlock.contents = MoreBytes.copy(bytes, 0, readByteCount);
        } else {
          break;
        }
        fileBlock.header.fileno = FILEID.fileno;
        fileBlock.header.blockno = BLOCKID;
        blocks.add(fileBlock);
      }
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    } finally {
      rLock.unlock();
    }

    int result = read(FILEID, BLOCKID, _BLOCKP);
    BLOCKP.setValue(Lists.newArrayList(_BLOCKP.value()));
    return result;
  }

  /**
   * @param FILEID
   * @param BLOCKID -1: append, -2 write all
   * @param BLOCKP
   * @return
   */
  @Override
  public int write(FILE FILEID, int BLOCKID, BLOCK BLOCKP) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("write to file {}, content = {}", FILEID, new String(BLOCKP.contents));
    }

    FILEState state = FILE_STATE_CACHE.get(FILEID);
    if (state == null) {
      throw FileSystemException.newE("file " + FILEID + " not opened!");
    }
    if (state.accessMode == ACCESSMODE.R) {
      throw FileSystemException
          .newE("invalid operation: cannot write when using read access mode!");
    }

    WriteLock wLock = state.lock.writeLock(); // WARN: too raw lock!!!
    wLock.lock();
    try {
      int blockStartOffset = blockSizeInByte * BLOCKID;
      int blockEndOffset = blockStartOffset + blockSizeInByte;
      state.raf.seek(blockStartOffset);
      byte[] b = new byte[BLOCKState.HEADER_BIT_SIZE];
      int readByteCount = state.raf.read(b, 0, BLOCKState.HEADER_BIT_SIZE);
      if (readByteCount == -1) {
        throw FileSystemException.newE("invalid block header in file");
      }

      // int blockSizeType = MoreBytes.toInt(b, 0, 2);
      int freeOffset = MoreBytes.toInt(b, 2, BLOCKState.HEADER_BIT_SIZE - 2);

      int toWriteByteCount = BLOCKP.contents.length;
      if (freeOffset + toWriteByteCount > blockEndOffset) {
        throw FileSystemException.newE("no enough space in block!");
      }
      if (ACCESSMODE.A.equals(state.accessMode)) {
        state.raf.seek(blockStartOffset + freeOffset);
        state.raf.write(BLOCKP.contents);
      } else if (ACCESSMODE.U.equals(state.accessMode)) {
        // pad black hole here
        state.raf.seek(blockStartOffset + 2);
        if (toWriteByteCount > freeOffset) {
          state.raf.write(BLOCKP.contents, 0, toWriteByteCount);
        } else {
          state.raf.write(MoreBytes.padTail(BLOCKP.contents, freeOffset - toWriteByteCount), 0,
            freeOffset);
        }
      }

      state.raf.seek(blockStartOffset + 2);
      byte[] toB = MoreBytes.toBytes(freeOffset + toWriteByteCount);
      state.raf.write(MoreBytes.padHead(toB, BLOCKState.HEADER_BIT_SIZE - 2 - toB.length));

    } catch (Exception e) {
      throw FileSystemException.newE(e);
    } finally {
      wLock.unlock();
    }

    return ReturnCode.OK.code();
  }

  @Override
  public int writec(FILE FILEID, int BLOCKID, BLOCK BLOCKP) {

    FILEState state = FILE_STATE_CACHE.get(FILEID);
    if (state == null) {
      throw FileSystemException.newE("file " + FILEID + " not opened!");
    }
    if (state.accessMode == ACCESSMODE.R) {
      throw FileSystemException
          .newE("invalid operation: cannot write when using read access mode!");
    }

    WriteLock wLock = state.lock.writeLock(); // WARN: too raw lock!!!
    wLock.lock();
    try {
      int blockStartOffset = blockSizeInByte * BLOCKID;
      int blockEndOffset = blockStartOffset + blockSizeInByte;
      state.raf.seek(blockStartOffset);
      byte[] b = new byte[BLOCKState.HEADER_BIT_SIZE];
      int readByteCount = state.raf.read(b, 0, BLOCKState.HEADER_BIT_SIZE);
      if (readByteCount == -1) {
        throw FileSystemException.newE("invalid block header in file");
      }

      // int blockSizeType = MoreBytes.toInt(b, 0, 2);
      int freeOffset = MoreBytes.toInt(b, 2, BLOCKState.HEADER_BIT_SIZE - 2);

      int toWriteByteCount = BLOCKP.contents.length;
      if (freeOffset + toWriteByteCount > blockEndOffset) {
        throw FileSystemException.newE("no enough space in block!");
      }
      // FIXME(zhoujiagen) slice input: i.e BLOCKP.contents
      // MAY CROSS BLOCK BOUNDARY
      if (ACCESSMODE.A.equals(state.accessMode)) {
        state.raf.seek(blockStartOffset + freeOffset);
        state.raf.write(BLOCKP.contents);
      } else if (ACCESSMODE.U.equals(state.accessMode)) {
        // pad black hole here
        state.raf.seek(blockStartOffset + 2);
        if (toWriteByteCount > freeOffset) {
          state.raf.write(BLOCKP.contents, 0, toWriteByteCount);
        } else {
          state.raf.write(MoreBytes.padTail(BLOCKP.contents, freeOffset - toWriteByteCount), 0,
            freeOffset);
        }
      }

      state.raf.seek(blockStartOffset + 2);
      byte[] toB = MoreBytes.toBytes(freeOffset + toWriteByteCount);
      state.raf.write(MoreBytes.padHead(toB, BLOCKState.HEADER_BIT_SIZE - 2 - toB.length));

    } catch (Exception e) {
      throw FileSystemException.newE(e);
    } finally {
      wLock.unlock();
    }

    return ReturnCode.OK.code();
  }

  @Override
  public int flush(FILE FILEID) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("flush file {}, ", FILEID);
    }

    FILEState state = FILE_STATE_CACHE.get(FILEID);
    if (state == null) {
      throw FileSystemException.newE("file " + FILEID + " not opened!");
    }

    return ReturnCode.OK.code();
  }

  // ---------------------------------------------------------------------------
  // helper methods
  // ---------------------------------------------------------------------------

  private FILEState queryFILEState(FILE file) {
    return FILE_STATE_CACHE.get(file);
  }

  private FILEState queryFILEState(String filename) {
    for (FILE file : FILE_STATE_CACHE.keySet()) {
      if (file.filename.equals(filename)) {
        return FILE_STATE_CACHE.get(file);
      }
    }
    return null;
  }

  private void removeFILEState(FILE file) {
    FILE_STATE_CACHE.remove(file);
  }

  private void removeFILEState(String filename) {
    FILE _FILE = null;
    for (FILE file : FILE_STATE_CACHE.keySet()) {
      if (file.filename.equals(filename)) {
        _FILE = file;
      }
    }
    if (_FILE != null) {
      FILE_STATE_CACHE.remove(_FILE);
    }
  }

}
