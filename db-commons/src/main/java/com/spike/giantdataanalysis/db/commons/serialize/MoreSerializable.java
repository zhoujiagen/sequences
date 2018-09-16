package com.spike.giantdataanalysis.db.commons.serialize;


public interface MoreSerializable {

  int BOOLEAN_MAX_STRING_LEN = Math.max(//
    String.valueOf(Boolean.TRUE).length(), String.valueOf(Boolean.FALSE).length());
  int BYTE_MAX_STRING_LEN = Math.max(//
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

  // TODO(zhoujiagen) when used in nested, its rather complicated!!!

  String ARRAY_JOIN_SEP = ",";
  char PAD_NUMBER = '0';
  String SEP = ".|.";
  String SEP_LIST = "..|..";
  String BEGIN = "[[";
  String END = "]]";





  class Ops {
    public static String[] to(long[] longs) {
      int len = longs.length;
      if (len == 0) {
        return new String[0];
      }
      String[] result = new String[len];
      for (int i = 0; i < len; i++) {
        result[i] = String.valueOf(longs[i]);
      }
      return result;
    }
  }
}
