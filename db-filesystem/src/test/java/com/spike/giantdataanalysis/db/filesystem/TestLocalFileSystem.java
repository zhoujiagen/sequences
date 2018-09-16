package com.spike.giantdataanalysis.db.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;
import com.spike.giantdataanalysis.db.commons.data.MoreBytes;
import com.spike.giantdataanalysis.db.filesystem.configuration.FileSystemConfiguration;
import com.spike.giantdataanalysis.db.filesystem.core.FileAccessModeEnum;
import com.spike.giantdataanalysis.db.filesystem.core.FileBlockEntity;
import com.spike.giantdataanalysis.db.filesystem.core.FileEntity;
import com.spike.giantdataanalysis.db.filesystem.core.cache.FileSystemCache;
import com.spike.giantdataanalysis.db.filesystem.exception.FileSystemException;

public class TestLocalFileSystem {

  // ---------------------------------------------------------------------------
  // set up
  // ---------------------------------------------------------------------------

  private FileSystemConfiguration fileSystemConfiguration = new FileSystemConfiguration();
  private IFileSystem fileSystem;
  private static final String FILE_NAME = "target/test.txt";
  private static final String CATALOG_DIR_NAME = "target/catalog/";
  private FileEntity fileEntity;

  private int blockSizeInByte;

  @Before
  public void before() {
    Paths.get(FILE_NAME).toFile().delete();
    File catalogDir = Paths.get(CATALOG_DIR_NAME).toFile();
    if (!catalogDir.exists()) {
      catalogDir.mkdir();
    }
    for (File catalogFile : catalogDir.listFiles()) {
      catalogFile.delete();
    }
    Preconditions.checkArgument(!Paths.get(FILE_NAME).toFile().exists());
    blockSizeInByte = fileSystemConfiguration.getCatalogConfiguration().getBlockSizeInByte();
    FileCatalogManager fileCatalogManager = new FileCatalogManager(fileSystemConfiguration);
    fileSystem = new LocalFileSystem(fileCatalogManager);
    fileSystem.create(FILE_NAME, null);
  }

  @After
  public void after() {
    dumpFile();
    fileSystem.close(fileEntity);
    Paths.get(FILE_NAME).toFile().deleteOnExit();

    FileSystemCache.I().clear();
  }

  private void dumpFile() {

    try (FileInputStream fis = new FileInputStream(FILE_NAME);) {
      byte[] b = new byte[blockSizeInByte];
      int readByteCount = 0;
      int blockIndex = 0;
      while ((readByteCount = fis.read(b)) > 0) {
        FileBlockHeader fileBlockHeader =
            new FileBlockHeader(blockSizeInByte, new FileBlockEntity(fileEntity, blockIndex++));
        System.out.println(fileBlockHeader.fromBytes(MoreBytes.copy(b, 0,
          fileBlockHeader.getReprByteSize())));
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
  public void testOpen() {
    Preconditions.checkArgument(Paths.get(FILE_NAME).toFile().exists());

    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.A);
    int fileNumber = fileEntity.getNumber();
    fileSystem.close(fileEntity);

    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.R);
    Assert.assertEquals(fileNumber, fileEntity.getNumber());
  }

  @Test
  public void testWriteSimple() throws IOException {
    Preconditions.checkArgument(Paths.get(FILE_NAME).toFile().exists());

    // write in block 0
    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.U);
    FileBlockEntity fileBlockEntity = new FileBlockEntity(fileEntity, 0);
    String content = "hello, there!\n";
    fileBlockEntity.setData(content.getBytes());
    fileSystem.write(fileEntity, 0, content.getBytes());

    FileBlockHeader fileBlockHeader = new FileBlockHeader(blockSizeInByte, fileBlockEntity);
    Assert.assertEquals(0, fileBlockHeader.getBlockSizeType());
    Assert.assertEquals(content.length() + fileBlockHeader.getReprByteSize(),
      fileBlockHeader.getFreeByteOffset());

    // write in block 1
    content = "hello, there!!!\n";
    fileBlockEntity.setData(content.getBytes());
    fileBlockEntity = new FileBlockEntity(fileEntity, 1);
    fileSystem.write(fileEntity, 1, content.getBytes());

    fileBlockHeader = new FileBlockHeader(blockSizeInByte, fileBlockEntity);
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
    FileBlockEntity fileBlockEntity = new FileBlockEntity(fileEntity, 0);
    fileBlockEntity.setData(data);
    fileSystem.write(fileEntity, 0, data);
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
    FileBlockEntity fileBlockEntity = new FileBlockEntity(fileEntity, 0);
    fileBlockEntity.setData(data);
    fileSystem.writec(fileEntity, 0, data);

    FileBlockHeader fileBlockHeader = new FileBlockHeader(blockSizeInByte, fileBlockEntity);
    Assert.assertEquals(0, fileBlockHeader.getBlockSizeType());
    Assert.assertEquals(blockSizeInByte, fileBlockHeader.getFreeByteOffset()); // 4096 = 2 + 4094

    fileBlockEntity = new FileBlockEntity(fileEntity, 1);
    fileBlockHeader = new FileBlockHeader(blockSizeInByte, fileBlockEntity);
    Assert.assertEquals(0, fileBlockHeader.getBlockSizeType());
    Assert.assertEquals(6, fileBlockHeader.getFreeByteOffset()); // 6 = 2 + 4
  }

