package com.cs481.mobilemapper;

/**
 * Profile object is stored in the database and defines a profile
 * 
 * @author Mgamerz
 * 
 */
public class Profile {
	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public AuthInfo getAuthInfo() {
		return authInfo;
	}

	public void setAuthInfo(AuthInfo authInfo) {
		this.authInfo = authInfo;
	}

	private String profileName; // e.g. "Cloud Router"
	private AuthInfo authInfo; // Authentication information:
}
