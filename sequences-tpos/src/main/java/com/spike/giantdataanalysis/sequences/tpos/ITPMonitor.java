package com.spike.giantdataanalysis.sequences.tpos;

/**
 * Transaction Processing Monitor, duties include:
 * <ul>
 * <li>bring up the TPOS
 * <li>administrate all transaction-related resources
 * <li>etc
 */
public interface ITPMonitor {

  class AccessControlList {
  }

  long/* RMID */rmInstall(String RMNAME, IRM/* rm_callbacks */callbacks, AccessControlList acl);

  boolean rmRevome(long RMID);

  boolean rmDeactivate(long RMID);

  boolean rmActivate(long RMID);

}
