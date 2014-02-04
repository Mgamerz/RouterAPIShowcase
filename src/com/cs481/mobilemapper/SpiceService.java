package com.cs481.mobilemapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.SpringAndroidSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.springandroid.json.jackson2.Jackson2ObjectPersisterFactory;

public class SpiceService extends SpringAndroidSpiceService {

  @Override
  public CacheManager createCacheManager( Application application ) throws CacheCreationException {
    CacheManager cacheManager = new CacheManager();
    Jackson2ObjectPersisterFactory jacksonObjectPersisterFactory = new Jackson2ObjectPersisterFactory( application );
    cacheManager.addPersister( jacksonObjectPersisterFactory );
    return cacheManager;
  }

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
  }
}