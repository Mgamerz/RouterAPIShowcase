package com.cs481.mobilemapper;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

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

	/**
	 * Prepares a connection and makes it transparent to other classes what the type of router they are accessing, be it ECM or local-based.
	 * @param url Suburl of the API to connect to, e.g. status/wlan
	 * @param authInfo Authinfo package defining authorization and router information.
	 * @return ConnectionInfo bundle describing this connection
	 */
	public static ConnectionInfo prepareConnection(String url, AuthInfo authInfo) {
		ConnectionInfo ci = new ConnectionInfo();
		DefaultHttpClient client = new DefaultHttpClient();
		Credentials defaultcreds = new UsernamePasswordCredentials(authInfo.getUsername(), authInfo.getPassword());
		
		//set auth type
		AuthScope auth;
		if (authInfo.isEcm()) {
			ci.setAccessUrl(String.format("https://cradlepointecm.com/api/v1/%s/?id=%s",url,authInfo.getRouterId()));
			auth = new AuthScope("cradlepointecm.com",443);
		} else {
			ci.setAccessUrl(String.format("http://%s/api/%s", authInfo.getRouterip(), url));
			auth = new AuthScope(authInfo.getRouterip(), 80,
					AuthScope.ANY_REALM);
		}
		client.getCredentialsProvider().setCredentials(auth, defaultcreds);
		ci.setClient(client);
		return ci;
	}

	/**
	 * Gets the default gateway this device is connected to
	 * @param context Context to use to get the service (the calling activity)
	 * @return String of the gateway
	 */
	public static String getDefaultGateway(Context context) {
		final WifiManager manager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		final DhcpInfo dhcp = manager.getDhcpInfo();
		// This method is deprecated because we use IPv4 and the new one
		// is IPv6. IPv6 is way more complicated and used in more
		// telecom related things (public facing things). It still works
		// so ignore the deprecation.
		return Formatter.formatIpAddress(dhcp.gateway);
	}
}
