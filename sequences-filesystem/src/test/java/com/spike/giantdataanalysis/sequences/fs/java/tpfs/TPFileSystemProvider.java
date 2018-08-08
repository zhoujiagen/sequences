package com.spike.giantdataanalysis.sequences.fs.java.tpfs;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TPFileSystemProvider extends FileSystemProvider {

  private static final String SCHEMA = "tp";

  public TPFileSystemProvider() {
    TPFSCB.I().setTPFileSystemProvider(this);
  }

  @Override
  public void checkAccess(Path path, AccessMode... modes) throws IOException {
    // do nothing now
  }

  @Override
  public void copy(Path source, Path target, CopyOption... options) throws IOException {
    // TODO Implement TPFileSystemProvider.copy

  }

  @Override
  public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
    // TODO Implement TPFileSystemProvider.createDirectory

  }

  @Override
  public void delete(Path path) throws IOException {
    // TODO Implement TPFileSystemProvider.delete

  }

  @Override
  public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type,
      LinkOption... options) {
    // TODO Implement TPFileSystemProvider.getFileAttributeView
    return null;
  }

  @Override
  public FileStore getFileStore(Path path) throws IOException {
    return Lists.newArrayList(TPFSCB.I().getFileSystem().getFileStores())
        .get((int) ((path.normalize().hashCode() % TPFSConfiguration.storeSize)));
  }

  // CALLED when java.nio.file.FileSystems.getFileSystem(URI)
  @Override
  public FileSystem getFileSystem(URI uri) {
    String schema = uri.getScheme();
    Preconditions.checkArgument(SCHEMA.equals(schema),
      "not support schema " + schema + ", only " + SCHEMA + " supported");
    TPFileSystem tpFileSystem = new TPFileSystem(Maps.<String, Object> newHashMap());
    TPFSCB.I().setFileSystem(tpFileSystem);
    return TPFSCB.I().getFileSystem();
  }

  @Override
  public Path getPath(URI uri) {
    // TODO Implement TPFileSystemProvider.getPath
    return null;
  }

  @Override
  public String getScheme() {
    return SCHEMA;
  }

  @Override
  public boolean isHidden(Path path) throws IOException {
    // TODO Implement TPFileSystemProvider.isHidden
    return false;
  }

  @Override
  public boolean isSameFile(Path path, Path path2) throws IOException {
    // TODO Implement TPFileSystemProvider.isSameFile
    return false;
  }

  @Override
  public void move(Path source, Path target, CopyOption... options) throws IOException {
    // TODO Implement TPFileSystemProvider.move

  }

  @Override
  public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options,
      FileAttribute<?>... attrs) throws IOException {
    // TODO Implement TPFileSystemProvider.newByteChannel
    return null;
  }

  @Override
  public DirectoryStream<Path> newDirectoryStream(Path dir, Filter<? super Path> filter)
      throws IOException {
    // TODO Implement TPFileSystemProvider.newDirectoryStream
    return null;
  }

  @Override
  public TPFileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
    String schema = uri.getScheme();
    Preconditions.checkArgument(SCHEMA.equals(schema),
      "not support schema " + schema + ", only " + SCHEMA + " supported");

    return TPFSCB.I().getFileSystem();
  }

  @Override
  public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type,
      LinkOption... options) throws IOException {
    // TODO Implement TPFileSystemProvider.readAttributes
    return null;
  }

  @Override
  public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options)
      throws IOException {
    // TODO Implement TPFileSystemProvider.readAttributes
    return null;
  }

  @Override
  public void setAttribute(Path path, String attribute, Object value, LinkOption... options)
      throws IOException {
    // TODO Implement TPFileSystemProvider.setAttribute

  }

}
