package com.spike.giantdataanalysis.sequences.filesystem;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.sequences.commons.bytes.MoreBytes;
import com.spike.giantdataanalysis.sequences.filesystem.configuration.FileSystemConfiguration;
import com.spike.giantdataanalysis.sequences.filesystem.core.FileBlockEntity;
import com.spike.giantdataanalysis.sequences.filesystem.core.FileBlockHeader;
import com.spike.giantdataanalysis.sequences.filesystem.core.FileEntity;
import com.spike.giantdataanalysis.sequences.filesystem.exception.FileSystemException;

/**
 * File System implemented in Java: just a demonstration.
 * <p>
 * BLOCK ORGANIZATION(4KB): (bits)
 * 
 * <pre>
 * ------------------------------------------------------------------------------------------------
 * BLOCK START | BLOCK_SIZE(2)| FREE offset(13) | RECORD ...
 * ------------------------------------------------------------------------------------------------
 * </pre>
 */
public class LocalFileSystem implements IFileSystem {

  private static final Logger LOG = LoggerFactory.getLogger(LocalFileSystem.class);

  // ---------------------------------------------------------------------------
  // state
  // ---------------------------------------------------------------------------

  private FileSystemConfiguration configuration;
  private int blockSizeInByte;

  /** file name => file entity. */
  private static final Map<String, FileEntity> FILE_CACHE = Maps.newConcurrentMap();
  /** file number counter. */
  private static final AtomicInteger FILENO_SEQUENCE = new AtomicInteger(0);

  // ---------------------------------------------------------------------------
  // constructor
  // ---------------------------------------------------------------------------

  public LocalFileSystem(FileSystemConfiguration configuration) {
    this.configuration = configuration;
    this.blockSizeInByte = configuration.catalogConfiguration.blockSizeInByte;

    validateConfiguration();
  }

  private void validateConfiguration() {
    Preconditions.checkArgument(configuration != null);
  }

  // ---------------------------------------------------------------------------
  // file system methods
  // ---------------------------------------------------------------------------

