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
import com.spike.giantdataanalysis.sequences.fs.java.TPSeekableByteChannel;

// [FSP-1] Create a custom file system provider class, such as MyFileSystemProvider, that extends
// the java.nio.file.spi.FileSystemProvider class.
/**
 * A {@link FileSystemProvider} based on {@link java.io.RandomAccessFile}.
 */
public class TPFileSystemProvider extends FileSystemProvider {

  public TPFileSystemProvider() {
    try {
      TPFileSystem fileSystem = newFileSystem(URI.create(TPFSConfiguration.FS_SCHEMA_PATH),
        Maps.<String, Object> newHashMap());
      TPFSCB.I().setFileSystem(fileSystem);
      TPFSCB.I().setTPFileSystemProvider(this);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void checkAccess(Path path, AccessMode... modes) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void copy(Path source, Path target, CopyOption... options) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
    Preconditions.checkArgument(dir instanceof TPPath);
    ((TPPath) dir).toFile().mkdirs();
  }

  @Override
  public void delete(Path path) throws IOException {
    Preconditions.checkArgument(path instanceof TPPath);
    ((TPPath) path).toFile().delete();
  }

  @Override
  public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type,
      LinkOption... options) {
    throw new UnsupportedOperationException();
  }

  @Override
  public FileStore getFileStore(Path path) throws IOException {
    return Lists.newArrayList(TPFSCB.I().getFileSystem().getFileStores())
        .get((int) ((path.normalize().hashCode() % TPFSConfiguration.storeSize)));
  }

  // [FSP-5] Implement the getFileSystem method. This method should search the cache and return a
  // previously created instance of a file system that corresponds to the given URI.
  @Override
  public FileSystem getFileSystem(URI uri) {
    String schema = uri.getScheme();
    Preconditions.checkArgument(getScheme().equals(schema),
      "not support schema " + schema + ", only " + getScheme() + " supported");
    return TPFSCB.I().getFileSystem();
  }

  @Override
  public Path getPath(URI uri) {
    return TPFSCB.I().getFileSystem().getPath(uri.getPath());
  }

  // [FSP-2] Define a URI scheme such as jar for the file system provider. The getScheme method
  // should return the URI scheme of this provider.
  @Override
  public String getScheme() {
    return TPFSConfiguration.FS_SCHEMA;
  }

  @Override
  public boolean isHidden(Path path) throws IOException {
    return false;
  }

  @Override
  public boolean isSameFile(Path path, Path path2) throws IOException {
    Preconditions.checkArgument(path instanceof TPPath);
    Preconditions.checkArgument(path2 instanceof TPPath);
    return ((TPPath) path).normalize().compareTo(((TPPath) path2).normalize()) == 0;
  }

  @Override
  public void move(Path source, Path target, CopyOption... options) throws IOException {
    throw new UnsupportedOperationException();
  }

  // [FSP-6] Implement the newFileChannel method or the newAsynchronousFileChannel method depending
  // on the requirements of your file system provider. This method should return a FileChannel
  // object that allows a file to be read or written in the file system.
  @Override
  public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options,
      FileAttribute<?>... attrs) throws IOException {
    return new TPSeekableByteChannel(path.toRealPath().toFile(), "rw");
  }

  @Override
  public DirectoryStream<Path> newDirectoryStream(Path dir, Filter<? super Path> filter)
      throws IOException {
    throw new UnsupportedOperationException();
  }

  // [FSP-4] Implement the newFileSystem method. The method will create a new custom file system at
  // the specified path and add the file system to cache. This method should throw a
  // java.nio.file.FileSystemAlreadyExistsException exception if a file system already exists at the
  // specified path.
  @Override
  public TPFileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
    String schema = uri.getScheme();
    Preconditions.checkArgument(getScheme().equals(schema),
      "not support schema " + schema + ", only " + getScheme() + " supported");

    TPFileSystem tpFileSystem = new TPFileSystem(env);
    TPFSCB.I().setFileSystem(tpFileSystem);
    return TPFSCB.I().getFileSystem();
  }

  @Override
  public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type,
      LinkOption... options) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options)
      throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setAttribute(Path path, String attribute, Object value, LinkOption... options)
      throws IOException {
    throw new UnsupportedOperationException();
  }

}
