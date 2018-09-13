package com.spike.giantdataanalysis.sequences.filesystem.core;

/**
 * File Block Abstraction.
 */
public class FileBlockEntity {
  private FileBlockHeader header;
  private byte[] data = new byte[0];

  public FileBlockHeader getHeader() {
    return header;
  }

  public void setHeader(FileBlockHeader header) {
    this.header = header;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

}