  @Override
  public void create(String filename, FileAllocationParameter allocationParameter) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("create file {}", filename);
    }

    File file = Paths.get(filename).toFile();
    if (file.exists()) {
      throw FileSystemException.newE("file already exist!");
    }

    try {
      file.createNewFile();
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    }
  }

  @Override
  public void delete(String filename) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("delete file {}", filename);
    }

    File file = Paths.get(filename).toFile();
    file.delete();
  }

  /**
   * Note: cache state only when file is successfuly opened.
   */
  @Override
  public FileEntity open(String filename, FileAccessModeEnum accessMode) {

    FileEntity result = new FileEntity(filename, accessMode);
    if (LOG.isDebugEnabled()) {
      LOG.debug("open {} with mode: {}", filename, accessMode);
    }

    File file = Paths.get(filename).toFile();
    if (!file.exists()) {
      if (FileAccessModeEnum.R.equals(accessMode) || FileAccessModeEnum.A.equals(accessMode)) {
        throw FileSystemException.newE("invalid operation: " + filename + " does not exist!");
      }
    }

    FileEntity fileEntity = queryFileCache(filename);
    if (fileEntity != null && fileEntity.getAccessMode() != null
        && FileAccessModeEnum.isCompatible(fileEntity.getAccessMode(), accessMode)) {
      throw FileSystemException.newE("file " + filename + " has been opened with mode: "
          + fileEntity.getAccessMode() + ", so cannot be opened with mode: " + accessMode + "!");
    }

    try {
      result.setHandler(new RandomAccessFile(file, accessMode.rafMode()));
      if (FileAccessModeEnum.A.equals(accessMode)) {
        result.getHandler().seek(result.getHandler().length());
      }
      FILE_CACHE.put(filename, result);
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    }

    result.setNumber(FILENO_SEQUENCE.getAndIncrement());
    return result;
  }

  @Override
  public void close(FileEntity fileEntity) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("close file {}", fileEntity);
    }

    try {
      fileEntity.getHandler().close();
      removeFileCache(fileEntity.getName());
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    }
  }

  @Override
  public void extend(FileEntity fileEntity, FileAllocationParameter allocationParameter) {
    throw FileSystemException.newE(new UnsupportedOperationException());
  }

  @Override
  public FileBlockEntity read(FileEntity fileEntity, int blockIndex) {
    FileBlockEntity result = new FileBlockEntity();
    if (LOG.isDebugEnabled()) {
      LOG.debug("read file {}", fileEntity);
    }

    ReadLock rLock = fileEntity.getLock().readLock();
    rLock.lock();
    try {

      int startBlockByteOffset = blockSizeInByte * blockIndex;

      fileEntity.getHandler().seek(startBlockByteOffset);
      FileBlockHeader blockHeader = FileBlockHeader.fromHandler(fileEntity.getHandler());
      result.setHeader(blockHeader);
      int headerByteSize = blockHeader.getReprByteSize();
      // fileEntity.getHandler().skipBytes(headerByteSize);
      fileEntity.getHandler().seek(startBlockByteOffset + headerByteSize);
      int tryToReadByteSize = blockSizeInByte - headerByteSize;
      byte[] bytes = new byte[tryToReadByteSize];
      int readByteCount = fileEntity.getHandler().read(bytes, 0, tryToReadByteSize);
      if (readByteCount != -1) {
        result.setData(MoreBytes.copy(bytes, 0, readByteCount));
      }

      if (LOG.isDebugEnabled()) {
        LOG.debug("read out data: {}", new String(bytes));
      }

    } catch (IOException e) {
      throw FileSystemException.newE(e);
    } finally {
      rLock.unlock();
    }

    return result;
  }

  @Override
  public FileBlockEntity readLine(FileEntity fileEntity, int blockIndex) {
    FileBlockEntity result = new FileBlockEntity();
    if (LOG.isDebugEnabled()) {
      LOG.debug("read file next line {}", fileEntity);
    }

    ReadLock rLock = fileEntity.getLock().readLock();
    rLock.lock();
    try {
      fileEntity.getHandler().seek(blockSizeInByte * blockIndex);
      FileBlockHeader blockHeader = FileBlockHeader.fromHandler(fileEntity.getHandler());
      result.setHeader(blockHeader);
      String line = fileEntity.getHandler().readLine();
      if (line != null) {
        result.setData(line.getBytes());
      }
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    } finally {
      rLock.unlock();
    }

    return result;

  }

  @Override
  public List<FileBlockEntity> readc(FileEntity fileEntity, int blockIndex, int blockCount) {
    List<FileBlockEntity> result = Lists.newArrayList();

    ReadLock rLock = fileEntity.getLock().readLock();
    rLock.lock();
    try {
      fileEntity.getHandler().seek(blockSizeInByte * blockIndex);
      for (int i = 0; i < blockCount; i++) {
        FileBlockEntity fileBlockEntity = new FileBlockEntity();
        byte[] bytes = new byte[blockSizeInByte];
        fileEntity.getHandler().seek(blockSizeInByte * blockIndex);
        FileBlockHeader blockHeader = FileBlockHeader.fromHandler(fileEntity.getHandler());
        fileBlockEntity.setHeader(blockHeader);
        int readByteCount = fileEntity.getHandler().read(bytes, 0, blockSizeInByte);
        if (readByteCount != -1) {
          fileBlockEntity.setData(MoreBytes.copy(bytes, 0, readByteCount));
        } else {
          break;
        }
        result.add(fileBlockEntity);
      }
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    } finally {
      rLock.unlock();
    }

    return result;
  }

  @Override
  public void write(FileEntity fileEntity, int blockIndex, FileBlockEntity fileBlockEntity) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("write to file {}, content = {}", fileEntity,
        new String(fileBlockEntity.getData()));
    }

    if (fileEntity.getAccessMode() == FileAccessModeEnum.R) {
      throw FileSystemException
          .newE("invalid operation: cannot write when using read access mode!");
    }

    WriteLock wLock = fileEntity.getLock().writeLock(); // WARN: too raw lock!!!
    wLock.lock();
    try {
      int blockStartByteOffset = blockSizeInByte * blockIndex;
      int blockEndByteOffset = blockStartByteOffset + blockSizeInByte;
      fileEntity.getHandler().seek(blockStartByteOffset);

      // read file block header
      FileBlockHeader fileBlockHeader = FileBlockHeader.fromHandler(fileEntity.getHandler());

      int toWriteByteCount = fileBlockEntity.getData().length;
      if (fileBlockHeader.getFreeByteOffset() + toWriteByteCount > blockEndByteOffset) {
        throw FileSystemException.newE("no enough space in block: use [writec] instead!");
      }

      if (FileAccessModeEnum.A.equals(fileEntity.getAccessMode())) {

        fileEntity.getHandler().seek(blockStartByteOffset + fileBlockHeader.getFreeByteOffset());
        fileEntity.getHandler().write(fileBlockEntity.getData(), 0, toWriteByteCount);
        fileBlockHeader.setFreeOffset(toWriteByteCount + fileBlockHeader.getFreeByteOffset());

      } else if (FileAccessModeEnum.U.equals(fileEntity.getAccessMode())) {

        fileEntity.getHandler().seek(blockStartByteOffset + fileBlockHeader.getReprByteSize());
        fileEntity.getHandler().write(fileBlockEntity.getData(), 0, toWriteByteCount);
        fileBlockHeader.setFreeOffset(toWriteByteCount + fileBlockHeader.getReprByteSize());
      }

      // update file block header
      if (LOG.isDebugEnabled()) {
        LOG.debug("update block header: " + fileBlockHeader);
      }
      fileEntity.getHandler().seek(blockStartByteOffset);
      fileEntity.getHandler().write(fileBlockHeader.toBytes());

    } catch (Exception e) {
      throw FileSystemException.newE(e);
    } finally {
      wLock.unlock();
    }

  }

  @Override
  public void writec(FileEntity fileEntity, int blockIndex, FileBlockEntity fileBlockEntity) {

    if (fileEntity.getAccessMode() == FileAccessModeEnum.R) {
      throw FileSystemException
          .newE("invalid operation: cannot write when using read access mode!");
    }

    WriteLock wLock = fileEntity.getLock().writeLock(); // WARN: too raw lock!!!
    wLock.lock();
    try {
      final int blockStartByteOffset = blockSizeInByte * blockIndex;
      final int totalByteCount = fileBlockEntity.getData().length;

      // [1] overwrite
      if (FileAccessModeEnum.U.equals(fileEntity.getAccessMode())) {

        int currentBlockStartByteOffset = blockStartByteOffset;
        int currentWriteByteIndex = 0;
        while (currentWriteByteIndex < totalByteCount) {

          // read file block header
          fileEntity.getHandler().seek(currentBlockStartByteOffset);
          FileBlockHeader fileBlockHeader = FileBlockHeader.fromHandler(fileEntity.getHandler());

          int currentWriteByteCount = blockSizeInByte - fileBlockHeader.getReprByteSize();

          // TODO(zhoujiagen) unlimited file size
          // introduce a max block count parameter in configuration???
          fileEntity.getHandler()
              .seek(currentBlockStartByteOffset + fileBlockHeader.getFreeByteOffset());
          if (currentWriteByteIndex + currentWriteByteCount > totalByteCount) {
            currentWriteByteCount = totalByteCount - currentWriteByteIndex;
          }
          if (LOG.isDebugEnabled()) {
            LOG.debug("write {} bytes to {}", currentWriteByteCount, fileEntity);
          }
          fileEntity.getHandler().write(fileBlockEntity.getData(), currentWriteByteIndex,
            currentWriteByteCount);

          // update file block header
          fileBlockHeader
              .setFreeOffset(fileBlockHeader.getFreeByteOffset() + currentWriteByteCount);
          fileEntity.getHandler().seek(currentBlockStartByteOffset);
          if (LOG.isDebugEnabled()) {
            LOG.debug("update block header {} to file {} ", fileBlockHeader, fileEntity);
          }
          fileEntity.getHandler().write(fileBlockHeader.toBytes());

          // update loop parameter
          currentWriteByteIndex += currentWriteByteCount;
          currentBlockStartByteOffset += blockSizeInByte;
        }
      }

      // [2] append since blockIndex
      else if (FileAccessModeEnum.A.equals(fileEntity.getAccessMode())) {

        int currentBlockStartByteOffset = blockStartByteOffset;
        int nextBlockStartByteOffset = currentBlockStartByteOffset + blockSizeInByte;

        // read file block header to find empty block to append all data
        fileEntity.getHandler().seek(currentBlockStartByteOffset);
        FileBlockHeader currentFileBlockHeader =
            FileBlockHeader.fromHandler(fileEntity.getHandler());
        fileEntity.getHandler().seek(nextBlockStartByteOffset);
        FileBlockHeader nextFileBlockHeader = FileBlockHeader.fromHandler(fileEntity.getHandler());
        while (nextFileBlockHeader.getFreeByteOffset() > nextFileBlockHeader.getReprByteSize()) {
          currentBlockStartByteOffset += blockSizeInByte;
          currentFileBlockHeader = nextFileBlockHeader;

          nextBlockStartByteOffset += blockSizeInByte;
          fileEntity.getHandler().seek(nextBlockStartByteOffset);
          nextFileBlockHeader = FileBlockHeader.fromHandler(fileEntity.getHandler());
        }

        // append data since currentFileBlockHeader
        fileEntity.getHandler()
            .seek(currentBlockStartByteOffset + currentFileBlockHeader.getFreeByteOffset());

        int currentAppendByteIndex = 0;
        while (currentAppendByteIndex < totalByteCount) {

          // read file block header
          fileEntity.getHandler().seek(currentBlockStartByteOffset);
          FileBlockHeader fileBlockHeader = FileBlockHeader.fromHandler(fileEntity.getHandler());

          int currentAppendByteCount = blockSizeInByte - fileBlockHeader.getFreeByteOffset();

          // TODO(zhoujiagen) unlimited file size
          // introduce a max block count parameter in configuration???
          fileEntity.getHandler()
              .seek(currentBlockStartByteOffset + fileBlockHeader.getFreeByteOffset());
          if (currentAppendByteIndex + currentAppendByteCount > totalByteCount) { // tail
            currentAppendByteCount = totalByteCount - currentAppendByteIndex;
          }
          if (LOG.isDebugEnabled()) {
            LOG.debug("append {} bytes to {}", currentAppendByteCount, fileEntity);
          }
          fileEntity.getHandler().write(fileBlockEntity.getData(), currentAppendByteIndex,
            currentAppendByteCount);

          // update file block header
          fileBlockHeader
              .setFreeOffset(fileBlockHeader.getFreeByteOffset() + currentAppendByteCount);
          fileEntity.getHandler().seek(currentBlockStartByteOffset);
          if (LOG.isDebugEnabled()) {
            LOG.debug("update block header {} to file {} ", fileBlockHeader, fileEntity);
          }
          fileEntity.getHandler().write(fileBlockHeader.toBytes());

          // update loop parameter
          currentAppendByteIndex += currentAppendByteCount;
          currentBlockStartByteOffset += blockSizeInByte;
        }

      }

    } catch (Exception e) {
      throw FileSystemException.newE(e);
    } finally {
      wLock.unlock();
    }
  }

  @Override
  public void flush(FileEntity fileEntity) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("flush file {}, ", fileEntity);
    }
  }

  // ---------------------------------------------------------------------------
  // helper methods
  // ---------------------------------------------------------------------------

  private FileEntity queryFileCache(String filename) {
    return FILE_CACHE.get(filename);
  }

  private void removeFileCache(String filename) {
    FILE_CACHE.remove(filename);
  }

}
