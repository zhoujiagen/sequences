package com.spike.giantdataanalysis.sequences.rm.file.core;

public interface Byteable<T> {
  byte[] toBytes();

  int size();

  T fromBytes(byte[] bytes);
}
