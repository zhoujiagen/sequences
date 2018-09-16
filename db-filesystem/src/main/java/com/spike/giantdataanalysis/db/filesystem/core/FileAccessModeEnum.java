package com.spike.giantdataanalysis.db.filesystem.core;

/**
 * File Access Mode.
 */
public enum FileAccessModeEnum {
  /** read */
  R,
  /** update */
  U,
  /** append */
  A;

  public static boolean isCompatible(FileAccessModeEnum before, FileAccessModeEnum after) {
    if (before.equals(after)) return true;
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
