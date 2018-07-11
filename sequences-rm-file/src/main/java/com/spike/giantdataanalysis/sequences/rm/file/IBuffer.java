package com.spike.giantdataanalysis.sequences.rm.file;

import com.spike.giantdataanalysis.sequences.rm.file.buffer.BufferAccessControlBlock;

/**
 * The buffer: fix pages, unfix pages, flush pages
 */
public interface IBuffer {

  void buffer_fix(int pageid, int lock_mode, BufferAccessControlBlock bacb);

  boolean buffer_unfix(BufferAccessControlBlock bacb);

  boolean empty_fix(int pageid, int lock_mode, BufferAccessControlBlock bacb);

  boolean flush(BufferAccessControlBlock bacb);

}
