package com.spike.giantdataanalysis.fs.java;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class TPPath implements Path {

  private List<String> paths = Lists.newArrayList();

  public TPPath() {
  }

  private static String ROOT = TPFSConfiguration.PATH_ROOT;
  private static String SEP = TPFSConfiguration.PATH_SEP;

  public TPPath(String first, String... more) {
    Preconditions.checkArgument(first.startsWith(ROOT), "only support absolute path");

    for (String path : Splitter.on(SEP).splitToList(first)) {
      if (!"".equals(path.trim())) {
        paths.add(path);
      }
    }

    if (more.length > 0) {
      for (String m : more) {
        for (String path : Splitter.on(SEP).splitToList(m)) {
          if (!"".equals(path.trim())) {
            paths.add(path);
          }
        }
      }
    }
  }

  public static TPPath newPath(String first, String... more) {
    return new TPPath(first, more);
  }

  @Override
  public int compareTo(Path other) {
    return Joiner.on(SEP).join(paths).compareTo(Joiner.on(SEP).join(other));
  }

  @Override
  public boolean endsWith(Path other) {
    return Joiner.on(SEP).join(paths).endsWith(Joiner.on(SEP).join(other));
  }

  @Override
  public boolean endsWith(String other) {
    return Joiner.on(SEP).join(paths).endsWith(other);
  }

  @Override
  public Path getFileName() {
    if (paths.size() == 0) return null;
    return TPPath.newPath(ROOT + paths.get(paths.size() - 1));
  }

  @Override
  public TPFileSystem getFileSystem() {
    return TPFSCB.I().getFileSystem();
  }

  @Override
  public Path getName(int index) {
    return TPPath.newPath(ROOT + paths.get(index));
  }

  @Override
  public int getNameCount() {
    return paths.size();
  }

  @Override
  public TPPath getParent() {
    if (paths.size() == 0) return getRoot();
    return subpath(0, paths.size() - 1);
  }

  @Override
  public TPPath getRoot() {
    return TPPath.newPath(ROOT + paths.get(0));
  }

  @Override
  public boolean isAbsolute() {
    return true;
  }

  @Override
  public Iterator<Path> iterator() {
    List<Path> _paths = Lists.newArrayList();
    for (String path : paths) {
      _paths.add(TPPath.newPath(ROOT + path));
    }
    return _paths.iterator();
  }

  @Override
  public TPPath normalize() {
    return TPPath.newPath(ROOT + Joiner.on(SEP).join(paths));
  }

  private String normalizePath() {
    return ROOT + Joiner.on(SEP).join(paths);
  }

  @Override
  public WatchKey register(WatchService watcher, Kind<?>... events) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers)
      throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public TPPath relativize(Path other) {
    throw new UnsupportedOperationException();
  }

  @Override
  public TPPath resolve(Path other) {
    return TPPath.newPath(normalizePath(), ((TPPath) other).normalizePath());
  }

  @Override
  public TPPath resolve(String other) {
    return TPPath.newPath(normalizePath(), other);
  }

  @Override
  public TPPath resolveSibling(Path other) {
    return TPPath.newPath(getParent().normalizePath(), ((TPPath) other).normalizePath());
  }

  @Override
  public TPPath resolveSibling(String other) {
    return TPPath.newPath(getParent().normalizePath(), other);
  }

  @Override
  public boolean startsWith(Path other) {
    return normalizePath().startsWith(((TPPath) other).normalizePath());
  }

  @Override
  public boolean startsWith(String other) {
    return normalizePath().startsWith(other);
  }

  @Override
  public TPPath subpath(int beginIndex, int endIndex) {
    List<String> sub = Lists.newArrayList();
    for (int i = beginIndex; i < endIndex; i++) {
      sub.add(paths.get(i));
    }
    return TPPath.newPath(ROOT + Joiner.on(SEP).join(sub));
  }

  @Override
  public TPPath toAbsolutePath() {
    return this;
  }

  @Override
  public File toFile() {
    // 转换为实际的路径: use underlying file system
    return Paths.get(TPFSConfiguration.STORE_ROOT + normalizePath()).toFile();
  }

  @Override
  public Path toRealPath(LinkOption... options) throws IOException {
    // 转换为实际的路径: use underlying file system
    return Paths.get(TPFSConfiguration.STORE_ROOT + normalizePath());
  }

  @Override
  public URI toUri() {
    return URI.create(TPFSConfiguration.FS_SCHEMA_PATH_PREFIX + normalizePath());
  }

}
