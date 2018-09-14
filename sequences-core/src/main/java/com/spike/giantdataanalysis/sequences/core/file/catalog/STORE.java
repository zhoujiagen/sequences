package com.spike.giantdataanalysis.sequences.core.file.catalog;

import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.BEGIN;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.END;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.SEP;

import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.sequences.serialize.Dumpable;
import com.spike.giantdataanalysis.sequences.serialize.Stringable;

// TODO(zhoujiagen) add support for extent allocaiton???
public class STORE implements Dumpable, Stringable<STORE> {
  private static final long serialVersionUID = 1L;

  public static STORE NULL = new STORE(new DISK());

  public List<DISK> disks = Lists.newArrayList();

  /** allocated extents in store */
  public LinkedList<EXTENT_ENTRY> extent_table = Lists.newLinkedList();

  // ---------------------------------------------------------------------------
  // Stringable
  // ---------------------------------------------------------------------------
  public static final String PREFIX = "S";

  public static final String SEP_LIST = "|" + SEP + "|";

  @Override
  public String asString() {
    return PREFIX + BEGIN //
        + Joiner.on(",").join(disks)//
        + SEP_LIST//
        + Joiner.on(",").join(extent_table)//
        + END;
  }

  @Override
  public int size() {
    return PREFIX.length() + BEGIN.length() //
        + disks.size() * DISK.NULL.size() //
        + SEP_LIST.length()//
        + extent_table.size() * EXTENT_ENTRY.NULL.size();
  }

  @Override
  public STORE fromString(String raw) {
    STORE result = new STORE();

    int start = PREFIX.length() + BEGIN.length();
    int end = raw.indexOf(SEP_LIST, start);
    String disksStrig = raw.substring(start, end);
    if (!"".equals(disksStrig)) {
      for (String disks : Splitter.on(",").splitToList(disksStrig)) {
        result.disks.add(DISK.NULL.fromString(disks));
      }
    }
    start = end + SEP_LIST.length();
    end = raw.indexOf(END, start);
    String extent_tableString = raw.substring(start, end);
    if (!"".equals(extent_tableString)) {
      for (String extent_table : Splitter.on(",").splitToList(extent_tableString)) {
        result.extent_table.add(EXTENT_ENTRY.NULL.fromString(extent_table));
      }
    }

    return result;
  }

  public STORE(DISK disk) {
    disks.add(disk);
  }

  public STORE(DISK... disks) {
    for (DISK disk : disks) {
      this.disks.add(disk);
    }
  }

  public void addDISK(DISK disk) {
    this.disks.add(disk);
  }

  @Override
  public String dump() {
    StringBuilder sb = new StringBuilder();
    sb.append("STORE[");
    sb.append(Joiner.on(System.lineSeparator()).join(disks));
    sb.append("]");
    return sb.toString();
  }

  @Override
  public String toString() {
    return dump();
  }

}
