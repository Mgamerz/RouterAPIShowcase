package com.cs481.mobilemapper.responses.control.led;

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

/**
 * This put request is used to reset all LEDs on the router to their default state.
 * @author Mgamerz
 *
 */
public class PutRequest extends SpringAndroidSpiceRequest<LED> {

	private String routerip, password;

	public PutRequest(String routerip, String password) {
		super(LED.class);
		this.routerip = routerip;
		this.password = password;
	}

	@Override
	public LED loadDataFromNetwork() throws Exception {
        String url = String.format("http://%s/api/control/led", routerip);

		final String CODEPAGE = "UTF-8";
		HttpPut put = new HttpPut(url);
		put.setHeader("Content-Type", "application/x-www-form-urlencoded");	
		
        LED led = new LED();
        led.setData(new com.cs481.mobilemapper.responses.control.led.Data());
        led.getData().setReset_leds(true);
		
		ObjectMapper mapper = new ObjectMapper();
		String req = mapper.writeValueAsString(led.getData());
		
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
		LED reset = mapper.readValue(responseString, LED.class);
		return reset;
	}

	/**
	 * This method generates a unique cache key for this request. In this case
	 * our cache key depends just on the keyword.
	 * 
	 * @return
	 */
	public String createCacheKey() {
		return "led_reset";
	}
}
