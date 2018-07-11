package com.spike.giantdataanalysis.sequences.rm.file.core;

// extent-based allocation
public class allocparmp {
  int primary; // initial allocation request
  int secondary; // next allocation request
  float growth; // next allocation increase factor
}
