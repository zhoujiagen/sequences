package com.spike.giantdataanalysis.sequences.server;

import com.spike.giantdataanalysis.sequences.server.constant.TxnConstant.CommitReturn;
import com.spike.giantdataanalysis.sequences.server.constant.TxnConstant.TransactionControl;
import com.spike.giantdataanalysis.sequences.server.constant.TxnConstant.TransactionState;

// TXINFO
public class TxInfo {
  XID xid;
  CommitReturn when_return;
  TransactionControl transaction_control;
  long transaction_timeout; // TRANSACTION_TIMEOUT
  TransactionState transaction_state;
}