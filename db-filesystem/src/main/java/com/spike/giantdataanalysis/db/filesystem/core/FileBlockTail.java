package com.spike.giantdataanalysis.db.filesystem.core;

import java.util.List;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.db.commons.serialize.Byteable;

import fr.devnied.bitlib.BitUtils;

public class FileBlockTail implements Byteable<FileBlockTail> {
  private static final long serialVersionUID = 1L;

  private List<FileBlockTailRecordPtr> recordPtrs = Lists.newArrayList();
  private byte[] crc = new byte[4];

  public void addRecordPtr(FileBlockTailRecordPtr recordPtr) {
    recordPtrs.add(recordPtr);
  }

  public List<FileBlockTailRecordPtr> getRecordPtrs() {
    return recordPtrs;
  }

  public void setRecordPtrs(List<FileBlockTailRecordPtr> recordPtrs) {
    this.recordPtrs = recordPtrs;
  }

  public byte[] getCrc() {
    return crc;
  }

  public void setCrc(byte[] crc) {
    this.crc = crc;
  }

  // ---------------------------------------------------------------------------
  // Byteable
  // ---------------------------------------------------------------------------

  public byte[] toBytes() {
    int ptrSize = recordPtrs.size();
    if (ptrSize == 0) return new byte[0];

    BitUtils bitUtils = new BitUtils(Integer.SIZE * ptrSize);
    for (FileBlockTailRecordPtr ptr : recordPtrs) {
      bitUtils.setNextInteger(ptr.getFileRecordId().getRecordIndex(), Integer.SIZE);
      bitUtils.setNextInteger(ptr.getRecordOffsetInBlock(), Integer.SIZE);
    }
    return bitUtils.getData();
  }

  @Override
  public int size() {
    return Integer.SIZE + Integer.SIZE;
  }

  @Override
  public FileBlockTail fromBytes(byte[] bytes) {
    if (bytes.length == 0) {
      return null;
    }
    BitUtils bitUtils = new BitUtils(bytes);
    int bitSize = bitUtils.getSize();
    FileBlockTail result = new FileBlockTail();
    while (bitSize > 0) {
      FileRecordId fileRecordId =
          new FileRecordId(FileBlockId.DEFAULT, bitUtils.getNextInteger(Integer.SIZE));
      FileBlockTailRecordPtr ptr =
          new FileBlockTailRecordPtr(fileRecordId, bitUtils.getNextInteger(Integer.SIZE));
      result.addRecordPtr(ptr);
      bitSize -= Integer.SIZE + Integer.SIZE;
    }

    return result;
  }

}
