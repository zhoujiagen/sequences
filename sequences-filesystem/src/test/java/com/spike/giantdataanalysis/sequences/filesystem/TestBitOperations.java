package com.spike.giantdataanalysis.sequences.filesystem;

import org.junit.Assert;
import org.junit.Test;

import com.spike.giantdataanalysis.sequences.commons.bytes.MoreBytes;
import com.spike.giantdataanalysis.sequences.filesystem.core.FileBlockHeader;

public class TestBitOperations {
  @Test
  public void testFileBlockHeader_fromBytes() {
    byte[] b = new byte[] { (byte) 255, (byte) 255 };
    System.out.println(MoreBytes.toHex(b)); // ffff
    FileBlockHeader fileBlockHeader = FileBlockHeader.from(b);
    System.out.println(fileBlockHeader);
    Assert.assertEquals(3, fileBlockHeader.getBlockSizeType());
    Assert.assertEquals(8191, fileBlockHeader.getFreeByteOffset());
  }

  @Test
  public void testReprBitSize() {
    FileBlockHeader fileBlockHeader = new FileBlockHeader(0);
    Assert.assertEquals(2, fileBlockHeader.getReprByteSize());

    fileBlockHeader = new FileBlockHeader(1);
    Assert.assertEquals(2, fileBlockHeader.getReprByteSize());

    fileBlockHeader = new FileBlockHeader(2);
    Assert.assertEquals(3, fileBlockHeader.getReprByteSize());
  }
}
