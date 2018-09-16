package com.spike.giantdataanalysis.db.filesystem;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.SyncFailedException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.db.commons.data.MoreBytes;
import com.spike.giantdataanalysis.db.filesystem.configuration.FileSystemConfiguration;
import com.spike.giantdataanalysis.db.filesystem.core.FileAccessModeEnum;
import com.spike.giantdataanalysis.db.filesystem.core.FileAllocationParameter;
import com.spike.giantdataanalysis.db.filesystem.core.FileBlockEntity;
import com.spike.giantdataanalysis.db.filesystem.core.FileEntity;
import com.spike.giantdataanalysis.db.filesystem.core.cache.CacheTargetEnum;
import com.spike.giantdataanalysis.db.filesystem.core.cache.FileSystemCache;
import com.spike.giantdataanalysis.db.filesystem.exception.FileSystemException;

/**
 * File System implemented in Java: just a demonstration.
 */
public class LocalFileSystem implements IFileSystem {

  private static final Logger LOG = LoggerFactory.getLogger(LocalFileSystem.class);

  // ---------------------------------------------------------------------------
  // state
  // ---------------------------------------------------------------------------

  private FileSystemConfiguration configuration;
  private int blockSizeInByte;

  private final FileCatalogManager fileCatalogManager;

  // ---------------------------------------------------------------------------
  // constructor
  // ---------------------------------------------------------------------------

  public LocalFileSystem(FileCatalogManager fileCatalogManager) {
    this.fileCatalogManager = fileCatalogManager;
    this.configuration = fileCatalogManager.getConfiguration();
    this.blockSizeInByte = configuration.getCatalogConfiguration().getBlockSizeInByte();

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
   * Note: cache state only when file is successfully opened.
   */
  @Override
  public FileEntity open(String filename, FileAccessModeEnum accessMode) {

    if (LOG.isDebugEnabled()) {
      LOG.debug("open {} with mode: {}", filename, accessMode);
    }

    File file = Paths.get(filename).toFile();
    if (!file.exists()) {
      if (FileAccessModeEnum.R.equals(accessMode) || FileAccessModeEnum.A.equals(accessMode)) {
        throw FileSystemException.newE("invalid operation: " + filename + " does not exist!");
      }
    }

    FileEntity fileEntity = fileCatalogManager.queryFileCache(filename);
    if (fileEntity != null) {
      Preconditions.checkState(
        fileEntity.getAccessMode() != null
            && FileAccessModeEnum.isCompatible(fileEntity.getAccessMode(), accessMode), //
        "file " + filename + " has been opened with mode: " + fileEntity.getAccessMode()
            + ", so cannot be opened with mode: " + accessMode + "!");
      return fileEntity;
    }

    fileEntity = new FileEntity(filename, accessMode);

    try {
      fileEntity.setHandler(new RandomAccessFile(file, accessMode.rafMode()));
      if (FileAccessModeEnum.A.equals(accessMode)) {
        fileEntity.getHandler().seek(fileEntity.getHandler().length());
      }
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    }

    fileEntity.setNumber(fileCatalogManager.getOrCreateFileId(filename));
    fileCatalogManager.cacheFile(filename, fileEntity);
    return fileEntity;
  }

  @Override
  public void close(FileEntity fileEntity) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("close file {}", fileEntity);
    }

    try {
      fileEntity.getHandler().close();
      fileCatalogManager.removeFileCache(fileEntity.getName());
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
    FileBlockEntity result = new FileBlockEntity(fileEntity, blockIndex);
    if (LOG.isDebugEnabled()) {
      LOG.debug("read file {}", fileEntity);
    }

    ReadLock rLock = null;
    try {

      int startBlockByteOffset = blockSizeInByte * blockIndex;

      fileEntity.getHandler().seek(startBlockByteOffset);
      FileBlockHeader blockHeader = new FileBlockHeader(blockSizeInByte, result);
      result.setHeader(blockHeader);

      rLock = blockHeader.lock().readLock();
      rLock.lock();
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
      if (rLock != null) {
        rLock.unlock();
      }
    }

    return result;
  }

  @Override
  public FileBlockEntity readLine(FileEntity fileEntity, int blockIndex) {
    FileBlockEntity result = new FileBlockEntity(fileEntity, blockIndex);
    if (LOG.isDebugEnabled()) {
      LOG.debug("read file next line {}", fileEntity);
    }

    ReadLock rLock = null;

    try {
      fileEntity.getHandler().seek(blockSizeInByte * blockIndex);
      FileBlockHeader blockHeader = new FileBlockHeader(blockSizeInByte, result);
      result.setHeader(blockHeader);

      rLock = blockHeader.lock().readLock();
      rLock.lock();
      String line = fileEntity.getHandler().readLine();
      if (line != null) {
        result.setData(line.getBytes());
      }
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    } finally {
      if (rLock != null) {
        rLock.unlock();
      }
    }

    return result;

  }

