package com.spike.giantdataanalysis.sequences.serialize;

import java.io.Serializable;

// for more representative work
public interface Stringable<T> extends Serializable {
  String asString();

  int size();

  T fromString(String raw);
}