package com.cs481.mobilemapper.responses.status.gpio;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.CommandCenterActivity;
import com.cs481.mobilemapper.ConnectionInfo;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.responses.control.gpio.GPIO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class GetRequest extends SpringAndroidSpiceRequest<GPIO> {

	private AuthInfo authInfo;

	public GetRequest(AuthInfo authInfo) {
		super(GPIO.class);
		this.authInfo = authInfo;
	}

	@Override
	public GPIO loadDataFromNetwork() throws Exception {
		//Prepare connection
		String url = "status/gpio"; //url to access
		ConnectionInfo ci = Utility.prepareConnection(url, authInfo);
		DefaultHttpClient client = ci.getClient();
		url = ci.getAccessUrl();
		Log.i(CommandCenterActivity.TAG, "GPIO GET to "+url);
		HttpGet get = new HttpGet(url);
		HttpResponse resp = client.execute(get); //execute the call on the network.
		HttpEntity entity = resp.getEntity(); //get the body of the response from the server.
		
		String responseString = EntityUtils.toString(entity, "UTF-8"); //format into a string we can read.
		
		ObjectMapper mapper = new ObjectMapper();
		GPIO gpio;
		if (authInfo.isEcm()){
			responseString = Utility.normalizeECM(mapper, responseString);
		}
		gpio = mapper.readValue(responseString, GPIO.class);
		return gpio;
	}

	/**
	 * This method generates a unique cache key for this request. In this case
	 * our cache key depends just on the keyword.
	 * 
	 * @return
	 */
	public String createCacheKey() {
		return "GPIO_status";
	}
}
