package com.spike.giantdataanalysis.sequences.txn.core;

import com.spike.giantdataanalysis.sequences.locking.core.lock.lock_request;

/** Transaction Control Block. */
public class TransCB {
  public lock_request locks;
  public lock_request wait;
  public TransCB cycle; // for dead lock detection
  // ...
}
