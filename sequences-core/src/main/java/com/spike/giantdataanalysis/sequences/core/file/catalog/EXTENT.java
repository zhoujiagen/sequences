package com.spike.giantdataanalysis.sequences.core.file.catalog;

import static com.spike.giantdataanalysis.db.commons.serialize.MoreSerializable.BEGIN;
import static com.spike.giantdataanalysis.db.commons.serialize.MoreSerializable.END;
import static com.spike.giantdataanalysis.db.commons.serialize.MoreSerializable.INTEGER_MAX_STRING_LEN;
import static com.spike.giantdataanalysis.db.commons.serialize.MoreSerializable.SEP;

import com.google.common.base.Strings;
import com.spike.giantdataanalysis.db.commons.serialize.Stringable;
/**
 * Extent: a set of contiguous block.
 */
public class EXTENT implements Stringable<EXTENT> {
  private static final long serialVersionUID = 1L;

  public static EXTENT NULL = new EXTENT();

  public String diskid = DISK.DEFAULT_ID; // in which disk
  public int ext_no = 0; // extent index in the disk
  public int accum_length = 0; // currently fulfilled block size

  // ---------------------------------------------------------------------------
  // Stringable
  // ---------------------------------------------------------------------------
  public static final String PREFIX = "E";

  @Override
  public String asString() {
    return PREFIX + BEGIN//
        + diskid //
        + SEP//
        + Strings.padStart(String.valueOf(ext_no), INTEGER_MAX_STRING_LEN, '0')//
        + SEP//
        + Strings.padStart(String.valueOf(accum_length), INTEGER_MAX_STRING_LEN, '0')//
        + END;
  }

  @Override
  public int size() {
    return PREFIX.length() + BEGIN.length() //
        + diskid.length() //
        + INTEGER_MAX_STRING_LEN//
        + SEP.length()//
        + INTEGER_MAX_STRING_LEN //
        + END.length();
  }

  @Override
  public EXTENT fromString(String raw) {
    EXTENT result = new EXTENT();

    int start = PREFIX.length() + BEGIN.length();
    int end = raw.indexOf(SEP, start);
    result.diskid = raw.substring(start, end);
    start = end + SEP.length();
    end = start + INTEGER_MAX_STRING_LEN;
    result.ext_no = Integer.valueOf(raw.substring(start, end));
    start = end + SEP.length();
    end = start + INTEGER_MAX_STRING_LEN;
    result.accum_length = Integer.valueOf(raw.substring(start, end));

    return result;
  }

  @Override
  public String toString() {
    return asString();
  }

}