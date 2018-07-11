package com.spike.giantdataanalysis.sequences.rm.file.core;

import java.util.BitSet;

public class Disk {
  String diskid;
  DiskSlot[] slots;
  BitSet bitmap;

  ExtentEntry[] extent_table;
}
