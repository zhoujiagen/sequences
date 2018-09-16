package com.spike.giantdataanalysis.fs.java;

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

// [FS-1] Create a class, such as MyFileSystem, that extends the java.nio.file.FileSystem class.
public class TPFileSystem extends FileSystem {

  @SuppressWarnings("unused")
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

    opened = true;
  }

  @Override
  public void close() throws IOException {
    opened = false;
  }

  // [FS-2.3] File store – A file system requires an underlying file store. The attributes that can
  // be set for a file will vary depending on the underlying file store.
  @Override
  public Iterable<FileStore> getFileStores() {
    Preconditions.checkState(opened);
    return fileStores;
  }

  @Override
  public TPPath getPath(String first, String... more) {
    Preconditions.checkState(opened);
    return TPPath.newPath(first, more);
  }

  @Override
  public PathMatcher getPathMatcher(String syntaxAndPattern) {
    Preconditions.checkState(opened);
    return null;
  }

  // [FS-2.1] Number of roots – A file system can have a single hierarchy of files with one root, or
  // multiple hierarchies.
  @Override
  public Iterable<Path> getRootDirectories() {
    return null;
  }

  @Override
  public String getSeparator() {
    return "\n";
  }

  @Override
  public UserPrincipalLookupService getUserPrincipalLookupService() {
    return null;
  }

  @Override
  public boolean isOpen() {
    return opened;
  }

  // [FS-2.2] Read and write access – A file system can be read-only or read/write.
  @Override
  public boolean isReadOnly() {
    return false;
  }

  @Override
  public WatchService newWatchService() throws IOException {
    return null;
  }

  @Override
  public FileSystemProvider provider() {
    return new TPFileSystemProvider();
  }

  @Override
  public Set<String> supportedFileAttributeViews() {
    return null;
  }

}
