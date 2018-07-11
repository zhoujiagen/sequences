package com.spike.giantdataanalysis.sequences.rm.file.fs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Bytes;
import com.spike.giantdataanalysis.sequences.commons.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.commons.bytes.MoreBytes;
import com.spike.giantdataanalysis.sequences.rm.file.IFileSystem;
import com.spike.giantdataanalysis.sequences.rm.file.core.AccessMode;
import com.spike.giantdataanalysis.sequences.rm.file.core.FILE;
import com.spike.giantdataanalysis.sequences.rm.file.core.FILEBlock;
import com.spike.giantdataanalysis.sequences.rm.file.core.allocparmp;

/**
 * File System implemented in Java: just a demonstration.
 */
public class JavaFileSystem implements IFileSystem {

  private static final Logger LOG = LoggerFactory.getLogger(JavaFileSystem.class);

  private Map<String, BufferedReader> readable = Maps.newConcurrentMap();
  private Map<String, InnerStreamMode> writable = Maps.newConcurrentMap();

  class InnerStreamMode {
    public OutputStream os;
    public AccessMode mode;

    public InnerStreamMode(OutputStream os, AccessMode mode) {
      this.os = os;
      this.mode = mode;
    }
  }

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

  @Override
  public int open(String filename, AccessMode accessMode, OutParameter<FILE> FILEID) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("open {} with mode: {}", filename, accessMode);
    }

    try {
      if (AccessMode.R.equals(accessMode)) {

        readable.put(filename, Files.newBufferedReader(Paths.get(filename)));

      } else {
        readable.put(filename, Files.newBufferedReader(Paths.get(filename)));
        writable.put(filename, //
          new InnerStreamMode(Files.newOutputStream(Paths.get(filename), StandardOpenOption.WRITE),
              accessMode));
      }
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    }

    FILEID.value().filename = filename;

    return ReturnCode.OK.code();
  }

  @Override
  public int close(FILE FILEID) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("close file {}", FILEID);
    }

    try {
      BufferedReader r = readable.remove(FILEID.filename);
      if (r != null) {
        r.close();
      }

      InnerStreamMode w = writable.remove(FILEID.filename);
      if (w != null && w.os != null) {
        w.os.close();
      }
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    }
    return ReturnCode.OK.code();
  }

  @Override
  public int extend(FILE FILEID, int allocparmp) {
    throw FileSystemException.newE(new UnsupportedOperationException());
  }

  /**
   * @param FILEID
   * @param BLOCKID -1: read all
   * @param BLOCKP
   * @return
   */
  @Override
  public int read(FILE FILEID, int BLOCKID, OutParameter<FILEBlock> BLOCKP) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("read file {}", FILEID);
    }

    if (!readable.containsKey(FILEID.filename)) {
      throw FileSystemException.newE("file not opened!");
    }

    FILEBlock fileBlock = new FILEBlock();
    try {

      BufferedReader reader = readable.get(FILEID.filename);
      String line = null;
      fileBlock.contents = new byte[0];
      while ((line = reader.readLine()) != null) {
        fileBlock.contents = Bytes.concat(fileBlock.contents, line.getBytes());
      }
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    }
    BLOCKP.setValue(fileBlock);
    return ReturnCode.FAIL.code();
  }

  @Override
  public int readLine(FILE FILEID, int BLOCKID, OutParameter<FILEBlock> BLOCKP) {

    if (LOG.isDebugEnabled()) {
      LOG.debug("read file next line {}", FILEID);
    }

    if (!readable.containsKey(FILEID.filename)) {
      throw FileSystemException.newE("file not opened!");
    }

    FILEBlock fileBlock = new FILEBlock();
    try {

      BufferedReader reader = readable.get(FILEID.filename);
      String line = null;
      fileBlock.contents = new byte[0];
      BLOCKP.setValue(fileBlock);
      while ((line = reader.readLine()) != null) {
        fileBlock.contents = Bytes.concat(fileBlock.contents, line.getBytes(Charsets.UTF_8));
        return ReturnCode.FAIL.code();
      }
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    }

    return ReturnCode.FAIL.code();

  }

  @Override
  public int readc(FILE FILEID, int BLOCKID, int blockcount, OutParameter<List<FILEBlock>> BLOCKP) {
    OutParameter<FILEBlock> _BLOCKP = new OutParameter<FILEBlock>();
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
  public int write(FILE FILEID, int BLOCKID, FILEBlock BLOCKP) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("write to file {}, content = {}", FILEID, MoreBytes.toHex(BLOCKP.contents));
    }

    if (!writable.containsKey(FILEID.filename)) {
      throw FileSystemException.newE("cannot write to " + FILEID);
    }

    if (BLOCKID == -1) {
      try {
        InnerStreamMode streamMode = writable.get(FILEID.filename);
        if (!AccessMode.A.equals(streamMode.mode)) {
          throw FileSystemException.newE("invalid operation, can only " + streamMode.mode);
        }
        streamMode.os.write(BLOCKP.contents);
        streamMode.os.flush(); // FLUSH
      } catch (IOException e) {
        throw FileSystemException.newE(e);
      }
    } else if (BLOCKID == -2) {
      try {
        InnerStreamMode streamMode = writable.get(FILEID.filename);
        if (!AccessMode.U.equals(streamMode.mode)) {
          throw FileSystemException.newE("invalid operation, can only " + streamMode.mode);
        }
        streamMode.os.write(BLOCKP.contents);
        streamMode.os.flush(); // FLUSH
      } catch (IOException e) {
        throw FileSystemException.newE(e);
      }
    }

    return ReturnCode.OK.code();
  }

  @Override
  public int writec(FILE FILEID, int BLOCKID, FILEBlock BLOCKP) {
    return write(FILEID, BLOCKID, BLOCKP);
  }

  @Override
  public int flush(FILE FILEID) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("flush file {}, ", FILEID);
    }

    InnerStreamMode streamMode = writable.get(FILEID.filename);
    if (streamMode == null) {
      throw FileSystemException.newE("not exist write handle to " + FILEID.filename);
    }

    try {
      streamMode.os.flush();
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    }

    return ReturnCode.OK.code();
  }

}
