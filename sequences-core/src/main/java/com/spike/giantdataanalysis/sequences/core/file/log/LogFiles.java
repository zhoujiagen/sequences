package com.spike.giantdataanalysis.sequences.core.file.log;

public class LogFiles {
  public String a_prefix; // pair log directroy 1
  public String b_prefix; // pair log directroy 2
  public long index; // index of log file in directory

  public LogFiles() {
  }

  public LogFiles(String a_prefix, String b_prefix, long index) {
    this.a_prefix = a_prefix;
    this.b_prefix = b_prefix;
    this.index = index;
  }

}
