package com.spike.giantdataanalysis.sequences.core.file;

import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.BEGIN;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.END;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.FLOAT_MAX_STRING_LEN;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.INTEGER_MAX_STRING_LEN;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.SEP;

import com.google.common.base.Strings;
import com.spike.giantdataanalysis.sequences.serialize.MoreSerializable;

/**
 * Allocation parameter.
 */
public class allocparmp implements MoreSerializable.Stringable<allocparmp> {
  private static final long serialVersionUID = 1L;

  public static allocparmp NULL = new allocparmp();

  public int primary = 0; // initial allocation request

  public int secondary = 0; // next allocation request
  public float growth = 0f; // next allocation increase factor

  // ---------------------------------------------------------------------------
  // Stringable
  // ---------------------------------------------------------------------------

  public static final String PREFIX = "ap";

  @Override
  public String asString() {
    return PREFIX + BEGIN//
        + Strings.padStart(String.valueOf(primary), INTEGER_MAX_STRING_LEN, '0')//
        + SEP//
        + Strings.padStart(String.valueOf(secondary), INTEGER_MAX_STRING_LEN, '0')//
        + SEP //
        + Strings.padStart(String.valueOf(growth), FLOAT_MAX_STRING_LEN, '0')//
        + END;
  }

  @Override
  public int size() {
    return PREFIX.length() + BEGIN.length() //
        + INTEGER_MAX_STRING_LEN//
        + INTEGER_MAX_STRING_LEN//
        + FLOAT_MAX_STRING_LEN//
        + END.length();
  }

  @Override
  public allocparmp fromString(String raw) {
    allocparmp result = new allocparmp();

    int start = PREFIX.length() + BEGIN.length();
    int end = start + INTEGER_MAX_STRING_LEN;
    result.primary = Integer.valueOf(raw.substring(start, end));
    start = end + SEP.length();
    end = start + INTEGER_MAX_STRING_LEN;
    result.secondary = Integer.valueOf(raw.substring(start, end));
    start = end + SEP.length();
    end = start + FLOAT_MAX_STRING_LEN;
    result.growth = Float.valueOf(raw.substring(start, end));

    return result;
  }

}
