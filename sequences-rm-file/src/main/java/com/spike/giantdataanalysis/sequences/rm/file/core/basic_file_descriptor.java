package com.spike.giantdataanalysis.sequences.rm.file.core;

/**
 * catalog of a file, including address mapping, free space hints; should be stored persistently.
 */
public class basic_file_descriptor {
  public String filename;
  public int fileno; // FILEID
  public int partition_no; // for partitioned file
  public int version_no; // version
  public allocparmp spacerequest; // describe primary and secondary
  public int curr_no_extends; // number of currently allocated extents

  /** Extent: a set of contiguous block. */
  public static class extent {
    public String diskid; // in which disk
    public int ext_no; // extent index in the disk
    public int accum_length; // currently fulfilled block size
  }

  public extent[] curr_alloc; // allocated extents

  public static class extent_entry {
    public int first_slot; // first slot index in this extent entry
    public int xt_length; // slot count
  }

}
