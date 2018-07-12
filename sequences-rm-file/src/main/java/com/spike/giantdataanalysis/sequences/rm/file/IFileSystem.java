package com.spike.giantdataanalysis.sequences.rm.file;

import java.util.List;

import com.spike.giantdataanalysis.sequences.commons.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.rm.file.core.ACCESSMODE;
import com.spike.giantdataanalysis.sequences.rm.file.core.BLOCK;
import com.spike.giantdataanalysis.sequences.rm.file.core.FILE;
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

  /**
   * create and allocate file.
   * @param filename
   * @param allocparmp
   * @return
   */
  int create(String filename, allocparmp allocparmp);

  /**
   * delete and deallocate file.
   * @param filename
   * @return
   */
  int delete(String filename);

  /**
   * open a file with access mode.
   * @param filename
   * @param accessMode
   * @param FILEID
   * @return
   */
  int open(String filename, ACCESSMODE accessMode, OutParameter<FILE> FILEID);

  /**
   * close a file.
   * @param FILEID
   * @return
   */
  int close(FILE FILEID);

  /**
   * extend an existed file with give parameter.
   * @param FILEID
   * @param allocparmp
   * @return
   */
  int extend(FILE FILEID, int allocparmp);

  /**
   * read block in disk to buffer in memeory.
   * @param FILEID
   * @param BLOCKID
   * @param BLOCKP
   * @return
   */
  int read(FILE FILEID, int BLOCKID, OutParameter<BLOCK> BLOCKP);

  // read a line
  int readLine(FILE FILEID, int BLOCKID, OutParameter<BLOCK> BLOCKP);

  /**
   * read <code>blockcount</code> blocks in disk to buffer in memory.
   * @param FILEID
   * @param BLOCKID
   * @param blockcount
   * @param BLOCKP
   * @return
   */
  int readc(FILE FILEID, int BLOCKID, int blockcount, OutParameter<List<BLOCK>> BLOCKP);

  /**
   * write from buffer in memeory to block in disk.
   * @param FILEID
   * @param BLOCKID
   * @param BLOCKP
   * @return
   */
  int write(FILE FILEID, int BLOCKID, BLOCK BLOCKP);

  /**
   * write continuously from buffer in memeory to block in disk.
   * @param FILEID
   * @param BLOCKID
   * @param BLOCKP
   * @return
   */
  int writec(FILE FILEID, int BLOCKID, BLOCK BLOCKP);

  // flush
  int flush(FILE FILEID);

}
