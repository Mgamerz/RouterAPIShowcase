package com.cs481.mobilemapper.responses.control.gpio;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.client.RestTemplate;

import android.util.Log;

import com.cs481.mobilemapper.CommandCenter;
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
		HttpHeaders requestHeaders = new HttpHeaders(); 
		//requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		final Map<String, String> parameterMap = new HashMap<String, String>(4);
		parameterMap.put("charset", "utf-8");
		requestHeaders.setContentType(
		    new MediaType("application","x-www-form-urlencoded", parameterMap));

		
		//convert to string.
		ObjectMapper mapper = new ObjectMapper();
		String req = mapper.writeValueAsString(data);
		req = Utility.convertToDataSegment(req);
		req = URLEncoder.encode(req, "UTF-8");
		req = "data="+req;
		//"Hack" the string to follow the put format. There seems to be no convenient way to do this
		
		Log.i(CommandCenter.TAG, "mapper str: "+req);
		//Log.i(CommandCenter.TAG,"Mapper str as ASCII: "+)
		HttpEntity<?> request = new HttpEntity<Object>(req, requestHeaders); //sends unformatted json string
		
		
		Log.i(CommandCenter.TAG, "Sending request.");
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
