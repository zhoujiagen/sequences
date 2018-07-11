package com.spike.giantdataanalysis.sequences.rm.file.core;

import java.io.Serializable;

public interface MoreSerializable {

  int BOOLEAN_MAX_STRING_LEN = Math.max(//
    String.valueOf(Boolean.TRUE).length(), String.valueOf(Boolean.FALSE).length());
  int Byte_MAX_STRING_LEN = Math.max(//
    String.valueOf(Byte.MAX_VALUE).length(), String.valueOf(Byte.MIN_VALUE).length());
  int CHARACTER_MAX_STRING_LEN = Math.max(//
    String.valueOf(Character.MAX_VALUE).length(), String.valueOf(Character.MIN_VALUE).length());
  int SHORT_MAX_STRING_LEN = Math.max(//
    String.valueOf(Short.MAX_VALUE).length(), String.valueOf(Short.MIN_VALUE).length());
  int INTEGER_MAX_STRING_LEN = Math.max(//
    String.valueOf(Integer.MAX_VALUE).length(), String.valueOf(Integer.MIN_VALUE).length());
  int LONG_MAX_STRING_LEN = Math.max(//
    String.valueOf(Long.MAX_VALUE).length(), String.valueOf(Long.MIN_VALUE).length());
  int FLOAT_MAX_STRING_LEN = Math.max(//
    String.valueOf(Float.MAX_VALUE).length(), String.valueOf(Float.MIN_VALUE).length());
  int DOUBLE_MAX_STRING_LEN = Math.max(//
    String.valueOf(Double.MAX_VALUE).length(), String.valueOf(Double.MIN_VALUE).length());

  String SEP = ".|.";
  String BEGIN = "[[";
  String END = "]]";

  // for performance critical
  interface Byteable<T> extends Serializable {
    byte[] toBytes();

    int size();

    T fromBytes(byte[] bytes);
  }

  // for more representative work
  interface Stringable<T> extends Serializable {
    String asString();

    int size();

    T fromString(String raw);
  }
}
