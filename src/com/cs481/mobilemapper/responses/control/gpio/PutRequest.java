package com.cs481.mobilemapper.responses.control.gpio;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.cs481.mobilemapper.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
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

		String url = String.format("http://%s/api/control/gpio/", routerip);
		RestTemplate rt = getRestTemplate();
		DefaultHttpClient client = new DefaultHttpClient();
		Credentials defaultcreds = new UsernamePasswordCredentials("admin",
				password);

		client.getCredentialsProvider().setCredentials(
				new AuthScope(routerip, 80, AuthScope.ANY_REALM), defaultcreds);
		// The HttpComponentsClientHttpRequestFactory uses the
		// org.apache.http package to make network requests
		rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
		HttpHeaders requestHeaders = new HttpHeaders(); 
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		/*final Map<String, String> parameterMap = new HashMap<String, String>(4);
		parameterMap.put("charset", "utf-8");
		requestHeaders.setContentType(
		    new MediaType("application","x-www-form-urlencoded", parameterMap));*/

		
		//convert to string.
		ObjectMapper mapper = new ObjectMapper();
		String req = mapper.writeValueAsString(data);
		req = Utility.convertToDataSegment(req);
		//req = URLEncoder.encode(req, "US-ASCII");
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("LED_POWER", "false");     
		map.add("LED_WIFI_RED", "false");  
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, requestHeaders);

		 List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		 messageConverters.add(new MappingJackson2HttpMessageConverter());   
		 messageConverters.add(new FormHttpMessageConverter());
		 rt.setMessageConverters(messageConverters);
		
		
		
		//req = "data="+req;
		//StringEntity sreq = new StringEntity("data=false");
		//"Hack" the string to follow the put format. There seems to be no convenient way to do this
		
		//Log.i(CommandCenter.TAG, "mapper str: "+sreq);
		//Log.i(CommandCenter.TAG,"Mapper str as ASCII: "+)
		//HttpEntity<?> request = new HttpEntity<Object>(sreq, requestHeaders); //sends unformatted json string
		
		
		//Log.i(CommandCenter.TAG, "Sending request with body:");
		//Log.i(CommandCenter.TAG, (String) request.getBody());

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
