package com.cs481.mobilemapper.responses.control.led;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.CommandCenterActivity;
import com.cs481.mobilemapper.ConnectionInfo;
import com.cs481.mobilemapper.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.octo.android.robospice.request.SpiceRequest;

/**
 * This put request is used to reset all LEDs on the router to their default state.
 * @author Mgamerz
 *
 */
public class PutRequest extends SpiceRequest<LED> {

	private AuthInfo authInfo;

	public PutRequest(AuthInfo authInfo) {
		super(LED.class);
		this.authInfo = authInfo;
	}

	@Override
	public LED loadDataFromNetwork() throws Exception {
		String url = "control/led"; //url to access
		ConnectionInfo ci = Utility.prepareConnection(url, authInfo);
		DefaultHttpClient client = ci.getClient();
		url = ci.getAccessUrl();
		Log.i(CommandCenterActivity.TAG, "LEDRESET PUT to "+url);
		HttpPut put = new HttpPut(url);
		
        LED led = new LED();
        led.setData(new com.cs481.mobilemapper.responses.control.led.Data());
        led.getData().setReset_leds(true); //makes the LEDs reset when the router processes this data
		
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonStr;
		//I can't really do this part in a utility without heavy use of generics and making the code really ugly.
		if (authInfo.isEcm()){
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			jsonStr = ow.writeValueAsString(led);
			Log.i(CommandCenterActivity.TAG, "Data to post: "+jsonStr);
		} else {
			jsonStr = mapper.writeValueAsString(led.getData());
		}
		
		put = Utility.preparePutRequest(authInfo, put, jsonStr);
		
		HttpResponse resp = client.execute(put);
		HttpEntity entity = resp.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");
		if (authInfo.isEcm()){
			Log.i(CommandCenterActivity.TAG, "Normalizing ECM response");
			responseString = Utility.normalizeECM(mapper, responseString);
		}
		
		Log.i(CommandCenterActivity.TAG, responseString);
		LED respLed = mapper.readValue(responseString, LED.class);
		return respLed;
		
		
		/*
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
		Log.i(CommandCenterActivity.TAG, responseString);
		LED reset = mapper.readValue(responseString, LED.class);
		return reset;
		*/
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
