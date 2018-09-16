package com.spike.giantdataanalysis.db.commons.cache;

public interface CacheKey {
  public boolean equals(Object obj);

  public int hashCode();
}
