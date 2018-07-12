package com.spike.giantdataanalysis.sequences.rm.file.core;

public enum PAGE_TYPE {
  DATA, // tuples in one relation
  INDEX, // nodes in one index file
  FREESPACE, // free block table
  DIRECTORY, // metadata of files
  CLUSTER, // tuples from multiple relations
  TABLE// including management table
}