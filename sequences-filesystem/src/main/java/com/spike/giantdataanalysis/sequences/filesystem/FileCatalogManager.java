package com.spike.giantdataanalysis.sequences.filesystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.sequences.core.file.catalog.STORE;
import com.spike.giantdataanalysis.sequences.core.file.catalog.basic_file_descriptor;
import com.spike.giantdataanalysis.sequences.filesystem.configuration.FileSystemCatalogConfiguration;
import com.spike.giantdataanalysis.sequences.filesystem.exception.FileCatalogException;

/**
 * File Catalog Manager.
 * <p>
 * Operate on <code>STORE</code>, <code>basic_file_descriptor</code>.
 */
@Deprecated
public class FileCatalogManager {

  private static final Logger LOG = LoggerFactory.getLogger(FileCatalogManager.class);

  private FileSystemCatalogConfiguration configuration;

  // STORE/DISK, basic_file_descriptor
  private STORE storeCache;
  private LinkedList<basic_file_descriptor> fileDescriptorCache = Lists.newLinkedList();

  // FIXME(zhoujiagen) reader/writer not work here, use RandomAccessFile instead
  private RandomAccessFile storeCatelogRAF;
  private RandomAccessFile fdCatalogRAF;

  private enum Catalog {
    STORE, FD
  }

  public FileCatalogManager(FileSystemCatalogConfiguration configuration) {
    this.configuration = configuration;

    this.initialize();
  }

  public synchronized boolean initialize() {
    LOG.info("file catalog manager initialize start.");

    // reload the file descriptors from store after reboot
    File storeCatalogFile = this.getOrCreateFile(Catalog.STORE);
    File fdCatalogFile = this.getOrCreateFile(Catalog.FD);

    try {
      storeCatelogRAF = new RandomAccessFile(storeCatalogFile, "rwd");
      fdCatalogRAF = new RandomAccessFile(fdCatalogFile, "rwd");
    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
      throw FileCatalogException.newE(e);
    }

    LOG.info("file catalog manager initialize finished.");
    return true;
  }

  private File getOrCreateFile(Catalog catalog) {
    File result = null;

    File catalogDirFile = new File(configuration.catalogRootDir);
    if (!catalogDirFile.isDirectory() || !catalogDirFile.exists()) {
      throw FileCatalogException.newE("invalid catalogDir");
    }

    String prefix = null;
    switch (catalog) {
    case STORE: {
      prefix = configuration.storeFilePreix;
      break;
    }
    case FD: {
      prefix = configuration.fileDescriptorFilePrefix;
      break;
    }
    default:
      throw FileCatalogException.newE(new UnsupportedOperationException());
    }

    final String catalogFilePrefix = prefix;
    try {
      File[] catalogFiles = catalogDirFile.listFiles(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return name.startsWith(catalogFilePrefix);
        }
      });
      if (catalogFiles == null || catalogFiles.length == 0) {
        String filename = catalogFilePrefix + Strings.padStart("", configuration.nameLength, '0');
        result = new File(catalogDirFile, filename);
        if (!result.createNewFile()) {
          throw FileCatalogException.newE("create " + filename + " failed");
        }
      } else {
        // attach to the last one
        result = catalogFiles[catalogFiles.length - 1];
      }

      // init cache
      for (File file : catalogFiles) {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = reader.readLine()) != null) {
          if (Catalog.STORE.equals(catalog)) {
            storeCache = STORE.NULL.fromString(line); // only one line
            break;
          } else if (Catalog.FD.equals(catalog)) {
            fileDescriptorCache.add(basic_file_descriptor.NULL.fromString(line));
          }
        }
        reader.close();
      }

    } catch (Exception e) {
      throw FileCatalogException.newE(e);
    }

    return result;
  }

  public boolean addFD(basic_file_descriptor fileDescriptor) {

    throw new UnsupportedOperationException();
  }

  public basic_file_descriptor getFD(String filename) {
    for (basic_file_descriptor fd : fileDescriptorCache) {
      if (fd.filename.equals(filename)) {
        return fd;
      }
    }
    return null;
  }

  public boolean deleteFD(basic_file_descriptor fileDescriptor) {
    throw new UnsupportedOperationException();
  }

  public boolean updateFD(basic_file_descriptor fileDescriptor) {
    throw new UnsupportedOperationException();
  }

}
