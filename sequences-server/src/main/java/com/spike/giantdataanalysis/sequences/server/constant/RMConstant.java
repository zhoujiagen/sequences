package com.spike.giantdataanalysis.sequences.server.constant;

public interface RMConstant {

  // ---------------------------------------------------------------------------
  // flag definition for XASwitch - XASwitch.flags
  // ---------------------------------------------------------------------------
  long TM_NOFLAGS = 0x00000000L; // no resource manager features selected

  long TM_REGISTER = 0x00000001L; // resource manager dynamically registers
  long TM_NOMIGRATE = 0x00000002L; // resource manager does not support association migration
  long TM_USEASYNC = 0x00000004L; // resource manager supports asynchronous operations

  // ---------------------------------------------------------------------------
  // flag definition for xa_, ax_ routines
  // ---------------------------------------------------------------------------
  long TM_ASYNC = 0x80000000L; // perform routine asynchronously
  long TM_ONEPHASE = 0x40000000L; // caller is using one-phase commit optimisation
  long TM_FAIL = 0x20000000L; // dissociates caller and marks transaction branch rollback-only
  long TM_NOWAIT = 0x10000000L; // return if blocking condition exists
  long TM_RESUME = 0x08000000L; // caller is resuming association with suspended transaction branch
  long TM_SUCCESS = 0x04000000L; // dissociate caller from transaction branch
  long TM_SUSPEND = 0x02000000L; // caller is suspending, not ending, association
  long TM_STARTRSCAN = 0x02000000L; // start a recovery scan
  long TM_ENDRSCAN = 0x00800000L; // end a recovery scan
  long TM_MULTIPLE = 0x00400000L; // wait for any asynchronous operation
  long TM_JOIN = 0x00200000L; // caller is joining existing transaction branch
  long TM_MIGRATE = 0x00100000L; // caller intends to perform migration

  // ---------------------------------------------------------------------------
  // RM reports to TM
  // ---------------------------------------------------------------------------
  int XA_RBBASE = 100; // the inclusive lower bound of the rollback codes
  int XA_RBROLLBACK = XA_RBBASE; // the rollback was caused by an unspecified reason
  int XA_RBCOMMFAIL = XA_RBBASE + 1; // the rollback was caused by a communication failure
  int XA_RBDEADLOCK = XA_RBBASE + 2;// a deadlock was detected
  // a condition that violates the integrity of the resources was detected
  int XA_RBINTEGRITY = XA_RBBASE + 3;
  // the resource manager rolled back the transaction branch for a reason not on this list
  int XA_RBOTHER = XA_RBBASE + 4;
  int XA_RBPROTO = XA_RBBASE + 5; // a protocol error occurred in the resource manager
  int XA_RBTIMEOUT = XA_RBBASE + 6; // a transaction branch took too long
  int XA_RBTRANSIENT = XA_RBBASE + 7; // may retry the transaction branch
  int XA_RBEND = XA_RBTRANSIENT; // the inclusive upper bound of the rollback codes

  int XA_NOMIGRATE = 9;// resumption must occur where suspension occurred
  int XA_HEURHAZ = 8; // the transaction branch may have been heuristically completed
  int XA_HEURCOM = 7; // the transaction branch has been heuristically committed
  int XA_HEURRB = 6; // the transaction branch has been heuristically rolled back
  int XA_HEURMIX = 5; // the transaction branch has been heuristically committed and rolled back
  int XA_RETRY = XA_RBBASE; // routine returned with no effect and may be reissued
  int XA_RDONLY = XA_RBBASE; // the transaction branch was read-only and has been committed

  int XA_OK = 0; // normal execution

  int XAER_ASYNC = -2; // asynchronous operation already outstanding
  int XAER_RMERR = -3; // a resource manager error occurred in the transaction branch

  int XAER_NOTA = -4; // the XID is not valid
  int XAER_INVAL = -5; // invalid arguments were given
  int XAER_PROTO = -6; // routine invoked in an improper context
  int XAER_RMFAIL = -7; // RM unavailable
  int XAER_DUPID = -8; // the XID already exists
  int XAER_OUTSIDE = -9; // resource manager doing work outside global transaction

}
