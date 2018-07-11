package com.spike.giantdataanalysis.sequences.rm.file.core;

public class FILEDescriptor {
  public String filename;
  public int fileno;
  public int partition_no;
  public int version_no;
  public allocparmp spacerequest;
  public int curr_no_extends; // number of currently allocated extents

  public ExtentAllocated[] curr_alloc;// new AllocatedExtent[FileConfiguration.EXTENT_MAX_SIZE];

}
