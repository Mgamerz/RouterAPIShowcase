package com.cs481.commandcenter.responses;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.ConnectionInfo;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.request.SpiceRequest;

/**
 * This is a generic put request that allows you to post any object to the router
 * or ECM.
 * @author Mike Perez
 */

public class PostRequest extends SpiceRequest<Response> {

	private AuthInfo authInfo;
	private String suburl;
	private Class clazz;
	private Object data;
	private Context context;

	/**
	 * Creates a new PostRequest object. It is executed when spiceManager.execute() is called.
	 * 
	 * The listener for this request should expect an integer result, which corresponds to what index it was just posted to.
	 * 
	 * @param data Object to post to the network.
	 * @param authInfo Auth info to authenticate to the router.
	 * @param suburl Suburl to access of where to send the post. For example, "config/wlan" posts to that subtree.
	 * @param clazz Class of object to put
	 */
	public PostRequest(Context context, Object data, AuthInfo authInfo, String suburl,
			 Class clazz) {
		super(Response.class);
		this.authInfo = authInfo;
		this.suburl = suburl;
		this.data = data;
		this.clazz = clazz;
		this.context = context;
	}

	@Override
	public Response loadDataFromNetwork() throws Exception {
		String url = suburl; // url to access
		ConnectionInfo ci = Utility.prepareConnection(context,  url, authInfo);
		DefaultHttpClient client = ci.getClient();
		url = ci.getAccessUrl();
		Log.i(CommandCenterActivity.TAG, "Post Request to " + url);
		HttpPost put = new HttpPost(url);
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = Utility.getPutString(data, clazz, mapper); //works for put as well
		Log.w(CommandCenterActivity.TAG, "Putting data to network: " + jsonStr);
		put = Utility.preparePostRequest(authInfo, put, jsonStr);

		HttpResponse resp = client.execute(put);
		HttpEntity entity = resp.getEntity();

		String responseString = EntityUtils.toString(entity, "UTF-8");

		if (authInfo.isEcm()) {
			responseString = Utility.normalizeECM(mapper, responseString);
		}
		JsonNode tree = mapper.readTree(responseString);
		RootElement rr = mapper.readValue(responseString, RootElement.class);
		tree = tree.get("data");
		
		Integer postIndex = Integer.valueOf(tree.toString());
		
		Log.i(CommandCenterActivity.TAG, "Post result: "+tree);
		//Object data = mapper.readValue(tree.toString(), clazz);
		return new Response(rr, postIndex);
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
