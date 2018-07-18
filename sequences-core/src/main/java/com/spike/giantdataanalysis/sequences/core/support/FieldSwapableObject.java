package com.spike.giantdataanalysis.sequences.core.support;

/** Hepler class for CAS on field of different (nested) class object */
public interface FieldSwapableObject<O> {
  O out();

  void in(O object);
}