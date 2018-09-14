package com.spike.giantdataanalysis.sequences.filesystem.core.cache;

public interface CacheKey {
  public boolean equals(Object obj);

  public int hashCode();
}
