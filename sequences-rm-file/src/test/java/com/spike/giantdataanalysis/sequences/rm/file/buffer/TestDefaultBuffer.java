package com.spike.giantdataanalysis.sequences.rm.file.buffer;

import com.spike.giantdataanalysis.sequences.commons.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.rm.file.IBufferManager;
import com.spike.giantdataanalysis.sequences.rm.file.IBufferManager.BUFFER_ACC_CB;
import com.spike.giantdataanalysis.sequences.rm.file.core.PAGEID;
import com.spike.giantdataanalysis.sequences.rm.file.core.PAGE_TYPE;

public class TestDefaultBuffer {
  public static void main(String[] args) {

    IBufferManager buffer = new DefaultBufferManager();

    PAGEID pageid = new PAGEID(0, 0, PAGE_TYPE.DATA);
    int lock_mode = -1;
    OutParameter<BUFFER_ACC_CB> out_bacb = new OutParameter<>(new BUFFER_ACC_CB());
    buffer.buffer_fix(pageid, lock_mode, out_bacb);

    BUFFER_ACC_CB bacb = out_bacb.value();

    buffer.flush(bacb);
    buffer.buffer_unfix(bacb);

    out_bacb = new OutParameter<>(new BUFFER_ACC_CB());
    pageid = new PAGEID(0, 0, PAGE_TYPE.DATA);
    buffer.empty_fix(pageid, lock_mode, out_bacb);
  }
}
