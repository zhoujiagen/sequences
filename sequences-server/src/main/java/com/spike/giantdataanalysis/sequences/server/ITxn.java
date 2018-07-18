package com.spike.giantdataanalysis.sequences.server;

import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.server.constant.TxnConstant.CommitReturn;
import com.spike.giantdataanalysis.sequences.server.constant.TxnConstant.ReturnCode;
import com.spike.giantdataanalysis.sequences.server.constant.TxnConstant.TransactionControl;

/**
 * TX(Transaction Demarcation) Interface.
 * <p>
 * REF X/Open CAE Specification: Distributed Transaction Processing: The TX (Transaction
 * Demarcation) Specification
 */
public interface ITxn {

  // ---------------------------------------------------------------------------
  // routines
  // ---------------------------------------------------------------------------

  /**
   * begin a global transaction.
   * @return
   */
  int tx_begin();

  /**
   * close a set of resource managers.
   * @return
   */
  int tx_close();

  /**
   * commit a global transaction.
   * @return
   */
  int tx_commit();

  /**
   * return global transaction information.
   * @param info
   * @return
   */
  int tx_info(OutParameter<TxInfo> info);

  /**
   * open a set of resource managers.
   * @return
   */
  int tx_open();

  /**
   * roll back a global transaction.
   * @return
   */
  int tx_rollback();

  /**
   * set commit_return characteristic
   * @param when_return
   * @return
   */
  int tx_set_commit_return(CommitReturn when_return);

  /**
   * set transaction_control characteristic.
   * @param control
   * @return
   */
  int tx_set_transaction_control(TransactionControl control);

  /**
   * set transaction_timeout characteristic.
   * @param timeout 0 means no timeout.
   * @return
   */
  int tx_set_transaction_timeout(long timeout);

  ITxn DEFAULT = new ITxn() {

    @Override
    public int tx_begin() {
      return ReturnCode.TX_NOT_SUPPORTED.code();
    }

    @Override
    public int tx_close() {
      return ReturnCode.TX_NOT_SUPPORTED.code();
    }

    @Override
    public int tx_commit() {
      return ReturnCode.TX_NOT_SUPPORTED.code();
    }

    @Override
    public int tx_info(OutParameter<TxInfo> info) {
      return ReturnCode.TX_NOT_SUPPORTED.code();
    }

    @Override
    public int tx_open() {
      return ReturnCode.TX_NOT_SUPPORTED.code();
    }

    @Override
    public int tx_rollback() {
      return ReturnCode.TX_NOT_SUPPORTED.code();
    }

    @Override
    public int tx_set_commit_return(CommitReturn when_return) {
      return ReturnCode.TX_NOT_SUPPORTED.code();
    }

    @Override
    public int tx_set_transaction_control(TransactionControl control) {
      return ReturnCode.TX_NOT_SUPPORTED.code();
    }

    @Override
    public int tx_set_transaction_timeout(long timeout) {
      return ReturnCode.TX_NOT_SUPPORTED.code();
    }
  };
}
