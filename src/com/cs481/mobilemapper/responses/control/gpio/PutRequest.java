package com.cs481.mobilemapper.responses.control.gpio;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import android.util.Log;

import com.cs481.mobilemapper.CommandCenter;
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
		URL url = new URL(String.format("http://%s/api/control/gpio/", routerip));
		ObjectMapper mapper = new ObjectMapper();
		String req = mapper.writeValueAsString(data.getData());
		String parameters="data="+URLEncoder.encode(req, "UTF-8");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("PUT");
		//not using the .setRequestProperty to the length, but this, solves the problem that i've mentioned
		conn.setFixedLengthStreamingMode(parameters.getBytes().length);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		PrintWriter out = new PrintWriter(conn.getOutputStream());
		out.print(parameters);
		out.close();

		String response = "";
		Scanner inStream = new Scanner(conn.getInputStream());

		while (inStream.hasNextLine()) {
		    response += (inStream.nextLine());
		}
		inStream.close();
		Log.i(CommandCenter.TAG, "Response was "+response);
		return null;
		
		
		/*
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
		    new MediaType("application","x-www-form-urlencoded", parameterMap));

		
		//convert to string.
		ObjectMapper mapper = new ObjectMapper();
		
		String req = mapper.writeValueAsString(data);
		Log.i(CommandCenter.TAG, req);
		//req = Utility.convertToDataSegment(req);
		//req = URLEncoder.encode(req, "US-ASCII");
		

		
		//MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		//map.add("LED_POWER", "false");     
		//map.add("LED_WIFI_RED", "false");  
		
		//HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(propertyMap, requestHeaders);

		 List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		 messageConverters.add(new MappingJackson2HttpMessageConverter());   
		 messageConverters.add(new FormHttpMessageConverter());
		 rt.setMessageConverters(messageConverters);
		
		
		//Map<String,String> propertyMap = mapper.convertValue(data, Map.class);

		//HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(propertyMap, requestHeaders);
		//HttpEntity
		ResponseEntity<GPIO> r = rt.exchange(url, HttpMethod.PUT, request, GPIO.class);
		return r.getBody();*/
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
