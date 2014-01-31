package com.cs481.mobilemapper.responses.control.gpio;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class PutRequest extends SpringAndroidSpiceRequest<Void> {

	private String routerip;
	private GPIO data;

	public PutRequest(String routerip, GPIO data) {
		super(Void.class);
		this.routerip = routerip;
		this.data = data;
	}

	@Override
	public Void loadDataFromNetwork() throws Exception {

		String url = String.format("http://%s/api/control/gpio", routerip);
		RestTemplate rt = getRestTemplate();
		DefaultHttpClient client = new DefaultHttpClient();
		Credentials defaultcreds = new UsernamePasswordCredentials("admin",
				"routerpass");

		client.getCredentialsProvider().setCredentials(
				new AuthScope(routerip, 80, AuthScope.ANY_REALM), defaultcreds);
		// The HttpComponentsClientHttpRequestFactory uses the
		// org.apache.http package to make network requests
		rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
		//rt.exchange(url, HttpMethod.PUT, data, GPIO.class);
		rt.put(url, data);
		return null;
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
