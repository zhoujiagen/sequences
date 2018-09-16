package com.spike.giantdataanalysis.fs.java.catalog;

public class TPFileDescriptor {

  int mode; // 0 read, 1 write
  long openAt;
  long pos;
}
