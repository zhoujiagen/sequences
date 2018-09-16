package com.spike.giantdataanalysis.db.filesystem.core;

public class FileBlockId {
  private final int fileId;
  private final int blockIndex;

  public static FileBlockId DEFAULT = new FileBlockId(-1, -1);

  public FileBlockId(int fileId, int blockIndex) {
    this.fileId = fileId;
    this.blockIndex = blockIndex;
  }

  public int getFileId() {
    return fileId;
  }

  public int getBlockIndex() {
    return blockIndex;
  }

}
