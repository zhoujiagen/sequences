package com.spike.giantdataanalysis.sequences.fs.java.tpfs.catalog;

public class TPFileDescriptor {

  int mode; // 0 read, 1 write
  long openAt;
  long pos;
}
