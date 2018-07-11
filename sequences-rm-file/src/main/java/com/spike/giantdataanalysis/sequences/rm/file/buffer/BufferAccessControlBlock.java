package com.spike.giantdataanalysis.sequences.rm.file.buffer;

import java.util.concurrent.Semaphore;

// BUFFER_ACC_CB, BUFFER_ACC_CBP
public class BufferAccessControlBlock {
  public int pageid; // page id in file
  public int pageaddr;// page base address in buffer pool
  public int index;// index of record in page
  public Semaphore pagesem;
  public boolean modified;
  public boolean invalid;
}
