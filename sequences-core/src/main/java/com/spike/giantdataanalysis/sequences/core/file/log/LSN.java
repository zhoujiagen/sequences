package com.spike.giantdataanalysis.sequences.core.file.log;

import static com.spike.giantdataanalysis.db.commons.serialize.MoreSerializable.BEGIN;
import static com.spike.giantdataanalysis.db.commons.serialize.MoreSerializable.END;
import static com.spike.giantdataanalysis.db.commons.serialize.MoreSerializable.LONG_MAX_STRING_LEN;
import static com.spike.giantdataanalysis.db.commons.serialize.MoreSerializable.SEP;

import com.google.common.base.Strings;
import com.spike.giantdataanalysis.db.commons.serialize.Stringable;
public class LSN implements Stringable<LSN> {
  private static final long serialVersionUID = 6268301188928797446L;

  // file no in directory
  // example: NNN => a_prefix.logaNNN, b_prefix.logbNNN
  public final long file;
  public final long rba; // record begining byte offset in file

  public LSN(long file, long rba) {
    this.file = file;
    this.rba = rba;
  }

  public static LSN NULL = new LSN(0, 0);

  // ---------------------------------------------------------------------------
  // Stringable
  // ---------------------------------------------------------------------------
  public static final String PREFIX = "LSN";

  @Override
  public String asString() {
    return PREFIX + BEGIN //
        + Strings.padStart(String.valueOf(file), LONG_MAX_STRING_LEN, '0') //
        + SEP //
        + Strings.padStart(String.valueOf(rba), LONG_MAX_STRING_LEN, '0')//
        + END;
  }

  @Override
  public int size() {
    return PREFIX.length() + BEGIN.length() //
        + LONG_MAX_STRING_LEN //
        + SEP.length()//
        + LONG_MAX_STRING_LEN //
        + END.length();
  }

  @Override
  public LSN fromString(String raw) {

    int start = PREFIX.length() + BEGIN.length();
    int end = start + LONG_MAX_STRING_LEN;
    long _file = Long.valueOf(raw.substring(start, end));
    start = end + SEP.length();
    end = start + LONG_MAX_STRING_LEN;
    long _rba = Long.valueOf(raw.substring(start, end));
    return new LSN(_file, _rba);
  }

  // ---------------------------------------------------------------------------
  // Byteable
  // ---------------------------------------------------------------------------
  // @Override
  // public byte[] toBytes() {
  //
  // return MoreBytes.add(new byte[][] { MoreBytes.toBytes(file), MoreBytes.toBytes(rba) });
  // }
  //
  // @Override
  // public int size() {
  // return Long.SIZE / Byte.SIZE * 2;
  // }
  //
  // @Override
  // public LSN fromBytes(byte[] bytes) {
  // int offset = 0;
  // long file = MoreBytes.getLong(bytes, offset);
  // offset += Long.SIZE / Byte.SIZE;
  // long rba = MoreBytes.getLong(bytes, offset);
  // return new LSN(file, rba);
  // }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (file ^ (file >>> 32));
    result = prime * result + (int) (rba ^ (rba >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    LSN other = (LSN) obj;
    if (file != other.file) return false;
    if (rba != other.rba) return false;
    return true;
  }

}
