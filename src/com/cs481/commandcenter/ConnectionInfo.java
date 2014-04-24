package com.cs481.commandcenter;

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * This class contains a bundle for connection related information, such as the
 * access URL and the HTTP Client that is used for authenticating to the
 * service.
 * 
 * @author Mike Perez
 * 
 */
public class ConnectionInfo {
	private DefaultHttpClient client;
	private String accessUrl;

	/**
	 * Gets the client this connection info is storing.
	 * @return Client object this connection info is storing for use.
	 */
	public DefaultHttpClient getClient() {
		return client;
	}

	/**
	 * Sets the client HTTP object that is used for connecting.
	 * @param client Client object to set to this ConnectionInfo object
	 */
	public void setClient(DefaultHttpClient client) {
		this.client = client;
	}

	/**
	 * Returns the URL that will be connected to.
	 * @return URL to be connected to
	 */
	public String getAccessUrl() {
		return accessUrl;
	}

	/**
	 * Sets the URL that will be connected to by this ConnectionInfo object.
	 * @param accessUrl URL to access
	 */
	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}
}
