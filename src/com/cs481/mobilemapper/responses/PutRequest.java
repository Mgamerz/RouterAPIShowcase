package com.cs481.mobilemapper.responses;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
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

/**
 * This is a generic put request that allows you to put any object to the router
 * or ECM.
 * 
 * @author Mgamerz
 * 
 */
public class PutRequest extends SpiceRequest<Response> {

	private AuthInfo authInfo;
	private String suburl;
	private Class clazz;
	private Object data;

	/**
	 * Creates a new PutRequest object. It is executed when spiceManager.execute() is called.
	 * @param data Object to put to the network.
	 * @param authInfo Auth info to authenticate to the router.
	 * @param suburl Suburl to access of where to send the put. For example, "config/wlan" puts to that subtree.
	 * @param classId
	 * @param clazz
	 */
	public PutRequest(Object data, AuthInfo authInfo, String suburl,
			 Class clazz) {
		super(Response.class);
		this.authInfo = authInfo;
		this.suburl = suburl;
		this.data = data;
		this.clazz = clazz;
	}

	@Override
	public Response loadDataFromNetwork() throws Exception {
		String url = suburl; // url to access
		ConnectionInfo ci = Utility.prepareConnection(url, authInfo);
		DefaultHttpClient client = ci.getClient();
		url = ci.getAccessUrl();
		Log.i(CommandCenterActivity.TAG, "Put Request to " + url);
		HttpPut put = new HttpPut(url);
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = Utility.getPutString(data, clazz, mapper);
		Log.w(CommandCenterActivity.TAG, "Putting data to network: " + jsonStr);
		put = Utility.preparePutRequest(authInfo, put, jsonStr);

		HttpResponse resp = client.execute(put);
		HttpEntity entity = resp.getEntity();

		String responseString = EntityUtils.toString(entity, "UTF-8");

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
	 * our cache key depends just on the keyword. This string should not be
	 * localized.
	 * 
	 * @return
	 */
	public String createCacheKey() {
		return "putrequest"+System.currentTimeMillis();
	}
}
