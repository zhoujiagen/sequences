package com.spike.giantdataanalysis.db.filesystem.configuration;

public class FileSystemDataFileConfiguration {
  private String dataRootDir = "target/data/";

  public String getDataRootDir() {
    return dataRootDir;
  }

  public void setDataRootDir(String dataRootDir) {
    this.dataRootDir = dataRootDir;
  }

}
