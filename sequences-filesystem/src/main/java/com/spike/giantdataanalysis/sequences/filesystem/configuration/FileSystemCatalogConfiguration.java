package com.spike.giantdataanalysis.sequences.filesystem.configuration;

public class FileSystemCatalogConfiguration {
  public String catalogRootDir = "target/catalog/";
  public int blockSizeInByte = 4 * 1024; // 4KB

  public String storeFilePreix = "STORE-";
  public String fileDescriptorFilePrefix = "FD-";
  public int nameLength = 10;
}