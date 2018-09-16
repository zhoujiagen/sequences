package com.spike.giantdataanalysis.db.filesystem.core;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.db.commons.cache.CacheKey;

/**
 * File Block Abstraction.
 */
public class FileBlockEntity implements CacheKey {

  private final FileEntity fileEntity;
  private final int blockIndex;
  private AbstractFileBlockHeader header;
  private FileBlockTail tail;
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

  public AbstractFileBlockHeader getHeader() {
    return header;
  }

  public void setHeader(AbstractFileBlockHeader header) {
    this.header = header;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public FileBlockTail getTail() {
    return tail;
  }

  public void setTail(FileBlockTail tail) {
    this.tail = tail;
  }

  @Override
  public String toString() {
    return "Block [file=" + fileEntity.getName() + ", blockIndex=" + blockIndex + "]";
  }

}
