package com.cs481.commandcenter.responses.ecm.routers;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.request.SpiceRequest;

/**
 * This is a special request that does not use the default ConnectionInfo values. 
 * The login to ECM is not part of our router so it does not adhere to our routers basic PUT/GET structure.
 * @author Mgamerz
 *
 */
public class GetRequest extends SpiceRequest<Routers> {

	private AuthInfo authInfo;
	private Context context;

	/**
	 * Creates a special get request for talking to ECM.
	 * @param authInfo Authinfo for talking to ECM.
	 * @param context Context to use for the connection.
	 */
	public GetRequest(AuthInfo authInfo, Context context) {
		super(Routers.class);
		this.authInfo = authInfo;
		this.context = context;
	}

	@Override
	public Routers loadDataFromNetwork() throws Exception {
		//Prepare connection
		
		DefaultHttpClient client = new DefaultHttpClient();
		Credentials defaultcreds = new UsernamePasswordCredentials(authInfo.getUsername(), authInfo.getPassword());
		
		SharedPreferences advancedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		String baseurl = advancedPrefs.getString(context.getResources().getString(R.string.prefskey_ecmapiurl), context.getResources().getString(R.string.ecmapi_base_url));


		//set auth type
		String url = String.format("%srouters", baseurl);
		Log.i(CommandCenterActivity.TAG, "ECM Get Request to " + url);

		String hostRealm = Utility.getDomainName(url);
		AuthScope auth = new AuthScope(hostRealm, 443);
		client.getCredentialsProvider().setCredentials(auth, defaultcreds);


		HttpGet get = new HttpGet(url);
		HttpResponse resp = client.execute(get); //execute the call on the network.
		HttpEntity entity = resp.getEntity(); //get the body of the response from the server.
		
		String responseString = EntityUtils.toString(entity, "UTF-8"); //format into a string we can read.
		
		ObjectMapper mapper = new ObjectMapper();
		Routers routers =  mapper.readValue(responseString, Routers.class);
		return routers;
	}

	/**
	 * This method generates a unique cache key for this request. In this case
	 * our cache key depends just on the keyword.
	 * 
	 * @return
	 */
	public String createCacheKey() {
		return "ecm_get_routers";
	}
}
