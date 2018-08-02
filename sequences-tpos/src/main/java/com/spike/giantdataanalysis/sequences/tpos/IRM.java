package com.spike.giantdataanalysis.sequences.tpos;

import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.OutParameter;

/**
 * Resource Manager.
 */
public interface IRM {

  String RMNAME();

  /** identify the specific instantiation. */
  class rmInstance {
    public long nodeid;// NODEID
    public long pid; // PID
    public long birthdate;// TIMESTAMP
  }

  /** Resource Manager parameters. */
  class rmParams {
    public String CB_bytes;// CB_length
  }

  /** A stateful interaction. */
  class BindId {
    public rmInstance EndA;
    public rmInstance EndB;
    int SeqNo;
  }

  BindId rmBind(rmInstance clientID);

  boolean rmUnbind(BindId bindId);

  /**
   * Invocation of RM named <code>RMNAME</code>.
   * @param RMNAME
   * @param BoundTo
   * @param InParams
   * @param OutResults
   * @return
   */
  boolean rmCall(String RMNAME, BindId BoundTo, //
      rmParams InParams, OutParameter<rmParams> OutResults);

  // ---------------------------------------------------------------------------
  // support for transaction
  // ---------------------------------------------------------------------------

  boolean rm_Prepare();

  boolean rm_Rollback_Savepoint(/* Savepoint */);

  boolean rm_Commit(Boolean flag);

  boolean rm_Savepoint();

  void rm_UNDO(/* buffer of log record */);

  void rm_Abort();

  void rm_REDO(/* buffer of log record */);

  void rm_Checkpoint();

  void rm_restart(/* LSN */);

  void rm_Startup();

  void rm_Shutdown();
}
