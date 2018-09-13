package com.spike.giantdataanalysis.sequences.filesystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;
import com.spike.giantdataanalysis.sequences.commons.bytes.MoreBytes;
import com.spike.giantdataanalysis.sequences.core.file.log.LSN;
import com.spike.giantdataanalysis.sequences.core.file.log.LogRecord;
import com.spike.giantdataanalysis.sequences.filesystem.configuration.FileSystemConfiguration;
import com.spike.giantdataanalysis.sequences.filesystem.core.FileBlockEntity;
import com.spike.giantdataanalysis.sequences.filesystem.core.FileBlockHeader;
import com.spike.giantdataanalysis.sequences.filesystem.core.FileEntity;
import com.spike.giantdataanalysis.sequences.filesystem.exception.FileSystemException;

public class TestIFileSystem {

  // ---------------------------------------------------------------------------
  // set up
  // ---------------------------------------------------------------------------

  private FileSystemConfiguration fileSystemConfiguration = new FileSystemConfiguration();
  private IFileSystem fileSystem;
  private static final String FILE_NAME = "target/test.txt";
  private FileEntity fileEntity;

  private int blockSizeInByte;

  @Before
  public void before() {
    Paths.get(FILE_NAME).toFile().delete();
    Preconditions.checkArgument(!Paths.get(FILE_NAME).toFile().exists());
    blockSizeInByte = fileSystemConfiguration.catalogConfiguration.blockSizeInByte;
    fileSystem = new LocalFileSystem(fileSystemConfiguration);
    fileSystem.create(FILE_NAME, null);
  }

  @After
  public void after() {
    dumpFile();
    fileSystem.close(fileEntity);
    Paths.get(FILE_NAME).toFile().deleteOnExit();
  }

  private void dumpFile() {

    try (FileInputStream fis = new FileInputStream(FILE_NAME);) {
      byte[] b = new byte[blockSizeInByte];
      int readByteCount = 0;
      while ((readByteCount = fis.read(b)) > 0) {
        FileBlockHeader fileBlockHeader = FileBlockHeader.fromHandler(fileEntity.getHandler());
        System.out.println(
          FileBlockHeader.fromBytes(MoreBytes.copy(b, 0, fileBlockHeader.getReprByteSize())));
        System.out.println(MoreBytes.toHex(b, 0, readByteCount));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));) {
    // String line = null;
    //
    // byte[] b = new byte[blockSizeInByte];
    //
    // while ((line = reader.readLine()) != null) {
    // System.out.println(MoreBytes.toHex(line.getBytes()) + ": " + line);
    // }
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
  }

  // ---------------------------------------------------------------------------
  // tests
  // ---------------------------------------------------------------------------

  @Test
  public void testWriteSimple() throws IOException {
    Preconditions.checkArgument(Paths.get(FILE_NAME).toFile().exists());

    // write in block 0
    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.U);
    FileBlockEntity fileBlockEntity = new FileBlockEntity();
    String content = "hello, there!\n";
    fileBlockEntity.setData(content.getBytes());
    fileSystem.write(fileEntity, 0, fileBlockEntity);

    fileEntity.getHandler().seek(0);
    FileBlockHeader fileBlockHeader = FileBlockHeader.fromHandler(fileEntity.getHandler());
    Assert.assertEquals(0, fileBlockHeader.getBlockSizeType());
    Assert.assertEquals(content.length() + fileBlockHeader.getReprByteSize(),
      fileBlockHeader.getFreeByteOffset());

    // write in block 1
    content = "hello, there!!!\n";
    fileBlockEntity.setData(content.getBytes());
    fileSystem.write(fileEntity, 1, fileBlockEntity);

