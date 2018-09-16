package com.spike.giantdataanalysis.db.filesystem.core.cache;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.db.commons.cache.CacheEntity;
import com.spike.giantdataanalysis.db.commons.cache.CacheKey;

public final class FileSystemCache {
  private static final Logger LOG = LoggerFactory.getLogger(FileSystemCache.class);

  private FileSystemCache() {
  }

  private static FileSystemCache INSTANCE = new FileSystemCache();
  private Object cacheLock = new Object();

  private Map<CacheTargetEnum, Map<CacheKey, CacheEntity>> CACHE = Maps.newConcurrentMap();

  public void put(CacheTargetEnum cacheTargetEnum, CacheKey cacheKey, CacheEntity cacheEntity) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("put {}-{}: {}", cacheTargetEnum, cacheKey, cacheEntity);
    }

    forceCacheTargetEnumExist(cacheTargetEnum);
    CACHE.get(cacheTargetEnum).put(cacheKey, cacheEntity);
  }

  public CacheEntity get(CacheTargetEnum cacheTargetEnum, CacheKey cacheKey) {
    forceCacheTargetEnumExist(cacheTargetEnum);
    CacheEntity result = CACHE.get(cacheTargetEnum).get(cacheKey);
    if (LOG.isDebugEnabled()) {
      LOG.debug("get {}-{}: {}", cacheTargetEnum, cacheKey, result);
    }
    return result;
  }

  public void remove(CacheTargetEnum cacheTargetEnum, CacheKey cacheKey) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("remove {}-{}", cacheTargetEnum, cacheKey);
    }

    forceCacheTargetEnumExist(cacheTargetEnum);
    CACHE.get(cacheTargetEnum).remove(cacheLock);
  }

  private void forceCacheTargetEnumExist(CacheTargetEnum cacheTargetEnum) {
    Map<CacheKey, CacheEntity> innerMap = CACHE.get(cacheTargetEnum);
    if (innerMap == null) {
      synchronized (cacheLock) {
        CACHE.put(cacheTargetEnum, Maps.<CacheKey, CacheEntity> newConcurrentMap());
      }
    }
  }

  public static FileSystemCache I() {
    return INSTANCE;
  }

  public void clear() {
    CACHE = Maps.newConcurrentMap();
  }

}
