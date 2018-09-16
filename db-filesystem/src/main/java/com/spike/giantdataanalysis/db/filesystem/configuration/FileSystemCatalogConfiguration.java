package com.spike.giantdataanalysis.db.filesystem.configuration;

import com.spike.giantdataanalysis.db.commons.Constant;

public class FileSystemCatalogConfiguration {
  private String catalogRootDir = "target/catalog/";
  private String fileIdFileName = "fileid.id";

  /** file block size in byte */
  private int blockSizeInByte = 4 * Constant.K; // 4KB
  /** chunk read size in byte */
  private int chunkReadSizeInByte = 4 * Constant.M; // 1024 blocks

  private String storeFilePreix = "STORE-";
  private String fileDescriptorFilePrefix = "FD-";
  private int nameLength = 10;

  public String getFileIdFileName() {
    return fileIdFileName;
  }

  public void setFileIdFileName(String fileIdFileName) {
    this.fileIdFileName = fileIdFileName;
  }

  public long getChunkReadSizeInByte() {
    return chunkReadSizeInByte;
  }

  public void setChunkReadSizeInByte(int chunkReadSizeInByte) {
    this.chunkReadSizeInByte = chunkReadSizeInByte;
  }

  public void setBlockSizeInByte(int blockSizeInByte) {
    this.blockSizeInByte = blockSizeInByte;
  }

  public String getCatalogRootDir() {
    return catalogRootDir;
  }

  public void setCatalogRootDir(String catalogRootDir) {
    this.catalogRootDir = catalogRootDir;
  }

  public int getBlockSizeInByte() {
    return blockSizeInByte;
  }

  public String getStoreFilePreix() {
    return storeFilePreix;
  }

  public void setStoreFilePreix(String storeFilePreix) {
    this.storeFilePreix = storeFilePreix;
  }

  public String getFileDescriptorFilePrefix() {
    return fileDescriptorFilePrefix;
  }

  public void setFileDescriptorFilePrefix(String fileDescriptorFilePrefix) {
    this.fileDescriptorFilePrefix = fileDescriptorFilePrefix;
  }

  public int getNameLength() {
    return nameLength;
  }

  public void setNameLength(int nameLength) {
    this.nameLength = nameLength;
  }

}