package com.spike.giantdataanalysis.sequences.rm.file.fs;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.spike.giantdataanalysis.sequences.commons.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.rm.file.IFS;
import com.spike.giantdataanalysis.sequences.rm.file.catalog.JavaFileCatalogManager;
import com.spike.giantdataanalysis.sequences.rm.file.core.ACCESSMODE;
import com.spike.giantdataanalysis.sequences.rm.file.core.allocparmp;
import com.spike.giantdataanalysis.sequences.rm.file.core.file.FILE;
import com.spike.giantdataanalysis.sequences.rm.file.core.catalog.BLOCK;
import com.spike.giantdataanalysis.sequences.rm.file.core.catalog.basic_file_descriptor;
import com.spike.giantdataanalysis.sequences.rm.file.exception.FileSystemException;

public class DefaultFileSystem implements IFS {

  // ---------------------------------------------------------------------------
  // inner state
  // ---------------------------------------------------------------------------
  private final AtomicInteger fileno_seq = new AtomicInteger(0);

  // ---------------------------------------------------------------------------
  // catalog state and routines
  // ---------------------------------------------------------------------------
  private final JavaFileCatalogManager fileCatalogManager;

  // ---------------------------------------------------------------------------
  // routines
  // ---------------------------------------------------------------------------
  public DefaultFileSystem(Configuration configuration) {
    fileCatalogManager = new JavaFileCatalogManager(configuration.catalogConfiguration);
    fileCatalogManager.initialize();
  }

  @Override
  public int create(String filename, allocparmp allocparmp) {
    if (filename == null || "".equals(filename)) {
      throw FileSystemException.newE("invalid parameter: filename");
    }
    if (allocparmp == null || allocparmp.primary < 0) {
      throw FileSystemException.newE("invalid parameter: allocparmp");
    }

    basic_file_descriptor fd = fileCatalogManager.getFD(filename);
    if (fd != null) {
      throw FileSystemException.newE("file " + filename + " already existed!");
    }

    fd = new basic_file_descriptor();
    fd.filename = filename;
    fd.fileno = fileno_seq.getAndIncrement();
    // allocate extent
    // ... others
    fileCatalogManager.addFD(fd);

    return ReturnCode.OK.code();
  }

  @Override
  public int delete(String filename) {
    // TODO Implement NativeFileSystem.delete
    return 0;
  }

  @Override
  public int open(String filename, ACCESSMODE accessMode, OutParameter<FILE> FILEID) {
    // TODO Implement NativeFileSystem.open
    return 0;
  }

  @Override
  public int close(FILE FILEID) {
    // TODO Implement NativeFileSystem.close
    return 0;
  }

  @Override
  public int extend(FILE FILEID, allocparmp allocparmp) {
    // TODO Implement NativeFileSystem.extend
    return 0;
  }

  @Override
  public int read(FILE FILEID, int BLOCKID, OutParameter<BLOCK> BLOCKP) {
    // TODO Implement NativeFileSystem.read
    return 0;
  }

  @Override
  public int readLine(FILE FILEID, int BLOCKID, OutParameter<BLOCK> BLOCKP) {
    // TODO Implement NativeFileSystem.readLine
    return 0;
  }

  @Override
  public int readc(FILE FILEID, int BLOCKID, int blockcount, OutParameter<List<BLOCK>> BLOCKP) {
    // TODO Implement NativeFileSystem.readc
    return 0;
  }

  @Override
  public int write(FILE FILEID, int BLOCKID, BLOCK BLOCKP) {
    // TODO Implement NativeFileSystem.write
    return 0;
  }

  @Override
  public int writec(FILE FILEID, int BLOCKID, BLOCK BLOCKP) {
    // TODO Implement NativeFileSystem.writec
    return 0;
  }

  @Override
  public int flush(FILE FILEID) {
    // TODO Implement NativeFileSystem.flush
    return 0;
  }

  // ---------------------------------------------------------------------------
  // helper methods
  // ---------------------------------------------------------------------------

}
