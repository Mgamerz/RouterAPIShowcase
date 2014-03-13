
package com.cs481.commandcenter.responses.status.wlan;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Events{
   	private String associate;
   	private String deauthenticated;
   	private String disassociate;
   	private String mac_filter_allow;
   	private String mac_filter_deny;
   	private String timeout;

 	public String getAssociate(){
		return this.associate;
	}
	public void setAssociate(String associate){
		this.associate = associate;
	}
 	public String getDeauthenticated(){
		return this.deauthenticated;
	}
	public void setDeauthenticated(String deauthenticated){
		this.deauthenticated = deauthenticated;
	}
 	public String getDisassociate(){
		return this.disassociate;
	}
	public void setDisassociate(String disassociate){
		this.disassociate = disassociate;
	}
 	public String getMac_filter_allow(){
		return this.mac_filter_allow;
	}
	public void setMac_filter_allow(String mac_filter_allow){
		this.mac_filter_allow = mac_filter_allow;
	}
 	public String getMac_filter_deny(){
		return this.mac_filter_deny;
	}
	public void setMac_filter_deny(String mac_filter_deny){
		this.mac_filter_deny = mac_filter_deny;
	}
 	public String getTimeout(){
		return this.timeout;
	}
	public void setTimeout(String timeout){
		this.timeout = timeout;
	}
}
