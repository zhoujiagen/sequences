package com.spike.giantdataanalysis.sequences.core.file.catalog;

import com.spike.giantdataanalysis.sequences.configuration.FileConfiguration;

/**
 * Catalog of page.
 */
public class PAGE_DIR {
  public byte[] not_needed = new byte[FileConfiguration.PAGE_DATA_SIZE];
  public int[] offset_in_page; // tuple offsets in page
}
