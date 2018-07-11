package com.spike.giantdataanalysis.sequences.rm.file.config;

public class FileConfiguration {

  static int BLOCK_DATA_SIZE = 8192; // 8KB

  static int SLOT_DATA_SIZE = 8192;
  static int SLOT_SIZE_IN_DISK = 1024 * 1024; // max number of slot in disk

  static int EXTENT_MAX_SIZE = 1000;// max number of extents
  static int EXTENT_SIZE_IN_DISK = 1000;
}
