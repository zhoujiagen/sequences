package com.spike.giantdataanalysis.sequences.rm.file.core;

public class PAGEID {
  public int fileno; // file no
  public int pageno; // page index in file

  public PAGE_TYPE page_type;

  public PAGEID() {
  }

  public PAGEID(int fileno, int pageno, PAGE_TYPE page_type) {
    this.fileno = fileno;
    this.pageno = pageno;
    this.page_type = page_type;
  }
}
