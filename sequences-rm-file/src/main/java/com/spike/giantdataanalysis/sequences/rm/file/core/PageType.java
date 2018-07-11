package com.spike.giantdataanalysis.sequences.rm.file.core;

public enum PageType {
  DATA, // tuples in one relation
  INDEX, // nodes in one index file
  FREESPACE, //
  DIRECTORY, // metadata of files
  CLUSTER, // tuples from multiple relations
  TABLE//
}
