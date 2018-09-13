package com.spike.giantdataanalysis.sequences.filesystem.core;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;
import com.spike.giantdataanalysis.sequences.filesystem.exception.FileSystemException;

import fr.devnied.bitlib.BitUtils;

/**
 * File Block Header Abstraction.
 */
public class FileBlockHeader {

  public static final int LENGTH_BLOCK_SIZE_TYPE = 2;

  public static final int DEFUALT_BLOCK_SIZE_TYPE = 0;

  // 0: 4KB, 1: 8KB, 2: 16KB
  private int blockSizeType = 0;
  private int freeByteOffset = 0;

  public FileBlockHeader(int blockSizeType, int freeByteOffset) {
    Preconditions.checkArgument(freeByteOffset >= getReprByteSize(blockSizeType));

    this.blockSizeType = blockSizeType;
    this.freeByteOffset = freeByteOffset;
  }

  public FileBlockHeader(int blockSizeType) {
    this.blockSizeType = blockSizeType;
    this.freeByteOffset = getReprByteSize(blockSizeType);
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

  public static FileBlockHeader fromBytes(byte[] b) {
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
   * extract entity from RAF.
   * <p>
   * NOTE: caller has seeked raf; rewind pos after this call.
   * @param raf
   * @return
   */
  public static FileBlockHeader fromHandler(RandomAccessFile raf) {
    try {

      long startPosition = raf.getFilePointer();

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
      FileBlockHeader result =
          FileBlockHeader.fromBytes(Bytes.concat(new byte[] { firstByte }, bs));
      if (result.getFreeByteOffset() == 0) {
        result.setFreeOffset(result.getReprByteSize());
      }

      raf.seek(startPosition);

      return result;
    } catch (IOException e) {
      throw FileSystemException.newE(e);
    }
  }

  public static int getBlockSizeType(byte b) {
    BitUtils bitUtils = new BitUtils(new byte[] { b });
    return bitUtils.getNextInteger(LENGTH_BLOCK_SIZE_TYPE);
  }

  public byte[] toBytes() {
    BitUtils bitUtils = new BitUtils(getReprByteSize(blockSizeType) * Byte.SIZE);
    bitUtils.setCurrentBitIndex(0);
    bitUtils.setNextInteger(blockSizeType, LENGTH_BLOCK_SIZE_TYPE);
    bitUtils.setNextInteger(freeByteOffset, getReprBitSize(blockSizeType));
    return bitUtils.getData();
  }

  public int getBlockSizeType() {
    return blockSizeType;
  }

  public int getFreeByteOffset() {
    return freeByteOffset;
  }

  @Override
  public String toString() {
    return "FileBlockHeader [headerSize=" + getReprByteSize() + ", freeByteOffset=" + freeByteOffset
        + "]";
  }

}
