package com.spike.giantdataanalysis.sequences.fs.java.tpfs;

public interface TPFSConfiguration {

  String FS_PATH_PREFIX = "tp://";
  String FS_NAME = "tp";

  String PATH_SEP = "/";
  String PATH_ROOT = "/";

  // FileStore
  String STORE_TYPE = "tp";
  String STORE_NAME_PREFIX = "tp-";
  int storeSize = 10;
  long sizePerStore = 1024 * 1024 * 64;// 64MB

}
