package com.spike.giantdataanalysis.db;

import com.spike.giantdataanalysis.db.filesystem.FileCatalogManager;
import com.spike.giantdataanalysis.db.filesystem.configuration.FileSystemConfiguration;
import com.spike.giantdataanalysis.db.filesystem.core.cache.FileSystemCache;

/**
 * File System Driver: kick off file system utilities;
 */
public class FileSystemDriver {

  private FileSystemConfiguration configuration;
  private FileCatalogManager fileCatalogManager;

  public FileSystemDriver(FileSystemConfiguration configuration) {
    this.configuration = configuration;
  }

  protected void start() {
    FileSystemCache.I().clear();
    fileCatalogManager = new FileCatalogManager(configuration);
    fileCatalogManager.initialize();
  }

}
