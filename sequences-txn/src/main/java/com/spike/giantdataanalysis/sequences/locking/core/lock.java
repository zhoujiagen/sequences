package com.spike.giantdataanalysis.sequences.locking.core;

import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.spike.giantdataanalysis.sequences.txn.core.TransCB;

public interface lock {

  // ---------------------------------------------------------------------------
  // Constants.
  // ---------------------------------------------------------------------------
  /** Class of Lock: lock duration. */
  enum LOCK_CLASS {
    /** instant duration */
    LOCK_INSTANT,
    /** short duration */
    LOCK_SHORT,
    /** medium duration */
    LOCK_MEDIUM,
    /** long duration */
    LOCK_LONG,
    /** very long duration */
    LOCK_VERY_LONG
  }

  enum LOCK_MODE {
    /** free lock */
    LOCK_FREE,
    /** share */
    LOCK_S,
    /** exclusive */
    LOCK_X,
    /** update */
    LOCK_U,
    /** intent share */
    LOCK_IS,
    /** intent exclusive */
    LOCK_IX,
    /** share and intent exclusive */
    LOCK_SIX,
    /** waiting */
    LOCK_WAIT
  }

  /** Status of lock request. */
  enum LOCK_STATUS {
    /***/
    LOCK_GRANTED,
    /***/
    LOCK_CONVERING,
    /***/
    LOCK_WAITING,
    /***/
    LOCK_DENIED
  }

  // ---------------------------------------------------------------------------
  // data structures
  // ---------------------------------------------------------------------------
  /** RM Specific Lock Name. */
  class lock_name {
    public int rmid;// RM id
    public String resource; // name defined by RM

    public lock_name(int rmid, String resource) {
      this.rmid = rmid;
      this.resource = resource;
    }

    public long lockhash() {
      return Hashing.sha1().hashString(String.valueOf(rmid) + resource, Charsets.UTF_8).asLong();
    }
  }

  class lock_head {
    public xsemaphore Xsem;
    public lock_head chain;
    public lock_name name;
    public List<lock_request> queue;
    public LOCK_MODE granted_mode;
    public boolean waiting;
  }

  /** Lock request in a transaction. */
  class lock_request {
    public lock_request queue; // after this request
    public lock_head head;
    public LOCK_STATUS status;
    public LOCK_MODE mode;
    public LOCK_MODE convert_mode; // the result mode if converting
    public int count; // locked count
    public LOCK_CLASS lockClass; // lock duration
    public PCB process;
    public TransCB tran; // txt list
    public lock_request tran_prev;
    public lock_request tran_next;
  }

}
