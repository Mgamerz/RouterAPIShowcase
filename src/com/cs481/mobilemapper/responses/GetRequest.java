package com.cs481.mobilemapper.responses;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.ConnectionInfo;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.activities.CommandCenterActivity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class GetRequest extends SpiceRequest<Response> {

	private AuthInfo authInfo;
	private String suburl;
	private Class clazz;
	private String requestid;

	public GetRequest(AuthInfo authInfo, String url, Class clazz, String requestid) {
		super(Response.class);
		this.suburl = url;
		this.authInfo = authInfo;
		this.clazz = clazz;
		this.requestid = requestid;
	}

	@Override
	public Response loadDataFromNetwork() throws Exception {
		// Prepare connection
		String url = suburl; // url to access
		ConnectionInfo ci = Utility.prepareConnection(suburl, authInfo);
		DefaultHttpClient client = ci.getClient();
		url = ci.getAccessUrl();
		Log.i(CommandCenterActivity.TAG, "Get Request to " + url);
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

		if (authInfo.isEcm()) {
			responseString = Utility.normalizeECM(mapper, responseString);
		}
		JsonNode tree = mapper.readTree(responseString);
		RootElement rr = mapper.readValue(responseString, RootElement.class);
		tree = tree.get("data");
		Object data = mapper.readValue(tree.toString(), clazz);
		return new Response(rr, data);

	}

	/**
	 * This method generates a unique cache key for this request. In this case
	 * our cache key depends just on the keyword.
	 * 
	 * @return
	 */
	public String createCacheKey() {
		return requestid;
	}
}
