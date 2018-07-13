package com.spike.giantdataanalysis.sequences.rm.file.core.catalog;

import com.spike.giantdataanalysis.sequences.rm.file.config.FileConfiguration;

/**
 * Catalog of page.
 */
public class PAGE_DIR {
  public byte[] not_needed = new byte[FileConfiguration.PAGE_DATA_SIZE];
  public int[] offset_in_page; // tuple offsets in page
}
