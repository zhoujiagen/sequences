package com.spike.giantdataanalysis.sequences.rm.file.core;

public final class BLOCK {

  public static class BLOCKHEAD {
    public int flip = 0; // write check mode - before
    public int fileno = -1; // FILENO: file no
    public int blockno = -1; // BLOCKNO: block no in file
  }

  public BLOCKHEAD header = new BLOCKHEAD(); // block heaer
  public byte[] contents = new byte[0];// content
  public int flop = header.flip; // write check mode - after

}
