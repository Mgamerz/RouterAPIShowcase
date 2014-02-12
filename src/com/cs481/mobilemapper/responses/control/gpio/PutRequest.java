package com.cs481.mobilemapper.responses.control.gpio;

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
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class PutRequest extends SpringAndroidSpiceRequest<GPIO> {

	private AuthInfo authInfo;
	private GPIO data;

	public PutRequest(AuthInfo authInfo, GPIO data) {
		super(GPIO.class);
		this.authInfo = authInfo;
		this.data = data;
	}

	@Override
	public GPIO loadDataFromNetwork() throws Exception {
		String url = "control/gpio"; //url to access
		ConnectionInfo ci = Utility.prepareConnection(url, authInfo);
		DefaultHttpClient client = ci.getClient();
		url = ci.getAccessUrl();
		Log.i(CommandCenterActivity.TAG, "GPIO PUT to "+url);
		HttpPut put = new HttpPut(url);
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonStr;
		//I can't really do this part in a utility without heavy use of generics and making the code really ugly.
		if (authInfo.isEcm()){
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			jsonStr = ow.writeValueAsString(data);
			Log.i(CommandCenterActivity.TAG, "Data to post: "+jsonStr);
		} else {
			jsonStr = mapper.writeValueAsString(data.getData());
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
		GPIO gpio = mapper.readValue(responseString, GPIO.class);
		return gpio;
	}

	/**
	 * This method generates a unique cache key for this request. In this case
	 * our cache key depends just on the request type and the time (so it is always unique).
	 * 
	 * @return
	 */
	public String createCacheKey() {
		return "update_gpio@"+System.currentTimeMillis();
	}
}
