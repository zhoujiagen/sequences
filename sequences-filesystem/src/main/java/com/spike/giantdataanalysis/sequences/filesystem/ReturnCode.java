package com.spike.giantdataanalysis.sequences.filesystem;

public enum ReturnCode {
  OK(0), FAIL(1);
  private int code;

  ReturnCode(int code) {
    this.code = code;
  }

  public int code() {
    return code;
  }
}