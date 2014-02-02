package com.cs481.mobilemapper.responses.control.gpio;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.cs481.mobilemapper.Utility;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class PutRequest extends SpringAndroidSpiceRequest<GPIO> {

	private String routerip, password;
	private GPIO data;

	public PutRequest(String routerip, String password, GPIO data) {
		super(GPIO.class);
		this.routerip = routerip;
		this.data = data;
		this.password = password;
	}

	@Override
	public GPIO loadDataFromNetwork() throws Exception {

		String url = String.format("http://%s/api/control/gpio", routerip);
		RestTemplate rt = getRestTemplate();
		DefaultHttpClient client = new DefaultHttpClient();
		Credentials defaultcreds = new UsernamePasswordCredentials("admin",
				password);

		client.getCredentialsProvider().setCredentials(
				new AuthScope(routerip, 80, AuthScope.ANY_REALM), defaultcreds);
		// The HttpComponentsClientHttpRequestFactory uses the
		// org.apache.http package to make network requests
		rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
		//rt.exchange(url, HttpMethod.PUT, data, GPIO.class);
		HttpHeaders requestHeaders = new HttpHeaders(); 
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<GPIO> request = new HttpEntity<GPIO>(data, requestHeaders);
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		MappingJackson2HttpMessageConverter map = new MappingJackson2HttpMessageConverter();
		ArrayList<MediaType> smt = new ArrayList<MediaType>();
		smt.add(MediaType.APPLICATION_FORM_URLENCODED);
		map.setSupportedMediaTypes(smt);
		messageConverters.add(map);
		//messageConverters.add(new FormHttpMessageConverter());
		//messageConverters.add(new FormHttpMessageConverter());
		//messageConverters.add(new StringHttpMessageConverter());
		rt.setMessageConverters(messageConverters);
		
		
		
		
		
		
		ResponseEntity<GPIO> r = rt.exchange(url, HttpMethod.PUT, request, GPIO.class);
		return r.getBody();
	}

	/**
	 * This method generates a unique cache key for this request. In this case
	 * our cache key depends just on the keyword.
	 * 
	 * @return
	 */
	public String createCacheKey() {
		return "update_gpio";
	}
}
