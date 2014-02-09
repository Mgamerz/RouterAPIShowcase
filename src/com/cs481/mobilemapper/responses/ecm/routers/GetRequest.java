package com.cs481.mobilemapper.responses.ecm.routers;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.cs481.mobilemapper.AuthInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

/**
 * This is a special request that does not use the default ConnectionInfo values. 
 * The login to ECM is not part of our router so it does not adhere to our routers basic PUT/GET structure.
 * @author Mgamerz
 *
 */
public class GetRequest extends SpringAndroidSpiceRequest<Routers> {

	private AuthInfo authInfo;

	public GetRequest(AuthInfo authInfo) {
		super(Routers.class);
		this.authInfo = authInfo;
	}

	@Override
	public Routers loadDataFromNetwork() throws Exception {
		//Prepare connection
		
		DefaultHttpClient client = new DefaultHttpClient();
		Credentials defaultcreds = new UsernamePasswordCredentials(authInfo.getUsername(), authInfo.getPassword());
		
		//set auth type
		String url = String.format("https://cradlepointecm.com/api/v1/routers");
		AuthScope auth = new AuthScope("cradlepointecm.com",443);
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
