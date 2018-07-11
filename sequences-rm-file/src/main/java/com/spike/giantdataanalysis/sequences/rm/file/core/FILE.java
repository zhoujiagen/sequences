package com.spike.giantdataanalysis.sequences.rm.file.core;

/**
 * File handler
 */
public class FILE {

  public String filename;
  public int fileno; // file no
  public FILEBlock[] blocks;

  public FILE() {
  }

  public FILE(int fileno, String filename) {
    this.fileno = fileno;
    this.filename = filename;
  }

  @Override
  public String toString() {
    return "FILE [filename=" + filename + ", fileno=" + fileno + "]";
  }

}