    fileEntity.getHandler().seek(blockSizeInByte);
    fileBlockHeader = FileBlockHeader.fromHandler(fileEntity.getHandler());
    Assert.assertEquals(0, fileBlockHeader.getBlockSizeType());
    Assert.assertEquals(content.length() + fileBlockHeader.getReprByteSize(),
      fileBlockHeader.getFreeByteOffset());
  }

  @Test(expected = FileSystemException.class)
  public void testWriteCrossBlockBoundaryFailed() {
    Preconditions.checkArgument(Paths.get(FILE_NAME).toFile().exists());

    byte[] data = new byte[blockSizeInByte + 2];
    data[0] = 0xF;
    data[data.length - 1] = 0xF;

    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.U);
    FileBlockEntity fileBlockEntity = new FileBlockEntity();
    fileBlockEntity.setData(data);
    fileSystem.write(fileEntity, 0, fileBlockEntity);
  }

  @Test
  public void testWriteCrossBlockBoundarySucceed() throws IOException {
    Preconditions.checkArgument(Paths.get(FILE_NAME).toFile().exists());

    byte[] data = new byte[blockSizeInByte + 2]; // 4098
    data[0] = 0xF;
    data[blockSizeInByte - 1] = 0xF;
    data[blockSizeInByte] = 0xF;
    data[data.length - 1] = 0xF;

    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.U);
    FileBlockEntity fileBlockEntity = new FileBlockEntity();
    fileBlockEntity.setData(data);
    fileSystem.writec(fileEntity, 0, fileBlockEntity);

    fileEntity.getHandler().seek(0);
    FileBlockHeader fileBlockHeader = FileBlockHeader.fromHandler(fileEntity.getHandler());
    Assert.assertEquals(0, fileBlockHeader.getBlockSizeType());
    Assert.assertEquals(blockSizeInByte, fileBlockHeader.getFreeByteOffset()); // 4096 = 2 + 4094

    fileEntity.getHandler().seek(blockSizeInByte);
    fileBlockHeader = FileBlockHeader.fromHandler(fileEntity.getHandler());
    Assert.assertEquals(0, fileBlockHeader.getBlockSizeType());
    Assert.assertEquals(6, fileBlockHeader.getFreeByteOffset()); // 6 = 2 + 4
  }

  @Test
  public void testReadSimple() {
    Preconditions.checkArgument(Paths.get(FILE_NAME).toFile().exists());

    // write
    byte[] data = "hello, there!!!".getBytes();
    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.U);
    FileBlockEntity fileBlockEntity = new FileBlockEntity();
    fileBlockEntity.setData(data);
    fileSystem.writec(fileEntity, 0, fileBlockEntity);
    fileSystem.close(fileEntity);

    // read
    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.R);
    fileBlockEntity = fileSystem.read(fileEntity, 0);
    Assert.assertArrayEquals(data, fileBlockEntity.getData());
  }

  @Test
  public void testAppendSimple() {
    Preconditions.checkArgument(Paths.get(FILE_NAME).toFile().exists());

    // append
    byte[] data = "hello, there!!!".getBytes();
    byte[] writedData = new byte[0];
    int times = 3;
    FileBlockEntity fileBlockEntity = new FileBlockEntity();
    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.A);
    for (int i = 0; i < times; i++) {
      fileBlockEntity.setData(data);
      fileSystem.write(fileEntity, 0, fileBlockEntity);
      writedData = Bytes.concat(writedData, data);
    }
    fileSystem.close(fileEntity);

    // read
    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.R);
    fileBlockEntity = fileSystem.read(fileEntity, 0);
    Assert.assertArrayEquals(writedData, fileBlockEntity.getData());
  }

  @Test
  public void testAppendCrossBlockBoundary() throws IOException {
    Preconditions.checkArgument(Paths.get(FILE_NAME).toFile().exists());

    // write
    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.U);
    FileBlockEntity fileBlockEntity = new FileBlockEntity();
    byte[] wdata = new byte[] { 0xf, 0xf, 0xf, 0xf, 0xf, 0xf, 0xf, 0xf }; // 8
    fileBlockEntity.setData(wdata);
    fileSystem.write(fileEntity, 0, fileBlockEntity);
    fileSystem.close(fileEntity);

    // append
    byte[] data = new byte[blockSizeInByte + 2]; // 4098
    data[0] = 0xF;
    data[blockSizeInByte - 1] = 0xF;
    data[blockSizeInByte] = 0xF;
    data[data.length - 1] = 0xF;

    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.A);
    fileBlockEntity.setData(data);
    fileSystem.writec(fileEntity, 0, fileBlockEntity);

    // read block header
    fileEntity.getHandler().seek(0);
    FileBlockHeader fileBlockHeader = FileBlockHeader.fromHandler(fileEntity.getHandler());
    Assert.assertEquals(0, fileBlockHeader.getBlockSizeType());
    Assert.assertEquals(blockSizeInByte, fileBlockHeader.getFreeByteOffset()); // 4096 = 2 + 4094

    fileEntity.getHandler().seek(blockSizeInByte);
    fileBlockHeader = FileBlockHeader.fromHandler(fileEntity.getHandler());
    Assert.assertEquals(0, fileBlockHeader.getBlockSizeType());
    Assert.assertEquals(14, fileBlockHeader.getFreeByteOffset()); // 14 = 4098+8-4094 +2
  }

  static void write_log() throws Exception {
    FileSystemConfiguration configuration = new FileSystemConfiguration();
    IFileSystem fs = new LocalFileSystem(configuration);

    String base = "target/";
    String filename = base + "hello.txt";
    fs.create(filename, null);

    FileEntity fileEntity = fs.open(filename, FileAccessModeEnum.U);

    System.out.println("=== write");
    String content = "hello, there!";
    LogRecord logRecord = new LogRecord();
    logRecord.lsn = LSN.NULL;
    logRecord.prev_lsn = LSN.NULL;
    logRecord.timestamp = new Date().getTime();
    logRecord.rmid = 1;
    logRecord.txnid = 1;
    logRecord.txn_prev_lsn = LSN.NULL;
    logRecord.length = content.length();
    logRecord.body = content.getBytes();

    FileBlockEntity fileBlockEntity = new FileBlockEntity();
    fileBlockEntity
        .setData(Bytes.concat(logRecord.asString().getBytes(), System.lineSeparator().getBytes()));
    fs.write(fileEntity, -2, fileBlockEntity);

    System.out.println("=== read again");
    fs.close(fileEntity);
    fileEntity = fs.open(filename, FileAccessModeEnum.R);
    fileBlockEntity = fs.read(fileEntity, 0);
    System.out.println(new String(fileBlockEntity.getData()));
  }

}
