package com.spike.giantdataanalysis.sequences.server.constant;

public interface TMConstant {

  // ---------------------------------------------------------------------------
  // TM reports to RM
  // ---------------------------------------------------------------------------
  int TM_JOIN = 2; // caller is joining existing transaction branch
  int TM_RESUME = 1; // caller is resuming association with suspended transaction branch
  int TM_OK = 0; // normal execution
  int TMER_TMERR = -1; // an error occurred in the TM
  int TMER_INVAL = -2; // invalid arguments were given
  int TMER_PROTO = -3; // routine invoked in an improper context
}
