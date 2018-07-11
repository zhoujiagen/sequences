package com.spike.giantdataanalysis.sequences.server;

/** Transaction identifier */
public class XID {
  long formatID; // format identifier: -1 means null
  long gtrid_length; // value from 1 through 64
  long bqual_length; // value from 1 through 64
  char data[] = new char[128];
}