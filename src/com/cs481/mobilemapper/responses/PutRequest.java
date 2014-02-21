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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

/**
 * This put request is used to reset all Responses on the router to their default state.
 * @author Mgamerz
 *
 */
public class PutRequest extends SpringAndroidSpiceRequest<Response> {

	private AuthInfo authInfo;
	private String suburl;
	private int classId;
	private Object data;

	public PutRequest(Object data, AuthInfo authInfo, String suburl, int classId) {
		super(Response.class);
		this.authInfo = authInfo;
		this.suburl = suburl;
		this.classId = classId;
		this.data = data;
	}

	@Override
	public Response loadDataFromNetwork() throws Exception {
		String url = suburl; //url to access
		ConnectionInfo ci = Utility.prepareConnection(url, authInfo);
		DefaultHttpClient client = ci.getClient();
		url = ci.getAccessUrl();
		Log.i(CommandCenterActivity.TAG, "Put Request to "+url);
		HttpPut put = new HttpPut(url);
		
        //Response Response = new Response();
        //Response.setData(new com.cs481.mobilemapper.responses.control.Response.Data());
        //Response.getData().setReset_Responses(true); //makes the Responses reset when the router processes this data
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = Utility.getPutString(data, classId, authInfo.isEcm(), mapper);
		
		put = Utility.preparePutRequest(authInfo, put, jsonStr);
		
		HttpResponse resp = client.execute(put);
		HttpEntity entity = resp.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");
		if (authInfo.isEcm()){
			Log.i(CommandCenterActivity.TAG, "Normalizing ECM response");
			responseString = Utility.normalizeECM(mapper, responseString);
		}
		
		Log.i(CommandCenterActivity.TAG, responseString);
		Response respResponse = mapper.readValue(responseString, Response.class);
		return respResponse;
		
		
		/*
        String url = String.format("http://%s/api/control/Response", routerip);

		final String CODEPAGE = "UTF-8";
		HttpPut put = new HttpPut(url);
		put.setHeader("Content-Type", "application/x-www-form-urlencoded");	
		
        Response Response = new Response();
        Response.setData(new com.cs481.mobilemapper.responses.control.Response.Data());
        Response.getData().setReset_Responses(true);
		
		ObjectMapper mapper = new ObjectMapper();
		String req = mapper.writeValueAsString(Response.getData());
		
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
		Response reset = mapper.readValue(responseString, Response.class);
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
		return "Response_reset";
	}
}
