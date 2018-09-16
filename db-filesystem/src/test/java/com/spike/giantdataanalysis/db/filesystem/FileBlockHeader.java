package com.spike.giantdataanalysis.db.filesystem;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

import com.google.common.primitives.Bytes;
import com.spike.giantdataanalysis.db.filesystem.core.AbstractFileBlockHeader;
import com.spike.giantdataanalysis.db.filesystem.core.FileBlockEntity;
import com.spike.giantdataanalysis.db.filesystem.core.cache.CacheTargetEnum;
import com.spike.giantdataanalysis.db.filesystem.core.cache.FileSystemCache;
import com.spike.giantdataanalysis.db.filesystem.exception.FileSystemException;

import fr.devnied.bitlib.BitUtils;

public class FileBlockHeader extends AbstractFileBlockHeader {
  private static final long serialVersionUID = 1L;

  private int freeByteOffset = 0;

  public FileBlockHeader(Integer blockSizeInByte, FileBlockEntity fileBlockEntity) {
    super(blockSizeInByte, fileBlockEntity);

    FileBlockHeader result = null;
    FileLock fileLock = null;
    long beforePosition = -1L;
    try {

      beforePosition = fileBlockEntity.getFileEntity().getHandler().getFilePointer();
      long postion = blockSizeInByte * fileBlockEntity.getBlockIndex();
      fileLock = fileBlockEntity.getFileEntity().lock(postion, blockSizeInByte, false);

      fileBlockEntity.getFileEntity().getHandler().seek(postion);
      // get from cache
      result = (FileBlockHeader) FileSystemCache.I().get(//
        CacheTargetEnum.FILE_BLOCK, fileBlockEntity);
      if (result == null) {
        // get from reading file
        result = fromHandler(fileBlockEntity.getFileEntity().getHandler());
        if (result == null) {
          // construct by configuration
          this.blockSizeType = getBlockSizeType(blockSizeInByte);
          this.freeByteOffset = getReprByteSize();
        } else {
          this.blockSizeType = result.blockSizeType;
          this.freeByteOffset = result.freeByteOffset;
        }
        FileSystemCache.I().put(CacheTargetEnum.FILE_BLOCK, fileBlockEntity, this);
      } else {
        this.blockSizeType = result.blockSizeType;
        this.freeByteOffset = result.freeByteOffset;
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
  }

  /**
   * extract entity from RAF.
   * @param raf
   * @return may be null when meet blank block
   */
  private FileBlockHeader fromHandler(RandomAccessFile raf) {
    try {

      byte firstByte = -1;
      try {
        firstByte = raf.readByte();
      } catch (EOFException e) {
        return null;
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

  private FileBlockHeader from(byte[] b) {
    BitUtils bitUtils = new BitUtils(b);
    blockSizeType = bitUtils.getNextInteger(LENGTH_BLOCK_SIZE_TYPE);
    freeByteOffset = bitUtils.getNextInteger(getReprBitSize(blockSizeType));
    if (freeByteOffset == 0) {
      freeByteOffset = getReprByteSize(blockSizeType);
    }
    return this;
  }

  public void increaseFreeOffset(int delta) {
    freeByteOffset += delta;
  }

  public void setFreeOffset(int freeByteOffset) {
    this.freeByteOffset = freeByteOffset;
  }

  /**
   * @param blockSizeType
   * @return block header repr bit size
   */
  @Override
  public int getReprBitSize(int blockSizeType) {
    int byteSize = getBlockSizeInByte(blockSizeType);
    return Integer.numberOfTrailingZeros(byteSize) + 1;
  }

  /**
   * @param blockSizeType
   * @return block header repr byte size
   */
  @Override
  public int getReprByteSize(int blockSizeType) {
    int bitSize = getReprBitSize(blockSizeType) + LENGTH_BLOCK_SIZE_TYPE;
    int mod = bitSize % Byte.SIZE;
    if (mod == 0) {
      return bitSize / Byte.SIZE;
    } else {
      return bitSize / Byte.SIZE + 1;
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
