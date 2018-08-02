package com.spike.giantdataanalysis.sequences.core.cb;

import com.spike.giantdataanalysis.sequences.core.locking.lock.lock_request;

/**
 * Transaction Control Block.
 */
public class TransCB {
  public lock_request locks;
  public lock_request wait;
  public TransCB cycle; // for dead lock detection
  // ...
}
