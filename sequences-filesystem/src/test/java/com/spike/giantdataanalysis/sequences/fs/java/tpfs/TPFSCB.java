package com.spike.giantdataanalysis.sequences.fs.java.tpfs;

import java.nio.file.FileSystem;
import java.util.List;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.sequences.fs.java.tpfs.catalog.TPDentry;
import com.spike.giantdataanalysis.sequences.fs.java.tpfs.catalog.TPFileDescriptor;
import com.spike.giantdataanalysis.sequences.fs.java.tpfs.catalog.TPINode;

public final class TPFSCB {
  private static TPFSCB INSTANCE = new TPFSCB();

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
