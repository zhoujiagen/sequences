package com.spike.giantdataanalysis.sequences.server;

import com.spike.giantdataanalysis.sequences.commons.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.server.constant.TMConstant;

public interface ITM {
  // ---------------------------------------------------------------------------
  // routines
  // ---------------------------------------------------------------------------

  /**
   * dynamically register a RM with a TM.
   * @param rmid integer that the RM received when the TM called xa_open()
   * @param xid
   * @param flags reserved: TM_NOFLAGS
   * @return
   */
  int ax_reg(int rmid, OutParameter<XID> xid, long flags);

  /**
   * dynamically unregister a RM with a TM.
   * @param rmid integer that the RM received when the TM called xa_open()
   * @param flags reserved: TM_NOFLAGS
   * @return
   */
  int ax_unreg(int rmid, long flags);

  ITM DEFAULT = new ITM() {

    @Override
    public int ax_unreg(int rmid, long flags) {
      return TMConstant.TMER_TMERR;
    }

    @Override
    public int ax_reg(int rmid, OutParameter<XID> xid, long flags) {
      return TMConstant.TMER_TMERR;
    }
  };

}
