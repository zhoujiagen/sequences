package com.spike.giantdataanalysis.db.filesystem.configuration;

public final class FileSystemConfiguration {
  // data
  private FileSystemDataFileConfiguration dataFileConfiguration =
      new FileSystemDataFileConfiguration();

  // catalog
  private FileSystemCatalogConfiguration catalogConfiguration =
      new FileSystemCatalogConfiguration();

  public FileSystemCatalogConfiguration getCatalogConfiguration() {
    return catalogConfiguration;
  }

  public FileSystemDataFileConfiguration getDataFileConfiguration() {
    return dataFileConfiguration;
  }

  public void setDataFileConfiguration(FileSystemDataFileConfiguration dataFileConfiguration) {
    this.dataFileConfiguration = dataFileConfiguration;
  }

  public void setCatalogConfiguration(FileSystemCatalogConfiguration catalogConfiguration) {
    this.catalogConfiguration = catalogConfiguration;
  }

}
