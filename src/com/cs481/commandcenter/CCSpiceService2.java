package com.cs481.commandcenter;

import java.io.File;

import android.app.Application;

import com.cs481.commandcenter.responses.Response;
import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.exception.CacheLoadingException;
import com.octo.android.robospice.persistence.exception.CacheSavingException;
import com.octo.android.robospice.persistence.file.InFileObjectPersister;

public class CCSpiceService2 extends SpiceService {

	@Override
	public CacheManager createCacheManager(Application application)
			throws CacheCreationException {
		CacheManager cacheManager = new CacheManager();

		InFileObjectPersister<Response> responsePersister = new InFileObjectPersister<Response>(
				application, Response.class) {
			@Override
			protected Response readCacheDataFromFile(File file)
					throws CacheLoadingException {
				return ResponsePersister.getSerializableFromCache(file);
			}

			@Override
			public Response saveDataToCacheAndReturnData(Response Response,
					Object cacheKey) throws CacheSavingException {
				ResponsePersister.saveSerializableToCache(Response, getCacheFile(cacheKey));
				return Response;
			}
		};

		cacheManager.addPersister(responsePersister);
		return cacheManager;
	}
}