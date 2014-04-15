package com.cs481.commandcenter.responses;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
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
 * This is a generic get request that allows you to get any object from the router
 * or ECM.
 * @author Mike Perez
 */

public class GetRequest extends SpiceRequest<Response> {

	private AuthInfo authInfo;
	private String suburl;
	private Class clazz;
	private String requestid;
	private Context context;

	/**
	 * Creates a new get request.
	 * @param context Context to use for performing the network operation.
	 * @param authInfo Info to use for connection parameters
	 * @param url subtree of the router to access
	 * @param clazz Class (.class) of the response object you want to request
	 * @param requestid ID to use for pending requests. If the ID matches an existing request the request will be ignored as one is already in progress.
	 */
	public GetRequest(Context context, AuthInfo authInfo, String url, Class clazz, String requestid) {
		super(Response.class);
		this.suburl = url;
		this.authInfo = authInfo;
		this.clazz = clazz;
		this.requestid = requestid;
		this.context = context;
	}

	@Override
	public Response loadDataFromNetwork() throws Exception {
		// Prepare connection
		String url = suburl; // url to access
		ConnectionInfo ci = Utility.prepareConnection(context, suburl, authInfo);
		DefaultHttpClient client = ci.getClient();
		url = ci.getAccessUrl();
		Log.i(CommandCenterActivity.TAG, "Get Request to " + url);
		HttpGet get = new HttpGet(url);
		HttpResponse resp = client.execute(get); // execute the call on the
													// network.
		HttpEntity entity = resp.getEntity(); // get the body of the response
												// from the `server.

		String responseString = EntityUtils.toString(entity, "UTF-8"); // format
																		// to
																		// string
																		// thats
																		// readable
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
	 * @return the requestid passed as the parameter to the constructor.
	 */
	public String createCacheKey() {
		return requestid;
	}
}
