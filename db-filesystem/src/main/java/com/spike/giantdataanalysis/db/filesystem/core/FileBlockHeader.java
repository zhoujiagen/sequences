package com.spike.giantdataanalysis.db.filesystem.core;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;
import com.spike.giantdataanalysis.db.commons.cache.CacheEntity;
import com.spike.giantdataanalysis.db.commons.serialize.Byteable;
import com.spike.giantdataanalysis.db.filesystem.core.cache.CacheTargetEnum;
import com.spike.giantdataanalysis.db.filesystem.core.cache.FileSystemCache;
import com.spike.giantdataanalysis.db.filesystem.exception.FileSystemException;

import fr.devnied.bitlib.BitUtils;

/**
 * File Block Header Abstraction.
 */
public class FileBlockHeader implements CacheEntity, Byteable<FileBlockHeader> {
  private static final long serialVersionUID = 1L;

  public static final int LENGTH_BLOCK_SIZE_TYPE = 2;

  public static final int DEFUALT_BLOCK_SIZE_TYPE = 0;

  /**
   * ORGANIZAITON(bits):
   * 
   * <pre>
   *  block id (?)
   *  block size type (2)
   *  block type (3)
   *  next block id (?)
   *  record count (?)
   *  free offset(13)
   * </pre>
   */

  private int blockSizeType = 0; // 0: 4KB, 1: 8KB, 2: 16KB
  private int freeByteOffset = 0;

  private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

  public FileBlockHeader(int blockSizeType, int freeByteOffset) {
    Preconditions.checkArgument(freeByteOffset >= getReprByteSize(blockSizeType));

    this.blockSizeType = blockSizeType;
    this.freeByteOffset = freeByteOffset;
  }

  public FileBlockHeader(int blockSizeType) {
    this.blockSizeType = blockSizeType;
    this.freeByteOffset = getReprByteSize(blockSizeType);
  }

  public ReentrantReadWriteLock lock() {
    return lock;
  }

  public void increaseFreeOffset(int delta) {
    freeByteOffset += delta;
  }

  public void setFreeOffset(int freeByteOffset) {
    this.freeByteOffset = freeByteOffset;
  }

  private static int getBlockSizeInByte(int blockSizeType) {
    switch (blockSizeType) {
    case 0:
      return 4 * 1024;
    case 1:
      return 8 * 1024;
    case 2:
      return 16 * 1024;
    default:
      return 4 * 1024;
    }
  }

  /**
   * @param blockSizeType
   * @return block header repr bit size
   */
  private static int getReprBitSize(int blockSizeType) {
    int byteSize = getBlockSizeInByte(blockSizeType);
    return Integer.numberOfTrailingZeros(byteSize) + 1;
  }

  public int getReprByteSize() {
    return getReprByteSize(blockSizeType);
  }

  /**
   * @param blockSizeType
   * @return block header repr byte size
   */
  private static int getReprByteSize(int blockSizeType) {
    int bitSize = getReprBitSize(blockSizeType) + LENGTH_BLOCK_SIZE_TYPE;
    int mod = bitSize % Byte.SIZE;
    if (mod == 0) {
      return bitSize / Byte.SIZE;
    } else {
      return bitSize / Byte.SIZE + 1;
    }
  }

  public static FileBlockHeader from(byte[] b) {
    BitUtils bitUtils = new BitUtils(b);
    int blockSizeType = bitUtils.getNextInteger(LENGTH_BLOCK_SIZE_TYPE);
    int freeByteOffset = bitUtils.getNextInteger(getReprBitSize(blockSizeType));
    if (freeByteOffset == 0) {
      freeByteOffset = getReprByteSize(blockSizeType);
    }
    FileBlockHeader result = new FileBlockHeader(blockSizeType, freeByteOffset);
    return result;
  }

  /**
   * get file block header.
   * <p>
   * rewind file pos after this call.
   * @param fileBlockEntity
   * @return
   */
  public static FileBlockHeader from(Integer blockSizeInByte, FileBlockEntity fileBlockEntity) {
    FileBlockHeader result = null;
    FileLock fileLock = null;
    long beforePosition = -1L;
    try {

      beforePosition = fileBlockEntity.getFileEntity().getHandler().getFilePointer();
      long postion = blockSizeInByte * fileBlockEntity.getBlockIndex();
      fileLock = fileBlockEntity.getFileEntity().lock(postion, blockSizeInByte, false);

      fileBlockEntity.getFileEntity().getHandler().seek(postion);
      result =
          (FileBlockHeader) FileSystemCache.I().get(CacheTargetEnum.FILE_BLOCK, fileBlockEntity);
      if (result == null) {
        result = fromHandler(fileBlockEntity.getFileEntity().getHandler());
        FileSystemCache.I().put(CacheTargetEnum.FILE_BLOCK, fileBlockEntity, result);
      }

    } catch (IOException e) {
      throw FileSystemException.newE(e);
    } finally {
      if (fileLock != null && fileLock.isValid()) {
        try {
          fileLock.release();
        } catch (IOException e) {
          throw FileSystemException.newE(e);
        }
      }
      if (beforePosition != -1) {
        try {
          fileBlockEntity.getFileEntity().getHandler().seek(beforePosition);
        } catch (IOException e) {
          throw FileSystemException.newE(e);
        }
      }
    }

    return result;
  }

  /**
   * extract entity from RAF.
   * @param raf
   * @return
   */
  private static FileBlockHeader fromHandler(RandomAccessFile raf) {
    try {

      byte firstByte = -1;
      try {
        firstByte = raf.readByte();
      } catch (EOFException e) {
        return new FileBlockHeader(DEFUALT_BLOCK_SIZE_TYPE);
      }

      int blockSizeType = getBlockSizeType(firstByte);
      int blockHeaderByteSize = getReprByteSize(blockSizeType);
      byte[] bs = new byte[blockHeaderByteSize - 1];
      raf.read(bs, 0, blockHeaderByteSize - 1);
      FileBlockHeader result = from(Bytes.concat(new byte[] { firstByte }, bs));
      if (result.getFreeByteOffset() == 0) {
        result.setFreeOffset(result.getReprByteSize());
      }

      return result;
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    }
  }

  public static int getBlockSizeType(byte b) {
    BitUtils bitUtils = new BitUtils(new byte[] { b });
    return bitUtils.getNextInteger(LENGTH_BLOCK_SIZE_TYPE);
  }

  public int getBlockSizeType() {
    return blockSizeType;
  }

  public int getFreeByteOffset() {
    return freeByteOffset;
  }

  @Override
  public String toString() {
    return "BlockHeader [headerSize=" + getReprByteSize() + ", freeByteOffset=" + freeByteOffset
        + "]";
  }

  // ---------------------------------------------------------------------------
  // Byteable
  // ---------------------------------------------------------------------------

  @Override
  public int size() {
    return LENGTH_BLOCK_SIZE_TYPE + getReprBitSize(blockSizeType);
  }

  @Override
  public FileBlockHeader fromBytes(byte[] bytes) {
    return from(bytes);
  }

  @Override
  public byte[] toBytes() {
    BitUtils bitUtils = new BitUtils(getReprByteSize(blockSizeType) * Byte.SIZE);
    bitUtils.setCurrentBitIndex(0);
    bitUtils.setNextInteger(blockSizeType, LENGTH_BLOCK_SIZE_TYPE);
    bitUtils.setNextInteger(freeByteOffset, getReprBitSize(blockSizeType));
    return bitUtils.getData();
  }
}
