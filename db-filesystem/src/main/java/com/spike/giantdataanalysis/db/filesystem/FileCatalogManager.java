package com.spike.giantdataanalysis.db.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.db.filesystem.configuration.FileSystemCatalogConfiguration;
import com.spike.giantdataanalysis.db.filesystem.configuration.FileSystemConfiguration;
import com.spike.giantdataanalysis.db.filesystem.core.FileBlockId;
import com.spike.giantdataanalysis.db.filesystem.core.FileEntity;
import com.spike.giantdataanalysis.db.filesystem.core.FileRecordId;
import com.spike.giantdataanalysis.db.filesystem.exception.FileCatalogException;

/**
 * File Catalog Manager.
 */
public class FileCatalogManager {
  private static final Logger LOG = LoggerFactory.getLogger(FileCatalogManager.class);

  private final FileSystemConfiguration configuration;

  private static final Map<String, Integer> FILE_ID_CACHE = Maps.newConcurrentMap();
  private static RandomAccessFile FILE_ID_RAF;
  private static final AtomicInteger FILENO_SEQUENCE = new AtomicInteger(0);

  /** file name => file entity. */
  private static final Map<String, FileEntity> FILE_CACHE = Maps.newConcurrentMap();

  public FileCatalogManager(FileSystemConfiguration configuration) {
    this.configuration = configuration;

    FileSystemCatalogConfiguration conf = configuration.getCatalogConfiguration();
    Preconditions.checkState(Paths.get(conf.getCatalogRootDir()).toFile().isDirectory());

    try {
      FILE_ID_RAF =
          new RandomAccessFile(Paths.get(conf.getCatalogRootDir(), conf.getFileIdFileName())
              .toFile(), "rwd");
    } catch (FileNotFoundException e) {
      throw FileCatalogException.newE(e);
    }
  }

  // ---------------------------------------------------------------------------
  // file id cache
  // ---------------------------------------------------------------------------

  public synchronized void initialize() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("file catalog manager initialize start.");
    }

    try {
      String line = null;
      int maxFileId = -1;
      while ((line = FILE_ID_RAF.readLine()) != null) {
        List<String> list = Splitter.on("=").splitToList(line);
        int fieldId = Integer.parseInt(list.get(1));
        FILE_ID_CACHE.put(list.get(0), fieldId);
        if (fieldId > maxFileId) {
          maxFileId = fieldId;
        }
      }
      FILENO_SEQUENCE.set(maxFileId + 1);

    } catch (IOException e) {
      throw FileCatalogException.newE(e);
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("file catalog manager initialize finished.");
    }
  }

  public int getOrCreateFileId(String filename) {
    Integer result = FILE_ID_CACHE.get(filename);

    if (result != null) {
      return result;
    } else {
      result = FILENO_SEQUENCE.getAndIncrement();
      FILE_ID_CACHE.put(filename, result);
      persistent();
    }
    return result;
  }

  public FileBlockId getOrCreateBlockId(String filename, int blockIndex) {
    int fileId = this.getOrCreateFileId(filename);
    return new FileBlockId(fileId, blockIndex);
  }

  public FileRecordId getOrCreateRecordId(String filename, int blockIndex, int recordIndex) {
    int fileId = this.getOrCreateFileId(filename);
    return new FileRecordId(new FileBlockId(fileId, blockIndex), recordIndex);
  }

  private synchronized void persistent() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("persistent file catalog.");
    }
    try {
      FILE_ID_RAF.seek(0);
      FILE_ID_RAF.setLength(0);

      for (String fileName : FILE_ID_CACHE.keySet()) {
        FILE_ID_RAF.writeChars(//
            fileName + "=" + FILE_ID_CACHE.get(fileName) + System.lineSeparator());
      }
      FILE_ID_RAF.getFD().sync();
    } catch (IOException e) {
      throw FileCatalogException.newE(e);
    }
  }

  // ---------------------------------------------------------------------------
  // file name cache
  // ---------------------------------------------------------------------------

  public FileSystemConfiguration getConfiguration() {
    return configuration;
  }

  public void cacheFile(String filename, FileEntity fileEntity) {
    FILE_CACHE.put(filename, fileEntity);
  }

  public FileEntity queryFileCache(String filename) {
    return FILE_CACHE.get(filename);
  }

  public void removeFileCache(String filename) {
    FILE_CACHE.remove(filename);
  }
}
