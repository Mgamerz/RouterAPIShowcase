package com.cs481.mobilemapper.responses;

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

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.CommandCenterActivity;
import com.cs481.mobilemapper.ConnectionInfo;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.responses.control.gpio.GPIO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

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
	private int classId;
	private Class clazz;
	private Object data;

	public PutRequest(Object data, AuthInfo authInfo, String suburl,
			int classId, Class clazz) {
		super(Response.class);
		this.authInfo = authInfo;
		this.suburl = suburl;
		this.classId = classId;
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
		String jsonStr = Utility.getPutString(data, clazz, classId, mapper);
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
		return "putrequest";
	}
}
