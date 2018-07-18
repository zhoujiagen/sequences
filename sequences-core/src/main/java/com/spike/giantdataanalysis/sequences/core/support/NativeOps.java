package com.spike.giantdataanalysis.sequences.core.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.CPointer;

public class NativeOps {
  private static final Logger LOG = LoggerFactory.getLogger(NativeOps.class);

  // ---------------------------------------------------------------------------
  // CAS
  // ---------------------------------------------------------------------------

  /** compare and swap on integer */
  public static boolean CS(CPointer<Integer> cell, CPointer<Integer> old, CPointer<Integer> _new) {
    if (cell.value().equals(old.value())) {
      cell.value(_new.value());
      return true;
    } else {
      old.value(cell.value());
      return false;
    }
  }

  /** compare and swap on long */
  public static boolean CSD(CPointer<Long> cell, CPointer<Long> old, CPointer<Long> _new) {
    if (cell.value().equals(old.value())) {
      cell.value(_new.value());
      return true;
    } else {
      old.value(cell.value());
      return false;
    }
  }

  /** compare and swap on object */
  public static <T> boolean CSP(CPointer<T> cell, CPointer<T> old, CPointer<T> _new) {
    if (cell.value().equals(old.value())) {
      cell.value(_new.value());
      return true;
    } else {
      old.value(cell.value());
      return false;
    }
  }

  /**
   * compare and swap on field of object.
   * @param cell
   * @param old
   * @param _new
   * @return
   */
  public static synchronized <T> boolean CSF(FieldSwapableObject<T> cell,
      FieldSwapableObject<T> old, FieldSwapableObject<T> _new) {
    LOG.debug("cell={}, old={}, _new={}", cell, old, _new);

    if (cell.out().equals(old.out())) {

      LOG.debug("cell[{}] <= new[{}]", cell.out(), _new.out());
      cell.in(_new.out());
      return true;

    } else {

      LOG.debug("old[{}] <= cell[{}]", old.out(), cell.out());
      old.in(cell.out());
      return false;
    }
  }

}
