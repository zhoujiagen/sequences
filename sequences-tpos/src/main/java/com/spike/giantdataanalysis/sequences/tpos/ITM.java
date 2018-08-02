package com.spike.giantdataanalysis.sequences.tpos;

/**
 * Transaction Manager.
 */
public interface ITM {

  /**
   * Introduce the RM to the TM.
   * <p>
   * Ask the TM to initiate restart recovery for the RM if necessary.
   * @param RMID
   * @return
   */
  boolean Identity(long RMID);
}