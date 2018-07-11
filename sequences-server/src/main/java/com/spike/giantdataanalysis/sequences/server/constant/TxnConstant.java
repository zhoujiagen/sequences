package com.spike.giantdataanalysis.sequences.server.constant;

public interface TxnConstant {

  /** commit return values: COMMIT_RETURN. */
  enum CommitReturn {
    TX_COMMIT_COMPLETED(0, ""), //
    TX_COMMIT_DECISION_LOGGED(1, "");

    long value;
    String description;

    private CommitReturn(long value, String description) {
      this.value = value;
      this.description = description;
    }
  }

  /** transaction control values: COMMIT_RETURN. */
  enum TransactionControl {
    TX_UNCHAINED(0, ""), //
    TX_CHAINED(1, "");

    long value;
    String description;

    private TransactionControl(long value, String description) {
      this.value = value;
      this.description = description;
    }
  }

  /** transaction state values */
  enum TransactionState {
    TX_ACTIVE(0, ""), //
    TX_TIMEOUT_ROLLBACK_ONLY(1, ""), //
    TX_ROLLBACK_ONLY(2, "");

    long value;
    String description;

    private TransactionState(long value, String description) {
      this.value = value;
      this.description = description;
    }
  }

  /** return code of routines */
  enum ReturnCode {
    TX_NOT_SUPPORTED(1, "option not supported"), //
    TX_OK(0, "normal execution"), //
    TX_OUTSIDE(-1, "application is in an RM local transaction"), //
    TX_ROLLBACK(-2, "transaction was rolled back"), //
    TX_MIXED(-3, "transaction was partially committed and partially rolled back"), //
    TX_HAZARD(-4, "transaction may have been partially  committed and partially rolled back"), //

    TX_PROTOCOL_ERROR(-5, "routine invoked in an improper contex"), //

    TX_ERROR(-6, "transient error"), //
    TX_FAIL(-7, "fatal error"), //
    TX_EINVAL(-8, "invalid arguments were given"), //
    TX_COMMITTED(-9, "transaction has heuristically committed"), //

    TX_NO_BEGIN(-100, "transaction committed plus new transaction could not be started"), //
    TX_ROLLBACK_NO_BEGIN(-100 + (-2),
        "transaction rollback plus new transaction could not be started"), //
    TX_MIXED_NO_BEGIN(-100 + (-3), "mixed plus new transaction could not be started"), //
    TX_HAZARD_NO_BEGIN(-100 + (-4), "hazard plus new transaction could not be started"), //
    TX_COMMITTED_NO_BEGIN(-100 + (-9),
        "heuristically committed plus new transaction could not be started"), //
    ;

    final int code;
    final String description;

    private ReturnCode(int code, String description) {
      this.code = code;
      this.description = description;
    }

    public int code() {
      return code;
    }
  }
}
