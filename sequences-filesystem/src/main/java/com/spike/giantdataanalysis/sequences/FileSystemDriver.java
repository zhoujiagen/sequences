package com.spike.giantdataanalysis.sequences;

import com.spike.giantdataanalysis.sequences.filesystem.FileCatalogManager;
import com.spike.giantdataanalysis.sequences.filesystem.configuration.FileSystemConfiguration;
import com.spike.giantdataanalysis.sequences.filesystem.core.cache.FileSystemCache;

/**
 * File System Driver: kick off file system utilities;
 */
public class FileSystemDriver {

  private FileSystemConfiguration configuration;
  private FileCatalogManager fileCatalogManager;

  public FileSystemDriver(FileSystemConfiguration configuration) {
    this.configuration = configuration;
  }

  public void start() {
    FileSystemCache.I().clear();
    fileCatalogManager = new FileCatalogManager(configuration);
    fileCatalogManager.initialize();
  }

}