  @Test
  public void testReadSimple() {
    Preconditions.checkArgument(Paths.get(FILE_NAME).toFile().exists());

    // write
    byte[] data = "hello, there!!!".getBytes();
    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.U);
    FileBlockEntity fileBlockEntity = new FileBlockEntity(fileEntity, 0);
    fileSystem.write(fileEntity, 0, data);
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
    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.A);
    FileBlockEntity fileBlockEntity = new FileBlockEntity(fileEntity, 0);
    for (int i = 0; i < times; i++) {
      fileSystem.writec(fileEntity, 0, data);
      writedData = Bytes.concat(writedData, data);
    }
    fileSystem.close(fileEntity);

    // read
    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.R);
    fileBlockEntity = fileSystem.read(fileEntity, 0);
    Assert.assertArrayEquals(writedData, fileBlockEntity.getData());
  }

  @Ignore
  @Test
  public void testAppendCrossBlockBoundary() throws IOException {
    Preconditions.checkArgument(Paths.get(FILE_NAME).toFile().exists());

    // write
    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.U);
    FileBlockEntity fileBlockEntity = new FileBlockEntity(fileEntity, 0);
    byte[] wdata = new byte[] { 0xf, 0xf, 0xf, 0xf, 0xf, 0xf, 0xf, 0xf }; // 8
    fileBlockEntity.setData(wdata);
    fileSystem.write(fileEntity, 0, wdata);
    fileSystem.close(fileEntity);

    // append
    byte[] data = new byte[blockSizeInByte + 2]; // 4098
    data[0] = 0xF;
    data[blockSizeInByte - 1] = 0xF;
    data[blockSizeInByte] = 0xF;
    data[data.length - 1] = 0xF;

    fileEntity = fileSystem.open(FILE_NAME, FileAccessModeEnum.A);
    fileBlockEntity.setData(data);
    fileSystem.writec(fileEntity, 0, data);

    // read block header
    fileBlockEntity = new FileBlockEntity(fileEntity, 0);
    FileBlockHeader fileBlockHeader = new FileBlockHeader(blockSizeInByte, fileBlockEntity);
    Assert.assertEquals(0, fileBlockHeader.getBlockSizeType());
    Assert.assertEquals(blockSizeInByte, fileBlockHeader.getFreeByteOffset()); // 4096 = 2 + 4094

    fileBlockEntity = new FileBlockEntity(fileEntity, 1);
    fileBlockHeader = new FileBlockHeader(blockSizeInByte, fileBlockEntity);
    Assert.assertEquals(0, fileBlockHeader.getBlockSizeType());
    Assert.assertEquals(14, fileBlockHeader.getFreeByteOffset()); // 14 = 4098+8-4094 +2
  }

}
