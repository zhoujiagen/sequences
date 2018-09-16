package com.spike.giantdataanalysis.db.filesystem.core;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.spike.giantdataanalysis.db.commons.Constant;
import com.spike.giantdataanalysis.db.commons.cache.CacheEntity;
import com.spike.giantdataanalysis.db.commons.serialize.Byteable;

import fr.devnied.bitlib.BitUtils;

/**
 * File Block Header Abstraction.
 */
public abstract class AbstractFileBlockHeader implements CacheEntity,
    Byteable<AbstractFileBlockHeader> {
  private static final long serialVersionUID = 1L;

  public static final int LENGTH_BLOCK_SIZE_TYPE = 2;
  public static final int DEFUALT_BLOCK_SIZE_TYPE = 0;

  protected int blockSizeType = 0; // 0: 4KB, 1: 8KB, 2: 16KB
  protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

  // ---------------------------------------------------------------------------
  // Constructor
  // ---------------------------------------------------------------------------

  /**
   * get file block header.
   * <p>
   * rewind file pos after this call.
   * @param fileBlockEntity
   * @return
   */
  protected AbstractFileBlockHeader(Integer blockSizeInByte, FileBlockEntity fileBlockEntity) {
  }

  // ---------------------------------------------------------------------------
  // Helper
  // ---------------------------------------------------------------------------

  public ReentrantReadWriteLock lock() {
    return lock;
  }

  protected int getBlockSizeInByte(int blockSizeType) {
    switch (blockSizeType) {
    case 0:
      return Constant.K_4;
    case 1:
      return Constant.K_8;
    case 2:
      return Constant.K_16;
    default:
      return Constant.K_4;
    }
  }

  protected int getBlockSizeType(int blockSizeInByte) {
    switch (blockSizeInByte) {
    case Constant.K_4:
      return 0;
    case Constant.K_8:
      return 1;
    case Constant.K_16:
      return 2;
    default:
      return 0;
    }
  }

  /**
   * @param blockSizeType
   * @return block header repr bit size
   */
  public abstract int getReprBitSize(int blockSizeType);

  public int getReprByteSize() {
    return getReprByteSize(blockSizeType);
  }

  /**
   * @param blockSizeType
   * @return block header repr byte size
   */
  public abstract int getReprByteSize(int blockSizeType);

  public static int getBlockSizeType(byte b) {
    BitUtils bitUtils = new BitUtils(new byte[] { b });
    return bitUtils.getNextInteger(LENGTH_BLOCK_SIZE_TYPE);
  }

  public int getBlockSizeType() {
    return blockSizeType;
  }

  @Override
  public String toString() {
    return "BlockHeader [headerSize=" + getReprByteSize() + "]";
  }

}
