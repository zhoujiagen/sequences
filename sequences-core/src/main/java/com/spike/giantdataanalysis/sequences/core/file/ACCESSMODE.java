package com.spike.giantdataanalysis.sequences.core.file;

public enum ACCESSMODE {
  /** read */
  R,
  /** update */
  U,
  /** append */
  A;

  public static boolean isCompatible(ACCESSMODE before, ACCESSMODE after) {
    if (before == R && after == R) return true;
    if (before == U && after == U) return true;
    if (before == A && after == A) return true;
    return false;
  }

  public String rafMode() {
    switch (this) {
    case R:
      return "r";
    case U:
      return "rwd";
    case A:
      return "rwd";
    default:
      return "r";
    }
  }
}