  @Override
  public List<FileBlockEntity> readc(FileEntity fileEntity, int blockIndex, int blockCount) {
    List<FileBlockEntity> result = Lists.newArrayList();

    ReadLock rLock = null;

    try {
      fileEntity.getHandler().seek(blockSizeInByte * blockIndex);
      for (int i = 0; i < blockCount; i++) {
        FileBlockEntity fileBlockEntity = new FileBlockEntity(fileEntity, blockIndex + i);
        byte[] bytes = new byte[blockSizeInByte];
        fileEntity.getHandler().seek(blockSizeInByte * blockIndex);
        FileBlockHeader blockHeader = new FileBlockHeader(blockSizeInByte, fileBlockEntity);
        fileBlockEntity.setHeader(blockHeader);

        rLock = blockHeader.lock().readLock();
        rLock.lock();
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
      if (rLock != null) {
        rLock.unlock();
      }
    }

    return result;
  }

  @Override
  public void write(FileEntity fileEntity, int blockIndex, byte[] data) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("write to file {}, content = {}", fileEntity, new String(data));
    }

    if (fileEntity.getAccessMode() == FileAccessModeEnum.R) {
      throw FileSystemException
          .newE("invalid operation: cannot write when using read access mode!");
    }

    WriteLock wLock = null;
    try {
      int blockStartByteOffset = blockSizeInByte * blockIndex;
      int blockEndByteOffset = blockStartByteOffset + blockSizeInByte;
      fileEntity.getHandler().seek(blockStartByteOffset);

      // read file block header
      FileBlockEntity fileBlockEntity = new FileBlockEntity(fileEntity, blockIndex);
      FileBlockHeader fileBlockHeader = new FileBlockHeader(blockSizeInByte, fileBlockEntity);

      wLock = fileBlockHeader.lock().writeLock();
      wLock.lock();
      int toWriteByteCount = data.length;
      if (fileBlockHeader.getFreeByteOffset() + toWriteByteCount > blockEndByteOffset) {
        throw FileSystemException.newE("no enough space in block: use [writec] instead!");
      }

      // append
      if (FileAccessModeEnum.A.equals(fileEntity.getAccessMode())) {
        fileEntity.getHandler().seek(blockStartByteOffset + fileBlockHeader.getFreeByteOffset());
        fileEntity.getHandler().write(data, 0, toWriteByteCount);
        fileBlockHeader.setFreeOffset(toWriteByteCount + fileBlockHeader.getFreeByteOffset());
      }
      // overwrite
      else if (FileAccessModeEnum.U.equals(fileEntity.getAccessMode())) {
        fileEntity.getHandler().seek(blockStartByteOffset + fileBlockHeader.getReprByteSize());
        fileEntity.getHandler().write(data, 0, toWriteByteCount);
        fileBlockHeader.setFreeOffset(toWriteByteCount + fileBlockHeader.getReprByteSize());
      }

      // update file block header
      if (LOG.isDebugEnabled()) {
        LOG.debug("update block header: " + fileBlockHeader);
      }
      fileEntity.getHandler().seek(blockStartByteOffset);
      fileEntity.getHandler().write(fileBlockHeader.toBytes());
      // update cache
      FileSystemCache.I().put(CacheTargetEnum.FILE_BLOCK, fileBlockEntity, fileBlockHeader);

    } catch (Exception e) {
      throw FileSystemException.newE(e);
    } finally {
      if (wLock != null) {
        wLock.unlock();
      }
    }

  }

  @Override
  public void writec(FileEntity fileEntity, int blockIndex, byte[] data) {

    if (fileEntity.getAccessMode() == FileAccessModeEnum.R) {
      throw FileSystemException
          .newE("invalid operation: cannot write when using read access mode!");
    }

    WriteLock wLock = null;

    try {
      final int blockStartByteOffset = blockSizeInByte * blockIndex;
      final int totalByteCount = data.length;

      // [1] overwrite
      if (FileAccessModeEnum.U.equals(fileEntity.getAccessMode())) {

        int currentBlockStartByteOffset = blockStartByteOffset;
        int currentWriteByteIndex = 0;
        int currentBlockIndex = blockIndex;
        while (currentWriteByteIndex < totalByteCount) {

          // clean cache
          FileSystemCache.I().remove(CacheTargetEnum.FILE_BLOCK,
            new FileBlockEntity(fileEntity, currentBlockIndex));

          // read file block header
          fileEntity.getHandler().seek(currentBlockStartByteOffset);
          FileBlockHeader fileBlockHeader =
              new FileBlockHeader(blockSizeInByte, new FileBlockEntity(fileEntity,
                  currentBlockIndex));

          int currentWriteByteCount = blockSizeInByte - fileBlockHeader.getReprByteSize();

          // TODO(zhoujiagen) unlimited file size
          // introduce a max block count parameter in configuration???
          fileEntity.getHandler().seek(
            currentBlockStartByteOffset + fileBlockHeader.getFreeByteOffset());
          if (currentWriteByteIndex + currentWriteByteCount > totalByteCount) {
            currentWriteByteCount = totalByteCount - currentWriteByteIndex;
          }
          if (LOG.isDebugEnabled()) {
            LOG.debug("write {} bytes to {}", currentWriteByteCount, fileEntity);
          }
          wLock = fileBlockHeader.lock().writeLock();
          wLock.lock();
          fileEntity.getHandler().write(data, currentWriteByteIndex, currentWriteByteCount);

          // update file block header
          fileBlockHeader
              .setFreeOffset(fileBlockHeader.getFreeByteOffset() + currentWriteByteCount);
          fileEntity.getHandler().seek(currentBlockStartByteOffset);
          if (LOG.isDebugEnabled()) {
            LOG.debug("update block header {} to file {} ", fileBlockHeader, fileEntity);
          }
          fileEntity.getHandler().write(fileBlockHeader.toBytes());
          FileSystemCache.I().put(CacheTargetEnum.FILE_BLOCK,
            new FileBlockEntity(fileEntity, currentBlockIndex), fileBlockHeader);

          // update loop parameter
          currentWriteByteIndex += currentWriteByteCount;
          currentBlockStartByteOffset += blockSizeInByte;
          currentBlockIndex++;
        }
      }

      // [2] append since blockIndex
      else if (FileAccessModeEnum.A.equals(fileEntity.getAccessMode())) {

        int currentBlockStartByteOffset = blockStartByteOffset;
        int nextBlockStartByteOffset = currentBlockStartByteOffset + blockSizeInByte;

        // read file block header to find empty block to append all data
        fileEntity.getHandler().seek(currentBlockStartByteOffset);
        FileBlockHeader currentFileBlockHeader =
            new FileBlockHeader(blockSizeInByte, new FileBlockEntity(fileEntity, blockIndex));
        fileEntity.getHandler().seek(nextBlockStartByteOffset);
        FileBlockHeader nextFileBlockHeader =
            new FileBlockHeader(blockSizeInByte, new FileBlockEntity(fileEntity, blockIndex + 1));

        int currentBlockIndex = blockIndex;
        while (nextFileBlockHeader.getFreeByteOffset() > nextFileBlockHeader.getReprByteSize()) {
          currentBlockStartByteOffset += blockSizeInByte;
          currentFileBlockHeader = nextFileBlockHeader;

          nextBlockStartByteOffset += blockSizeInByte;
          fileEntity.getHandler().seek(nextBlockStartByteOffset);
          nextFileBlockHeader =
              new FileBlockHeader(blockSizeInByte, new FileBlockEntity(fileEntity,
                  currentBlockIndex++));
        }

        // append data since currentFileBlockHeader
        fileEntity.getHandler().seek(
          currentBlockStartByteOffset + currentFileBlockHeader.getFreeByteOffset());

        int currentAppendByteIndex = 0;
        while (currentAppendByteIndex < totalByteCount) {

          // read file block header
          fileEntity.getHandler().seek(currentBlockStartByteOffset);
          FileBlockHeader fileBlockHeader =
              new FileBlockHeader(blockSizeInByte, new FileBlockEntity(fileEntity,
                  currentBlockIndex));

          wLock = fileBlockHeader.lock().writeLock();
          wLock.lock();

          int currentAppendByteCount = blockSizeInByte - fileBlockHeader.getFreeByteOffset();

          // TODO(zhoujiagen) unlimited file size
          // introduce a max block count parameter in configuration???
          fileEntity.getHandler().seek(
            currentBlockStartByteOffset + fileBlockHeader.getFreeByteOffset());
          if (currentAppendByteIndex + currentAppendByteCount > totalByteCount) { // tail
            currentAppendByteCount = totalByteCount - currentAppendByteIndex;
          }
          if (LOG.isDebugEnabled()) {
            LOG.debug("append {} bytes to {}", currentAppendByteCount, fileEntity);
          }
          fileEntity.getHandler().write(data, currentAppendByteIndex, currentAppendByteCount);

          // update file block header
          fileBlockHeader.setFreeOffset(fileBlockHeader.getFreeByteOffset()
              + currentAppendByteCount);
          fileEntity.getHandler().seek(currentBlockStartByteOffset);
          if (LOG.isDebugEnabled()) {
            LOG.debug("update block header {} to file {} ", fileBlockHeader, fileEntity);
          }
          fileEntity.getHandler().write(fileBlockHeader.toBytes());
          FileSystemCache.I().put(CacheTargetEnum.FILE_BLOCK,
            new FileBlockEntity(fileEntity, currentBlockIndex), fileBlockHeader);

          // update loop parameter
          currentAppendByteIndex += currentAppendByteCount;
          currentBlockStartByteOffset += blockSizeInByte;
          currentBlockIndex++;
        }

      }

    } catch (Exception e) {
      throw FileSystemException.newE(e);
    } finally {
      if (wLock != null) {
        wLock.unlock();
      }
    }
  }

  @Override
  public void flush(FileEntity fileEntity) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("flush file {}, ", fileEntity);
    }

    try {
      fileEntity.getHandler().getFD().sync();
    } catch (SyncFailedException e) {
      throw FileSystemException.newE(e);
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    }
  }

}
