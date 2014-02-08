package com.cs481.mobilemapper.responses.control.gpio;

import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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
        String url = String.format("http://%s/api/control/gpio", routerip);

		final String CODEPAGE = "UTF-8";
		HttpPut put = new HttpPut(url);
		put.setHeader("Content-Type", "application/x-www-form-urlencoded");
		
		ObjectMapper mapper = new ObjectMapper();
		String req = mapper.writeValueAsString(data.getData());
		
		put.setEntity(new StringEntity("data="+req, CODEPAGE));
		HttpResponse resp = null;

		DefaultHttpClient client = new DefaultHttpClient();
		Credentials defaultcreds = new UsernamePasswordCredentials("admin",
				password);

		client.getCredentialsProvider().setCredentials(
				new AuthScope(routerip, 80, AuthScope.ANY_REALM), defaultcreds);
		
		
		resp = client.execute(put);
		HttpEntity entity = resp.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");
		Log.i(CommandCenter.TAG, responseString);
		GPIO gpio = mapper.readValue(responseString, GPIO.class);
		return gpio;
		
		
		
        /*AndroidHttpClient httpClient = AndroidHttpClient.newInstance("MobileCommandCenterApp");

        URL urlObj = new URL(String.format("http://%s/api/control/gpio", routerip));
        HttpHost host = new HttpHost(urlObj.getHost(), urlObj.getPort(), urlObj.getProtocol());
        AuthScope scope = new AuthScope(urlObj.getHost(), urlObj.getPort());
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("admin", password);

        CredentialsProvider cp = new BasicCredentialsProvider();
        cp.setCredentials(scope, creds);
        HttpContext credContext = new BasicHttpContext();
        credContext.setAttribute(ClientContext.CREDS_PROVIDER, cp);

        HttpPut job = new HttpPut(urlObj);
        HttpResponse response = httpClient.execute(host,job,credContext);
        StatusLine status = response.getStatusLine();
        Log.d(TestActivity.TEST_TAG, status.toString());
        httpClient.close();*/
		/*
		Authenticator.setDefault(new Authenticator() {
		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication("admin", password.toCharArray());
		    }
		});

		/*
		URL url = new URL(String.format("http://%s/api/control/gpio/", routerip));
		ObjectMapper mapper = new ObjectMapper();
		String req = mapper.writeValueAsString(data.getData());
		String parameters="data="+URLEncoder.encode(req, "UTF-8");
		Log.i(CommandCenter.TAG, parameters);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("PUT");
		//not using the .setRequestProperty to the length, but this, solves the problem that i've mentioned
		conn.setFixedLengthStreamingMode(parameters.getBytes().length);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());

		// what should I write here to output stream to post params to server ?
		outputStream.writeBytes(parameters);
		outputStream.flush();
		outputStream.close();

		/*PrintWriter out = new PrintWriter(conn.getOutputStream());
		out.print(parameters);
		out.close();

		String response = "";
		Scanner inStream = new Scanner(conn.getInputStream());

		while (inStream.hasNextLine()) {
		    response += (inStream.nextLine());
		}
		inStream.close();
		Log.i(CommandCenter.TAG, "Response was "+response);
		GPIO gpio = mapper.readValue(response, GPIO.class);
		return gpio; */
		
		
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
		return "update_gpio@"+System.currentTimeMillis();
	}
}
