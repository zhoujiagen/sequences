package com.spike.giantdataanalysis.sequences.rm.file.core;

import com.spike.giantdataanalysis.sequences.rm.file.core.log.LSN;

public class PAGE_HEADER {
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
  byte[] stuff;

}
