package com.spike.giantdataanalysis.db.filesystem.core;

public class FileBlockTailRecordPtr {
  private FileRecordId fileRecordId;
  private int recordOffsetInBlock;

  public static FileBlockTailRecordPtr DEFAULT = new FileBlockTailRecordPtr(FileRecordId.DEFAULT,
      -1);

  public FileBlockTailRecordPtr(FileRecordId fileRecordId, int recordOffsetInBlock) {
    this.fileRecordId = fileRecordId;
    this.recordOffsetInBlock = recordOffsetInBlock;
  }

  // private int recordSize;
  public FileRecordId getFileRecordId() {
    return fileRecordId;
  }

  public void setFileRecordId(FileRecordId fileRecordId) {
    this.fileRecordId = fileRecordId;
  }

  public int getRecordOffsetInBlock() {
    return recordOffsetInBlock;
  }

  public void setRecordOffsetInBlock(int recordOffsetInBlock) {
    this.recordOffsetInBlock = recordOffsetInBlock;
  }

}
