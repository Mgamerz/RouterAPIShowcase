
package com.cs481.mobilemapper.responses.status.wlan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Survey{
	@JsonProperty("authmode")
   	private String authmode;
	
	@JsonProperty("bssid")
   	private String bssid;
	
	@JsonProperty("channel")
   	private Number channel;
	
	@JsonProperty("extcha")
   	private String extcha;
	
	@JsonProperty("mode")
   	private String mode;
	
	@JsonProperty("rssi")
   	private Number rssi;
	
	@JsonProperty("ssid")
   	private String ssid;
	
	@JsonProperty("type")
   	private String type;

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
 	public Number getChannel(){
		return this.channel;
	}
	public void setChannel(Number channel){
		this.channel = channel;
	}
 	public String getExtcha(){
		return this.extcha;
	}
	public void setExtcha(String extcha){
		this.extcha = extcha;
	}
 	public String getMode(){
		return this.mode;
	}
	public void setMode(String mode){
		this.mode = mode;
	}
 	public Number getRssi(){
		return this.rssi;
	}
	public void setRssi(Number rssi){
		this.rssi = rssi;
	}
 	public String getSsid(){
		return this.ssid;
	}
	public void setSsid(String ssid){
		this.ssid = ssid;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
}
