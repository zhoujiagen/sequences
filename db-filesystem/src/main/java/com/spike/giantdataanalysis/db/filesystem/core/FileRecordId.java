package com.spike.giantdataanalysis.db.filesystem.core;

public class FileRecordId {
  private final FileBlockId fileBlockId;
  private final int recordIndex;

  public static FileRecordId DEFAULT = new FileRecordId(FileBlockId.DEFAULT, -1);

  public FileRecordId(FileBlockId fileBlockId, int recordIndex) {
    this.fileBlockId = fileBlockId;
    this.recordIndex = recordIndex;
  }

  public FileBlockId getFileBlockId() {
    return fileBlockId;
  }

  public int getRecordIndex() {
    return recordIndex;
  }

}
