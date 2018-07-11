package com.spike.giantdataanalysis.sequences.rm.file.core;

public final class FILEBlock {

  // block heaer
  public int flip; // write check mode - before
  public int fileno; // file no
  public int blockno; // block no in file

  // content
  public byte[] contents;// = new byte[FileConfiguration.BLOCK_DATA_SIZE];
  public int flop; // write check mode - after

  public char[] charContents() {
    if (contents == null || contents.length == 0) return new char[0];

    char[] result = new char[contents.length];
    for (int i = 0, len = contents.length; i < len; i++) {
      result[i] = (char) contents[i];
    }
    return result;
  }
}
