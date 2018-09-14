package com.spike.giantdataanalysis.sequences.core.file.catalog;

import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.BEGIN;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.END;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.INTEGER_MAX_STRING_LEN;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.SEP;

import com.google.common.base.Strings;
import com.spike.giantdataanalysis.sequences.serialize.Dumpable;
import com.spike.giantdataanalysis.sequences.serialize.Stringable;

public class EXTENT_ENTRY implements Dumpable, Stringable<EXTENT_ENTRY> {
  private static final long serialVersionUID = 1L;

  public int first_slot = 0; // first slot index in this extent entry
  public int xt_length = 0; // slot count

  public static EXTENT_ENTRY NULL = new EXTENT_ENTRY();

  // ---------------------------------------------------------------------------
  // Stringable
  // ---------------------------------------------------------------------------
  public static final String PREFIX = "EE";

  @Override
  public String asString() {
    return PREFIX + BEGIN //
        + Strings.padStart(String.valueOf(first_slot), INTEGER_MAX_STRING_LEN, '0') //
        + SEP.length() //
        + Strings.padStart(String.valueOf(xt_length), INTEGER_MAX_STRING_LEN, '0') //
        + END;
  }

  @Override
  public int size() {
    return PREFIX.length() + BEGIN.length() //
        + INTEGER_MAX_STRING_LEN //
        + SEP.length() //
        + INTEGER_MAX_STRING_LEN//
        + END.length();
  }

  @Override
  public EXTENT_ENTRY fromString(String raw) {
    EXTENT_ENTRY result = new EXTENT_ENTRY();
    int start = PREFIX.length() + BEGIN.length();
    int end = start + INTEGER_MAX_STRING_LEN;
    result.first_slot = Integer.valueOf(raw.substring(start, end));
    start = end + SEP.length();
    end = start + INTEGER_MAX_STRING_LEN;
    result.xt_length = Integer.valueOf(raw.substring(start, end));

    return result;
  }

  @Override
  public String dump() {
    return "first_slot=" + first_slot + ", xt_length=" + xt_length;
  }

  @Override
  public String toString() {
    return dump();
  }

}