
package com.cs481.mobilemapper.responses.config.wwan;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class defines a WAN profile on the router. Putting this to the router will define a new WAP that
 * this router will attempt to connect to when it's seen in the survey list.
 * @author Mgamerz
 *
 */
public class WANProfile{
	@JsonProperty("authmode")
   	private String authmode;
	
	@JsonProperty("bssid")
   	private String bssid;
	
	@JsonProperty("enabled")
   	private boolean enabled;
	
	@JsonProperty("ssid")
   	private String ssid;
	
	@JsonProperty("uid")
   	private String uid;
	
	@JsonProperty("wpacipher")
   	private String wpacipher;
	
	@JsonProperty("wpapsk")
   	private String wpapsk;

 	public String getAuthmode(){
		return this.authmode;
	}
	public void setAuthmode(String authmode){
		this.authmode = authmode;
	}
 	public String getBssid(){
		return this.bssid;
	}
	public void setBssid(String bssid){
		this.bssid = bssid;
	}
 	public boolean getEnabled(){
		return this.enabled;
	}
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
 	public String getSsid(){
		return this.ssid;
	}
	public void setSsid(String ssid){
		this.ssid = ssid;
	}
 	public String getUid(){
		return this.uid;
	}
	public void setUid(String uid){
		this.uid = uid;
	}
 	public String getWpacipher(){
		return this.wpacipher;
	}
	public void setWpacipher(String wpacipher){
		this.wpacipher = wpacipher;
	}
 	public String getWpapsk(){
		return this.wpapsk;
	}
	public void setWpapsk(String wpapsk){
		this.wpapsk = wpapsk;
	}
}
