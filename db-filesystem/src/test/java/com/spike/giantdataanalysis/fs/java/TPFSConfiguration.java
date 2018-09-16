package com.spike.giantdataanalysis.fs.java;

public interface TPFSConfiguration {

  String FS_NAME = "tp";
  String FS_SCHEMA = "tp";
  String FS_SCHEMA_PATH = "tp:///";
  String FS_SCHEMA_PATH_PREFIX = "tp://";
  String PATH_SEP = "/";
  String PATH_ROOT = "/";

  // FileStore
  String STORE_TYPE = "tp";
  String STORE_NAME_PREFIX = "tp-";
  String STORE_ROOT = "/tmp/TPFS";
  int storeSize = 10;
  long sizePerStore = 1024 * 1024 * 64;// 64MB

}
