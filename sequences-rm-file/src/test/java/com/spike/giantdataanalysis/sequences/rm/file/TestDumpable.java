package com.spike.giantdataanalysis.sequences.rm.file;

import com.spike.giantdataanalysis.sequences.rm.file.core.catalog.DISK;
import com.spike.giantdataanalysis.sequences.rm.file.core.catalog.STORE;

public class TestDumpable {

  public static void main(String[] args) {
    STORE store = new STORE(new DISK(10), new DISK(3));
    System.out.println(store.dump());
  }
}
