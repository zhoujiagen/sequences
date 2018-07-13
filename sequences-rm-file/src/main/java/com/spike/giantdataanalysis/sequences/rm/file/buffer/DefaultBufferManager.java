package com.spike.giantdataanalysis.sequences.rm.file.buffer;

import java.util.concurrent.Semaphore;

import com.spike.giantdataanalysis.sequences.commons.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.rm.file.IBM;
import com.spike.giantdataanalysis.sequences.rm.file.config.FileConfiguration;
import com.spike.giantdataanalysis.sequences.rm.file.core.file.FILE;
import com.spike.giantdataanalysis.sequences.rm.file.core.page.PAGEID;
import com.spike.giantdataanalysis.sequences.rm.file.core.page.PAGE_HEADER;
import com.spike.giantdataanalysis.sequences.rm.file.core.log.LSN;

public class DefaultBufferManager implements IBM {

  // FIXME(zhoujiagen) well, it's rather complicated here!!!
  // ---------------------------------------------------------------------------
  // inner structure
  // ---------------------------------------------------------------------------
  static class Element {
    PAGE_HEADER page_header;
    byte[] page_body[];
  }

  // BUFFER_CBP
  static class BUFFER_CB {
    PAGEID pageid;
    FILE fileid;
    int frame_index;
    Semaphore pagesem;
    boolean modified;
    int fixcount;
    LSN forminlsn;

    BUFFER_CB prev_in_LRU; // previous page in LRU chain
    BUFFER_CB next_in_LRU; // next in LRU chain
    BUFFER_CB next_in_hclass; // hashing overflow list
  }

  static class hash_entry {
    Semaphore class_sem;
    BUFFER_CB first_cbp;
  }

  // ---------------------------------------------------------------------------
  // inner state
  // ---------------------------------------------------------------------------
  Element[] bufferpool = new Element[FileConfiguration.BUFFER_MAX_SIZE]; // size fixed

  long no_free_frames; // number of free frames
  int[] free_frames = new int[FileConfiguration.BUFFER_MAX_SIZE]; // free frame indexes
  Semaphore free_frame_sem; // on free_frames

  hash_entry[] buffer_hash = new hash_entry[FileConfiguration.BUFFER_HASH_SIZE];
  BUFFER_CB mru_page;
  BUFFER_CB lru_page;
  Semaphore LRU_sem;

  BUFFER_CB free_cb_list;
  int no_free_cbs;
  Semaphore free_cb_sem;

  BUFFER_ACC_CB free_acc_cb_list;
  int no_free_acc_cb;
  Semaphore free_acb_sem;

  // ---------------------------------------------------------------------------
  // methods
  // ---------------------------------------------------------------------------

  @Override
  public void buffer_fix(PAGEID pageid, int lock_mode, OutParameter<BUFFER_ACC_CB> bacb) {
    // TODO Implement IBuffer.buffer_fix
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean buffer_unfix(BUFFER_ACC_CB bacb) {
    // TODO Implement IBuffer.buffer_unfix
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean empty_fix(PAGEID pageid, int lock_mode, OutParameter<BUFFER_ACC_CB> bacb) {
    // TODO Implement IBuffer.empty_fix
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean flush(BUFFER_ACC_CB bacb) {
    // TODO Implement IBuffer.flush
    throw new UnsupportedOperationException();
  }

}
