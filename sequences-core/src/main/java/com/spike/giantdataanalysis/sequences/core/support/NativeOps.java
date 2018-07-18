package com.spike.giantdataanalysis.sequences.core.support;

import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.CPointer;

public class NativeOps {
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

  public static <T> boolean CSO(CPointer<T> cell, CPointer<T> old, CPointer<T> _new) {
    if (cell.value().equals(old.value())) {
      cell.value(_new.value());
      return true;
    } else {
      old.value(cell.value());
      return false;
    }
  }

  // TODO(zhoujiagen) CAS on field of different class object

}
