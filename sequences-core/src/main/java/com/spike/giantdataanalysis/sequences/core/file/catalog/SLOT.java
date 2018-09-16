package com.spike.giantdataanalysis.sequences.core.file.catalog;

import com.spike.giantdataanalysis.db.commons.serialize.Dumpable;

public class SLOT implements Dumpable {
  public String diskid = DISK.DEFAULT_ID;
  public int slotno = 0;

  // public byte[] contents = new byte[FileConfiguration.SLOT_DATA_SIZE];

  public SLOT(String diskid) {
    this.diskid = diskid;
  }

  @Override
  public String dump() {
    StringBuilder sb = new StringBuilder();

    sb.append("SLOT[diskid=");
    sb.append(diskid);
    // sb.append(", contents=");
    // sb.append(MoreBytes.toHex(contents, 0, 10));
    sb.append(", slotno=").append(slotno);
    sb.append("]");

    return sb.toString();
  }

  @Override
  public String toString() {
    return dump();
  }

}