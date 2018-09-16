package com.spike.giantdataanalysis.fs.java;

import java.util.List;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.fs.java.catalog.TPDentry;
import com.spike.giantdataanalysis.fs.java.catalog.TPFileDescriptor;
import com.spike.giantdataanalysis.fs.java.catalog.TPINode;

public final class TPFSCB {
  private static TPFSCB INSTANCE = new TPFSCB();

  // [FSP-3] Create an internal cache to keep track of file systems created by this provider.
  TPFileSystemProvider fileSystemProvider;
  TPFileSystem fileSystem;

  List<TPINode> inodes = Lists.newLinkedList();
  List<TPDentry> dentries = Lists.newLinkedList();
  List<TPFileDescriptor> fileDescriptors = Lists.newLinkedList();

  // public TPPath getPath(String first, String... more) {
  //
  // }

  private TPFSCB() {
  }

  public static TPFSCB I() {
    return INSTANCE;
  }

  public void setFileSystem(TPFileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  public TPFileSystem getFileSystem() {
    return fileSystem;
  }

  public void setTPFileSystemProvider(TPFileSystemProvider provider) {
    this.fileSystemProvider = provider;
  }

}
