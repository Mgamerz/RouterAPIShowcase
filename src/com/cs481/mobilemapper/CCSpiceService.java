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
    cacheManager.addPersister( jacksonObjectPersisterFactory );
    return cacheManager;
  }
/*
  @Override
  public RestTemplate createRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    //find more complete examples in RoboSpice Motivation app
    //to enable Gzip compression and setting request timeouts.

    // web services support json responses
    MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
    ObjectMapper mapper = jsonConverter.getObjectMapper();
    
    
    
    
    List<MediaType> types = jsonConverter.getSupportedMediaTypes();
    List<MediaType> newtypes = new ArrayList<MediaType>();
    newtypes.add(MediaType.APPLICATION_FORM_URLENCODED);
    newtypes.add(MediaType.APPLICATION_JSON);
    //types.add(MediaType.APPLICATION_FORM_URLENCODED);
    jsonConverter.setSupportedMediaTypes(newtypes);
    //jsonConverter.setPrefixJson(prefixJson);
    
    //FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
    //FormHttpMessageConverter xmlformHttpMessageConverter = new XmlAwareFormHttpMessageConverter();
    //StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
    final List< HttpMessageConverter< ? >> listHttpMessageConverters = restTemplate.getMessageConverters();

    listHttpMessageConverters.add( jsonConverter );
    //listHttpMessageConverters.add( formHttpMessageConverter );
    //listHttpMessageConverters.add( xmlformHttpMessageConverter );
    //listHttpMessageConverters.add( stringHttpMessageConverter );
    restTemplate.setMessageConverters( listHttpMessageConverters );
    return restTemplate;
  }*/
}