package com.spike.giantdataanalysis.sequences.rm.file.core;

import com.spike.giantdataanalysis.sequences.rm.file.core.log.LSN;

public interface page {
  // ---------------------------------------------------------------------------
  // Data Structure
  // ---------------------------------------------------------------------------
  class PAGEID {
    public int fileno; // file no
    public int pageno; // page no in file

    public PAGE_TYPE page_type;

    public PAGEID() {
    }

    public PAGEID(int fileno, int pageno, PAGE_TYPE page_type) {
      this.fileno = fileno;
      this.pageno = pageno;
      this.page_type = page_type;
    }
  }

  class PAGE_HEADER {
    PAGEID thatsme;
    PAGE_TYPE page_type;
    int object_id; // OBJID: inner id of relation or index
    LSN safe_up_to; // for WAL
    // in a linked list
    PAGEID previous;
    PAGEID next;
    PAGE_STATE status;
    int no_entries; // number of entry in page catalog

    int unused;// un-used non-contiguous bytes
    int freespace; // contiguous free bytes for payload
    byte[] stuff = new byte[0];
  }

  // ---------------------------------------------------------------------------
  // Constant
  // ---------------------------------------------------------------------------

  enum PAGE_STATE {
    VALID, //
    INVALID, //
    INDOUBT, //
    SHADOW
  }

  enum PAGE_TYPE {
    DATA, // tuples in one relation
    INDEX, // nodes in one index file
    FREESPACE, // free block table
    DIRECTORY, // metadata of files
    CLUSTER, // tuples from multiple relations
    TABLE// including management table
  }
}
