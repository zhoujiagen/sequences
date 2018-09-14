package com.spike.giantdataanalysis.sequences.filesystem.core;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.sequences.filesystem.core.cache.CacheKey;

/**
 * File Block Abstraction.
 */
public class FileBlockEntity implements CacheKey {

  private FileEntity fileEntity;
  private int blockIndex;
  private FileBlockHeader header;
  private byte[] data = new byte[0];

  public FileBlockEntity(FileEntity fileEntity, int blockIndex) {
    Preconditions.checkArgument(fileEntity != null);
    Preconditions.checkArgument(blockIndex >= 0);

    this.fileEntity = fileEntity;
    this.blockIndex = blockIndex;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + blockIndex;
    result = prime * result + ((fileEntity == null) ? 0 : fileEntity.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    FileBlockEntity other = (FileBlockEntity) obj;
    if (blockIndex != other.blockIndex) return false;
    if (fileEntity == null) {
      if (other.fileEntity != null) return false;
    } else if (!fileEntity.equals(other.fileEntity)) return false;
    return true;
  }

  public FileEntity getFileEntity() {
    return fileEntity;
  }

  public int getBlockIndex() {
    return blockIndex;
  }

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

  @Override
  public String toString() {
    return "Block [file=" + fileEntity.getName() + ", blockIndex=" + blockIndex + "]";
  }

}
