package com.spike.giantdataanalysis.sequences.configuration;

public interface FileConfiguration {

  // disk

  /// block
  int BLOCK_DATA_SIZE = 8192; // 8KB

  /// slot, extent
  int SLOT_DATA_SIZE = BLOCK_DATA_SIZE;
  int SLOT_SIZE_IN_DISK = 1024 * 1024; // max number of slot in disk

  int EXTENT_MAX_SIZE = 1000;// max number of extents - not a restriction

  /// page
  int PAGE_DATA_SIZE = BLOCK_DATA_SIZE;

  /// buffer
  int BUFFER_MAX_SIZE = 1000;
  // Prime Numbers: http://www.primos.mat.br/indexen.html
  int BUFFER_HASH_SIZE = 113;
}
