package com.spike.giantdataanalysis.sequences.rm.file;

import java.util.List;

import com.spike.giantdataanalysis.sequences.commons.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.rm.file.core.ACCESSMODE;
import com.spike.giantdataanalysis.sequences.rm.file.core.allocparmp;
import com.spike.giantdataanalysis.sequences.rm.file.core.file.FILE;
import com.spike.giantdataanalysis.sequences.rm.file.core.catalog.BLOCK;

/**
 * File System.
 * <p>
 * Note that
 * <ul>
 * <li><code>filename</code> should be unique in file system
 * <li><code>FILEID</code> is just a file handle used after calling
 * {@link #open(String, ACCESSMODE, OutParameter)}
 * <li><code>FILENO</code>/<code>fileno</code> is matained by file system
 */
public interface IFS {

  class Configuration {
    public CatalogConfiguration catalogConfiguration;
  }
  
  
  class CatalogConfiguration {
    public String catalogDir = "target/catalog/";
    public String storeFilePreix = "STORE-";
    public String fileDescriptorFilePrefix = "FD-";
    public int nameLength = 10;
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
   * @param filename should be unique in file system
   * @param allocparmp
   * @return
   */
  int create(String filename, allocparmp allocparmp);

  /**
   * delete and deallocate file.
   * @param filename should be unique in file system
   * @return
   */
  int delete(String filename);

  /**
   * open a file with access mode.
   * @param filename should be unique in file system
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
  int extend(FILE FILEID, allocparmp allocparmp);

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
