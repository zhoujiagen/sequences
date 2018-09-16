package com.spike.giantdataanalysis.db.filesystem.core;

public class FileRecordEntity {

  private FileBlockEntity blockEntity;
  private int recordIndex;
  private FileRecordHeader header;
  private FileRecordTail tail;
  private byte[] data = new byte[0];

  public FileRecordEntity(FileBlockEntity blockEntity, int recordIndex) {
    this.blockEntity = blockEntity;
    this.recordIndex = recordIndex;
  }

  public FileBlockEntity getBlockEntity() {
    return blockEntity;
  }

  public void setBlockEntity(FileBlockEntity blockEntity) {
    this.blockEntity = blockEntity;
  }

  public int getRecordIndex() {
    return recordIndex;
  }

  public void setRecordIndex(int recordIndex) {
    this.recordIndex = recordIndex;
  }

  public FileRecordHeader getHeader() {
    return header;
  }

  public void setHeader(FileRecordHeader header) {
    this.header = header;
  }

  public FileRecordTail getTail() {
    return tail;
  }

  public void setTail(FileRecordTail tail) {
    this.tail = tail;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

}
