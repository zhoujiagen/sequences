package com.spike.giantdataanalysis.fs.java;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;

import com.google.common.base.Preconditions;

public class TPFileStore extends FileStore {

  private long size;
  private long allocateSize;
  private int index;

  public TPFileStore(int index) {
    this.index = index;
    this.size = TPFSConfiguration.sizePerStore;
  }

  public void allocate(long size) {
    Preconditions.checkArgument(size - allocateSize - size > 0, "No enough space.");
    allocateSize += size;
  }

  @Override
  public Object getAttribute(String attribute) throws IOException {
    return null;
  }

  @Override
  public <V extends FileStoreAttributeView> V getFileStoreAttributeView(Class<V> type) {
    return null;
  }

  @Override
  public long getTotalSpace() throws IOException {
    return size;
  }

  @Override
  public long getUnallocatedSpace() throws IOException {
    return (size - allocateSize);
  }

  @Override
  public long getUsableSpace() throws IOException {
    return getUnallocatedSpace();
  }

  @Override
  public boolean isReadOnly() {
    return false;
  }

  @Override
  public String name() {
    return TPFSConfiguration.STORE_NAME_PREFIX + index;
  }

  @Override
  public boolean supportsFileAttributeView(Class<? extends FileAttributeView> type) {
    return false;
  }

  @Override
  public boolean supportsFileAttributeView(String name) {
    return false;
  }

  @Override
  public String type() {
    return TPFSConfiguration.STORE_TYPE;
  }

}
