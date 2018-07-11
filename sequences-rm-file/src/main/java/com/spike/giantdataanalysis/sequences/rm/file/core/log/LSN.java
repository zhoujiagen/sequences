package com.spike.giantdataanalysis.sequences.rm.file.core.log;

import com.spike.giantdataanalysis.sequences.commons.bytes.MoreBytes;
import com.spike.giantdataanalysis.sequences.rm.file.core.Byteable;

public class LSN implements Byteable<LSN> {
  // file no in directory
  // example: NNN => a_prefix.logaNNN, b_prefix.logbNNN
  public final long file;
  public final long rba; // record begining byte offset in file

  public LSN(long file, long rba) {
    this.file = file;
    this.rba = rba;
  }

  public static LSN NULL = new LSN(0, 0);

  @Override
  public byte[] toBytes() {

    return MoreBytes.add(new byte[][] { MoreBytes.toBytes(file), MoreBytes.toBytes(rba) });
  }

  @Override
  public int size() {
    return Long.SIZE / Byte.SIZE * 2;
  }

  @Override
  public LSN fromBytes(byte[] bytes) {
    int offset = 0;
    long file = MoreBytes.getLong(bytes, offset);
    offset += Long.SIZE / Byte.SIZE;
    long rba = MoreBytes.getLong(bytes, offset);
    return new LSN(file, rba);
  }

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
