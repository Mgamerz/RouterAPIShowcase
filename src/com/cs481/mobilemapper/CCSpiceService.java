package com.cs481.mobilemapper;

import android.app.Application;

import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.springandroid.json.jackson2.Jackson2ObjectPersisterFactory;

public class CCSpiceService extends SpiceService {

  @Override
  public CacheManager createCacheManager( Application application ) throws CacheCreationException {
    CacheManager cacheManager = new CacheManager();
    Jackson2ObjectPersisterFactory jacksonObjectPersisterFactory = new Jackson2ObjectPersisterFactory( application );
    //InMemoryLRUCacheObjectPersister()
    cacheManager.addPersister( jacksonObjectPersisterFactory );
    return cacheManager;
  }
}