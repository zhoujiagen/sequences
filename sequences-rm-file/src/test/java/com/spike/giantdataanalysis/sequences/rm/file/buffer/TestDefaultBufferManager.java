package com.spike.giantdataanalysis.sequences.rm.file.buffer;

import com.spike.giantdataanalysis.sequences.core.file.page.PAGEID;
import com.spike.giantdataanalysis.sequences.core.file.page.PAGE_TYPE;
import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.rm.file.IBM;
import com.spike.giantdataanalysis.sequences.rm.file.IBM.BUFFER_ACC_CB;

public class TestDefaultBufferManager {
  public static void main(String[] args) {

    IBM buffer = new DefaultBufferManager();

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
