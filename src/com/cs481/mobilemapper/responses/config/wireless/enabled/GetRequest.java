package com.cs481.mobilemapper.responses.config.wireless.enabled;

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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class GetRequest extends SpringAndroidSpiceRequest<WlanConfig> {

	private AuthInfo authInfo;

	public GetRequest(AuthInfo authInfo) {
		super(WlanConfig.class);
		this.authInfo = authInfo;
	}

	@Override
	public WlanConfig loadDataFromNetwork() throws Exception {
		// Prepare connection
		String url = "config/wlan"; // url to access
		ConnectionInfo ci = Utility.prepareConnection(url, authInfo);
		DefaultHttpClient client = ci.getClient();
		url = ci.getAccessUrl();
		Log.i(CommandCenterActivity.TAG, "CONFIG WLAN GET to " + url);
		HttpGet get = new HttpGet(url);
		HttpResponse resp = client.execute(get); // execute the call on the
													// network.
		HttpEntity entity = resp.getEntity(); // get the body of the response
												// from the `server.

		String responseString = EntityUtils.toString(entity, "UTF-8"); // format
																		// into
																		// a
																		// string
																		// we
																		// can
																		// read.
		ObjectMapper mapper = new ObjectMapper();
		JsonNode tree = mapper.readTree(responseString);
		JsonNode datatree = tree.get("data");
		Log.w(CommandCenterActivity.TAG, datatree.toString());
		
		JsonNode radiotree = datatree.get("radio");
		if (radiotree.isArray()) {
		    for (final JsonNode radioNode : radiotree) {
		        JsonNode enabled = radioNode.get("enabled");
		    	Log.w(CommandCenterActivity.TAG, enabled.toString());
		    }
		}
		/*
		 * ObjectMapper mapper = new ObjectMapper(); if (authInfo.isEcm()) {
		 * responseString = Utility.normalizeECM(mapper, responseString); }
		 * Log.i(CommandCenterActivity.TAG, responseString); WlanConfig wlan =
		 * mapper.readValue(responseString, WlanConfig.class); return wlan;
		 */
		return null;
	}

	/**
	 * This method generates a unique cache key for this request. In this case
	 * our cache key depends just on the keyword.
	 * 
	 * @return
	 */
	public String createCacheKey() {
		return "config_wlan";
	}
}
