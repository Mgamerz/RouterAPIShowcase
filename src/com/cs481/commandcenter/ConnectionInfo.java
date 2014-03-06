package com.cs481.commandcenter;

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * This class contains a bundle for connection related information, such as the
 * access URL and the HTTP Client that is used for authenticating to the
 * service.
 * 
 * @author Mgamerz
 * 
 */
public class ConnectionInfo {
	private DefaultHttpClient client;
	private String accessUrl;

	public DefaultHttpClient getClient() {
		return client;
	}

	public void setClient(DefaultHttpClient client) {
		this.client = client;
	}

	public String getAccessUrl() {
		return accessUrl;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}
}
