package com.spike.giantdataanalysis.sequences.rm.file.core;

import java.util.BitSet;

import com.spike.giantdataanalysis.sequences.rm.file.config.FileConfiguration;
import com.spike.giantdataanalysis.sequences.rm.file.core.basic_file_descriptor.extent_entry;

/**
 * Disk with addressing tuple: (磁盘标识diskid, 柱面号cylinderno, 磁道号trackno, 槽号slotno).
 */
public class Disk { // TODO(zhoujiagen) need to check
  public String diskid = "DEFAULT";
  public Slot[] slots = new Slot[FileConfiguration.SLOT_SIZE_IN_DISK]; // all slots
  public BitSet bitmap = new BitSet(FileConfiguration.SLOT_SIZE_IN_DISK); // indicator of free slots
  public extent_entry[] extent_table; // allocated extents

  public static class Slot {
    String diskid = "DEFAULT";
    byte[] contents = new byte[FileConfiguration.SLOT_DATA_SIZE];
  }
}
