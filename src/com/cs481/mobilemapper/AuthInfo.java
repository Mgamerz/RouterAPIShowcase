package com.cs481.mobilemapper;

/**
 * This class is a convenience bundle for authentication related information.
 * If the ecm flag is set to true, classes that use this object will expect username, password and routerId to not be null.
 * If the ecm flag is set to false, the routerip, username, and password must not be null.
 * 
 * @author Mgamerz
 * 
 */
public class AuthInfo {
	private boolean ecm;
	private String routerip;
	private String username = "admin"; //default
	private String password;
	private String routerId;

	public String getRouterId() {
		return routerId;
	}

	public void setRouterId(String routerId) {
		this.routerId = routerId;
	}

	public boolean isEcm() {
		return ecm;
	}

	public void setEcm(boolean ecm) {
		this.ecm = ecm;
	}

	public String getRouterip() {
		return routerip;
	}

	public void setRouterip(String routerip) {
		this.routerip = routerip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
