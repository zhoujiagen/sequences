package com.spike.giantdataanalysis.sequences.rm.file.core;

// extent-based allocation

/**
 * Allocation parameter.
 */
public class allocparmp {
  public int primary; // initial allocation request
  public int secondary; // next allocation request
  public float growth; // next allocation increase factor
}
