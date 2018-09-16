package com.spike.giantdataanalysis.db.commons.serialize;

import java.io.Serializable;

// for performance critical
public interface Byteable<T> extends Serializable {
  byte[] toBytes();

  /** size in bit. */
  int size();

  T fromBytes(byte[] bytes);
}