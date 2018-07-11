package com.spike.giantdataanalysis.sequences.rm.file;

import java.util.List;

import com.spike.giantdataanalysis.sequences.commons.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.rm.file.core.AccessMode;
import com.spike.giantdataanalysis.sequences.rm.file.core.FILE;
import com.spike.giantdataanalysis.sequences.rm.file.core.FILEBlock;
import com.spike.giantdataanalysis.sequences.rm.file.core.allocparmp;

/**
 * File System.
 */
public interface IFileSystem {

  class FileSystemException extends RuntimeException {
    private static final long serialVersionUID = 5225858589672335027L;

    public static FileSystemException newE() {
      return new FileSystemException();
    }

    public static FileSystemException newE(String message) {
      return new FileSystemException(message);
    }

    public static FileSystemException newE(String message, Throwable cause) {
      return new FileSystemException(message, cause);
    }

    public static FileSystemException newE(Throwable cause) {
      return new FileSystemException(cause);
    }

    public static FileSystemException newE(String message, Throwable cause,
        boolean enableSuppression, boolean writableStackTrace) {
      return new FileSystemException(message, cause, enableSuppression, writableStackTrace);
    }

    public FileSystemException() {
      super();
    }

    public FileSystemException(String message) {
      super(message);
    }

    public FileSystemException(String message, Throwable cause) {
      super(message, cause);
    }

    public FileSystemException(Throwable cause) {
      super(cause);
    }

    public FileSystemException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
    }

  }

  enum ReturnCode {
    OK(0), FAIL(1);
    private int code;

    ReturnCode(int code) {
      this.code = code;
    }

    public int code() {
      return code;
    }
  }

  int create(String filename, allocparmp allocparmp);

  int delete(String filename);

  int open(String filename, AccessMode accessMode, OutParameter<FILE> FILEID);

  int close(FILE FILEID);

  int extend(FILE FILEID, int allocparmp);

  // read block in disk to buffer in memeory
  int read(FILE FILEID, int BLOCKID, OutParameter<FILEBlock> BLOCKP);

  int readLine(FILE FILEID, int BLOCKID, OutParameter<FILEBlock> BLOCKP);
  
  
  int readc(FILE FILEID, int BLOCKID, int blockcount, OutParameter<List<FILEBlock>> BLOCKP);

  // write from buffer in memeory to block in disk
  int write(FILE FILEID, int BLOCKID, FILEBlock BLOCKP);

  int writec(FILE FILEID, int BLOCKID, FILEBlock BLOCKP);

  int flush(FILE FILEID);

}
