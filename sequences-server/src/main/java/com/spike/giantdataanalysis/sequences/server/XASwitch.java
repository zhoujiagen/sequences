package com.spike.giantdataanalysis.sequences.server;

/**
 * XA Switch Data Structure: xa_switch_t.
 */
public class XASwitch {
  String name; // name of resource manger
  long flags;// options specific to the resource manager
  long version; // must be zero

  IRM rm;
}
