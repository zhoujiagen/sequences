package com.spike.giantdataanalysis.sequences.rm.file.core;

/**
 * File handle: FILEID.
 */
public class FILE implements Comparable<FILE> { // TODO(zhoujiagen) need to check the structure

  public String filename;
  public int fileno; // FILENO: only effective after open
  // public BLOCK[] blocks;

  public FILE() {
  }

  public FILE(int fileno, String filename) {
    this.fileno = fileno;
    this.filename = filename;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((filename == null) ? 0 : filename.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    FILE other = (FILE) obj;
    if (filename == null) {
      if (other.filename != null) return false;
    } else if (!filename.equals(other.filename)) return false;
    return true;
  }

  @Override
  public int compareTo(FILE o) {
    return filename.compareTo(o.filename);
  }

  @Override
  public String toString() {
    return "FILE [filename=" + filename + ", fileno=" + fileno + "]";
  }
}
