package com.spike.giantdataanalysis.sequences.fs.java.tpfs;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class TPFileSystem extends FileSystem {

  private TPFSCB cb = TPFSCB.I();
  private Map<String, ?> env;

  private List<FileStore> fileStores = Lists.newArrayList();
  private volatile boolean opened = false;

  public TPFileSystem() {
    this.init();
  }

  public TPFileSystem(Map<String, ?> env) {
    this.env = env;

    this.init();
  }

  private void init() {
    int fileStoreSize = TPFSConfiguration.storeSize;
    Preconditions.checkArgument(fileStoreSize > 0, "No enough space when start.");
    for (int i = 0; i < fileStoreSize; i++) {
      fileStores.add(new TPFileStore(i));
    }

    cb.setFileSystem(this);
    opened = true;
  }

  @Override
  public void close() throws IOException {
    opened = false;
  }

  @Override
  public Iterable<FileStore> getFileStores() {
    return fileStores;
  }

  @Override
  public TPPath getPath(String first, String... more) {
    return TPPath.newPath(first, more);
  }

  @Override
  public PathMatcher getPathMatcher(String syntaxAndPattern) {
    // TODO Implement TPFileSystem.getPathMatcher
    return null;
  }

  @Override
  public Iterable<Path> getRootDirectories() {
    // TODO Implement TPFileSystem.getRootDirectories
    return null;
  }

  @Override
  public String getSeparator() {
    return "\n";
  }

  @Override
  public UserPrincipalLookupService getUserPrincipalLookupService() {
    // TODO Implement TPFileSystem.getUserPrincipalLookupService
    return null;
  }

  @Override
  public boolean isOpen() {
    // TODO Implement TPFileSystem.isOpen
    return false;
  }

  @Override
  public boolean isReadOnly() {
    return false;
  }

  @Override
  public WatchService newWatchService() throws IOException {
    // TODO Implement TPFileSystem.newWatchService
    return null;
  }

  @Override
  public FileSystemProvider provider() {
    return new TPFileSystemProvider();
  }

  @Override
  public Set<String> supportedFileAttributeViews() {
    // TODO Implement TPFileSystem.supportedFileAttributeViews
    return null;
  }

}
