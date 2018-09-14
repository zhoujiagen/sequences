package com.spike.giantdataanalysis.sequences.core.file.catalog;

import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.BEGIN;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.END;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.INTEGER_MAX_STRING_LEN;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.SEP;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.sequences.serialize.Dumpable;
import com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.Ops;
import com.spike.giantdataanalysis.sequences.serialize.Stringable;

/**
 * Disk with addressing tuple: (磁盘标识diskid, 柱面号cylinderno, 磁道号trackno, 槽号slotno).
 * <p>
 * WARNIG: just a mocking object.
 */
public class DISK implements Dumpable, Stringable<DISK> {
  private static final long serialVersionUID = 1L;

  public static DISK NULL = new DISK(0);

  // ---------------------------------------------------------------------------
  // inner state
  // ---------------------------------------------------------------------------

  public static String DEFAULT_ID = "default";

  public String diskid = DEFAULT_ID;
  // /** all slots in disk */
  // private SLOT[] slots = new SLOT[0];
  public int slotSize = 0;

  /** indicator of free slots */
  public BitSet bitmap = new BitSet(0);
  /** allocated extents in disk */
  public LinkedList<EXTENT_ENTRY> extent_table = Lists.newLinkedList();

  // ---------------------------------------------------------------------------
  // Stringable
  // ---------------------------------------------------------------------------

  public static final String PREFIX = "D";

  @Override
  public String asString() {
    return PREFIX + BEGIN//
        + diskid//
        + SEP //
        + Strings.padStart(String.valueOf(slotSize), INTEGER_MAX_STRING_LEN, '0')//
        + SEP//
        + Joiner.on(",").join(Ops.to(bitmap.toLongArray())) //
        + SEP //
        + Joiner.on(",").join(extent_table)//
        + END;
  }

  @Override
  public int size() {
    return -1;
  }

  @Override
  public DISK fromString(String raw) {
    DISK result = new DISK();
    int start = PREFIX.length() + BEGIN.length();
    int end = raw.indexOf(SEP);
    result.diskid = raw.substring(start, end);
    start = end + SEP.length();
    end = start + INTEGER_MAX_STRING_LEN;
    result.slotSize = Integer.valueOf(raw.substring(start, end));
    start = end + SEP.length();
    end = raw.indexOf(SEP, start);
    String bitSetString = raw.substring(start, end);
    if (!"".equals(bitSetString)) {
      List<String> byteStrings = Splitter.on(",").splitToList(bitSetString);
      int len = byteStrings.size();
      if (len > 0) {
        long[] values = new long[len];
        for (int i = 0; i < len; i++) {
          values[i] = Long.valueOf(byteStrings.get(i));
        }
        result.bitmap = BitSet.valueOf(values);
      }
    }
    start = end + SEP.length();
    end = raw.indexOf(END, start);
    String extent_entry_String = raw.substring(start, end);
    if (!"".equals(extent_entry_String)) {
      List<String> extent_entry_list = Splitter.on(",").splitToList(extent_entry_String);
      if (extent_entry_list.size() > 0) {
        for (String ee : extent_entry_list) {
          result.extent_table.add(EXTENT_ENTRY.NULL.fromString(ee));
        }
      }
    }

    return result;
  }

  public DISK() {
    this(DEFAULT_ID, 0);
  }

  public DISK(int slotSize) {
    this(DEFAULT_ID, slotSize);
  }

  public DISK(String diskid, int slotSize) {
    if (slotSize < 0) {
      throw new IllegalArgumentException("invalid slotSize");
    }
    this.diskid = diskid;
    // this.slots = new SLOT[slotSize];
    // for (int i = 0; i < slotSize; i++) {
    // this.slots[i] = new SLOT(diskid);
    // }
    this.bitmap = new BitSet(slotSize);
  }

  // ---------------------------------------------------------------------------
  // routines
  // ---------------------------------------------------------------------------
  // TODO(zhoujiagen) allocate extents

  @Override
  public String dump() {
    StringBuilder sb = new StringBuilder();

    sb.append("DISK[diskid:").append(diskid);
    sb.append(System.lineSeparator());

    // sb.append("slot:");
    // sb.append(System.lineSeparator());
    // sb.append(Joiner.on(System.lineSeparator()).join(slots));
    // sb.append(System.lineSeparator());

    sb.append("bitmap:");
    sb.append(System.lineSeparator());
    sb.append(bitmap.toString());
    sb.append(System.lineSeparator());

    sb.append("extent_table:");
    sb.append(System.lineSeparator());
    sb.append(Joiner.on(System.lineSeparator()).join(extent_table));

    sb.append("]");

    return sb.toString();
  }

  @Override
  public String toString() {
    return asString();
  }

}
