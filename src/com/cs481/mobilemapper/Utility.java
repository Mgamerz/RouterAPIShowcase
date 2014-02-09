package com.cs481.mobilemapper;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * The utility class holds methods that are accessed by many classes.
 * 
 * @author Mgamerz
 * 
 */
public class Utility {

	public static String convertToDataSegment(String str) {
		str = str.substring(0, str.length() - 1); // remove last }
		str = str.substring(1, str.length()); // remove first {
		str = str.replaceFirst("\"data\":", "");
		return str;
	}

	public static DefaultHttpClient prepareConnection(String url, AuthInfo authInfo) {
		DefaultHttpClient client = new DefaultHttpClient();
		Credentials defaultcreds = new UsernamePasswordCredentials(authInfo.getUsername(), authInfo.getPassword());
		AuthScope auth;
		if (authInfo.isEcm()) {
			auth = new AuthScope("cradlepointecm.com",443);
		} else {
			auth = new AuthScope(authInfo.getRouterip(), 80,
					AuthScope.ANY_REALM);
		}
		client.getCredentialsProvider().setCredentials(auth, defaultcreds);
		return client;
	}
}
