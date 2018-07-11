package com.spike.giantdataanalysis.sequences.server;

import java.util.List;

import com.spike.giantdataanalysis.sequences.commons.ICJavaAdapter.IOperation;
import com.spike.giantdataanalysis.sequences.commons.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.server.constant.RMConstant;

public interface IRM {

  // ---------------------------------------------------------------------------
  // routines
  // ---------------------------------------------------------------------------
  /**
   * close a resource manager.
   * @param xa_info instance-specific information for the RM
   * @param rmid TM generated when calling xa_open()
   * @param flags TM_ASYNC/TM_NOFLAGS
   * @return
   */
  int xa_close(String xa_info, int rmid, long flags);

  /**
   * commit work done on behalf of a transaction branch.
   * @param xid OUT
   * @param rmid TM generated when calling xa_open()
   * @param flags TM_NOWAIT/TM_ASYNC/TM_ONEPHASE/TM_NOFLAGS
   * @return
   */
  int xa_commit(OutParameter<XID> xid, int rmid, long flags);

  /**
   * wait for an asynchronous operation to complete.
   * @param handle the asynchronous operation
   * @param retval return value of the asynchronous operation
   * @param rmid TM generated when calling xa_open()
   * @param flags TM_MULTIPLE/TM_NOWAIT/TM_NOFLAGS
   * @return
   */
  int xa_complete(IOperation<Object, Integer> handle, OutParameter<Integer> retval, int rmid,
      long flags);

  /**
   * end work performed on behalf of a transaction branch.
   * @param xid either passed to the xa_start() call or returned from the ax_reg() call that
   *          established the thread’s association;
   * @param rmid TM generated when calling xa_open()
   * @param flags a bit complicated :-(
   * @return
   */
  int xa_end(XID xid, int rmid, long flags);

  /**
   * forget about a heuristically completed transaction branch.
   * @param xid
   * @param rmid TM generated when calling xa_open()
   * @param flags TM_ASYNC/TM_NOFLAGS
   * @return
   */
  int xa_forget(XID xid, int rmid, long flags);

  /**
   * open a RM.
   * @param xa_info instance-specific information for the RM
   * @param rmid assigned by the TM, uniquely identifies the called RM instance within the thread of
   *          control
   * @param flags TM_ASYNC/TM_NOFLAGS
   * @return
   */
  int xa_open(String xa_info, int rmid, long flags);

  /**
   * prepare to commit work done on behalf of a transaction branch.
   * @param xid
   * @param rmid TM generated when calling xa_open()
   * @param flags TM_ASYNC/TM_NOFLAGS
   * @return
   */
  int xa_prepare(XID xid, int rmid, long flags);

  /**
   * obtain a list of prepared transaction branches from a RM.
   * @param xids
   * @param rmid TM generated when calling xa_open()
   * @param flags TM_STARTRSCAN/TM_ENDRSCAN/TM_NOFLAGS
   * @return
   */
  int xa_recover(List<XID> xids, int rmid, long flags);

  /**
   * roll back work done on behalf of a transaction branch.
   * @param xid
   * @param rmid TM generated when calling xa_open()
   * @param flags TM_ASYNC/TM_NOFLAGS
   * @return
   */
  int xa_rollback(XID xid, int rmid, long flags);

  /**
   * start work on behalf of a transaction branch.
   * @param xid
   * @param rmid TM generated when calling xa_open()
   * @param flags TM_JOIN/TM_RESUME/TM_NOWAIT/TM_ASYNC/TM_NOFLAGS
   * @return
   */
  int xa_start(XID xid, int rmid, long flags);

  IRM DEFAULT = new IRM() {

    @Override
    public int xa_start(XID xid, int rmid, long flags) {
      return RMConstant.XAER_RMFAIL;
    }

    @Override
    public int xa_rollback(XID xid, int rmid, long flags) {
      return RMConstant.XAER_RMFAIL;
    }

    @Override
    public int xa_recover(List<XID> xids, int rmid, long flags) {
      return RMConstant.XAER_RMFAIL;
    }

    @Override
    public int xa_prepare(XID xid, int rmid, long flags) {
      return RMConstant.XAER_RMFAIL;
    }

    @Override
    public int xa_open(String xa_info, int rmid, long flags) {
      return RMConstant.XAER_RMFAIL;
    }

    @Override
    public int xa_forget(XID xid, int rmid, long flags) {
      return RMConstant.XAER_RMFAIL;
    }

    @Override
    public int xa_end(XID xid, int rmid, long flags) {
      return RMConstant.XAER_RMFAIL;
    }

    @Override
    public int xa_complete(IOperation<Object, Integer> handle, OutParameter<Integer> retval,
        int rmid, long flags) {
      return RMConstant.XAER_RMFAIL;
    }

    @Override
    public int xa_commit(OutParameter<XID> xid, int rmid, long flags) {
      return RMConstant.XAER_RMFAIL;
    }

    @Override
    public int xa_close(String xa_info, int rmid, long flags) {
      return RMConstant.XAER_RMFAIL;
    }
  };

}
