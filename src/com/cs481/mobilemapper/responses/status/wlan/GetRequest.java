package com.cs481.mobilemapper.responses.status.wlan;

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

public class GetRequest extends SpringAndroidSpiceRequest<Wlan> {

	private AuthInfo authInfo;

	public GetRequest(AuthInfo authInfo) {
		super(Wlan.class);
		this.authInfo = authInfo;
	}

	@Override
	public Wlan loadDataFromNetwork() throws Exception {
		// Prepare connection
		String url = "status/wlan"; // url to access
		ConnectionInfo ci = Utility.prepareConnection(url, authInfo);
		DefaultHttpClient client = ci.getClient();
		url = ci.getAccessUrl();
		Log.i(CommandCenterActivity.TAG, "WLAN GET to " + url);
		HttpGet get = new HttpGet(url);
		HttpResponse resp = client.execute(get); // execute the call on the
													// network.
		HttpEntity entity = resp.getEntity(); // get the body of the response
												// from the server.

		String responseString = EntityUtils.toString(entity, "UTF-8"); // format
																		// into
																		// a
																		// string
																		// we
																		// can
																		// read.

		ObjectMapper mapper = new ObjectMapper();
		if (authInfo.isEcm()) {
			responseString = Utility.normalizeECM(mapper, responseString);
		}
		Wlan wlan = mapper.readValue(responseString, Wlan.class);
		return wlan;
	}

	/**
	 * This method generates a unique cache key for this request. In this case
	 * our cache key depends just on the keyword.
	 * 
	 * @return
	 */
	public String createCacheKey() {
		return "wlan_status";
	}
}
