package com.spike.giantdataanalysis.sequences.server.test.txn.xa;

import java.util.Random;

import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.server.IRM;
import com.spike.giantdataanalysis.sequences.server.ITM;
import com.spike.giantdataanalysis.sequences.server.XID;
import com.spike.giantdataanalysis.sequences.server.constant.RMConstant;

// TODO(zhoujiagen) give implementation for IRM, ITM, ITxn
public class TestXA {

  @SuppressWarnings("unused")
  public static void main(String[] args) {

    IRM rm = IRM.DEFAULT;
    ITM tm = ITM.DEFAULT;

    int rmid = new Random().nextInt();
    long flags = RMConstant.TM_NOFLAGS;
    int xa_oepn_r = rm.xa_open("DEFAULT_RM", rmid, flags);

    OutParameter<XID> xid = new OutParameter<XID>();
    int ax_reg_r = tm.ax_reg(rmid, xid, flags);

    int xa_start_r = rm.xa_start(xid.value(), rmid, flags);
    int xa_end_r = rm.xa_end(xid.value(), rmid, flags);

    int xa_prepare_r = rm.xa_prepare(xid.value(), rmid, flags);
    int xa_commit_r = rm.xa_commit(xid, rmid, flags);
  }
}
