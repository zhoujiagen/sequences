package com.spike.giantdataanalysis.sequences.core.support;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.CPointer;

/**
 * Atomic Operations.
 * <p>
 * more detailes see
 * <code>compareAndSwapObject</code>,<code>compareAndSwapInt</code>,<code>compareAndSwapLong</code>
 * in <code>sun.misc.Unsafe</code>
 * @see AtomicInteger
 * @see AtomicLong
 * @see AtomicReference
 */
public abstract class AtomicOps {

  private AtomicOps() {
  }

  // compare and swap on integer
  public static boolean CS(CPointer<Integer> cell, CPointer<Integer> old, CPointer<Integer> _new) {
    if (cell.value().equals(old.value())) {
      cell.value(_new.value());
      return true;
    } else {
      old.value(cell.value());
      return false;
    }
  }

  // compare and swap on long
  public static boolean CSD(CPointer<Long> cell, CPointer<Long> old, CPointer<Long> _new) {
    if (cell.value().equals(old.value())) {
      cell.value(_new.value());
      return true;
    } else {
      old.value(cell.value());
      return false;
    }
  }
}