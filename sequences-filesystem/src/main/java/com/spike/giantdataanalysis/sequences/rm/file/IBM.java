package com.spike.giantdataanalysis.sequences.rm.file;

import java.util.concurrent.Semaphore;

import com.spike.giantdataanalysis.sequences.core.file.page.PAGEID;
import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.OutParameter;

/**
 * Buffer Manager: fix pages, unfix pages, flush pages.
 * <p>
 * Use the FIX_USER_UNFIX protocol.
 */
public interface IBM {

  /** Buffer Access Control Block. */
  class BUFFER_ACC_CB {
    public int pageid; // PAGEID: page id in file
    public int pageaddr;// PAGEPTR: page base address in buffer pool, setted by buffer manager
    public int index;// index of record in page
    public Semaphore pagesem;
    public boolean modified; // caller flag: modify page
    public boolean invalid; // caller flag: invalid page
  }

  /**
   * fix/allocated a page.
   * @param pageid
   * @param lock_mode
   * @param bacb
   */
  void buffer_fix(PAGEID pageid, int lock_mode, OutParameter<BUFFER_ACC_CB> bacb);

  /**
   * unfix a page.
   * @param bacb
   * @return
   */
  boolean buffer_unfix(BUFFER_ACC_CB bacb);

  /**
   * allocated a free page.
   * @param pageid
   * @param lock_mode
   * @param bacb
   * @return
   */
  boolean empty_fix(PAGEID pageid, int lock_mode, OutParameter<BUFFER_ACC_CB> bacb);

  boolean flush(BUFFER_ACC_CB bacb);

}
