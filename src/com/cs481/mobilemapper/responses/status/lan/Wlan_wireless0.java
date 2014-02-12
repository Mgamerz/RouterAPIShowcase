
package com.cs481.mobilemapper.responses.status.lan;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Wlan_wireless0{
	@JsonProperty("info")
   	private Info info;
	
	@JsonProperty("stats")
   	private Stats stats;
	
	@JsonProperty("status")
   	private Status status;

 	public Info getInfo(){
		return this.info;
	}
	public void setInfo(Info info){
		this.info = info;
	}
 	public Stats getStats(){
		return this.stats;
	}
	public void setStats(Stats stats){
		this.stats = stats;
	}
 	public Status getStatus(){
		return this.status;
	}
	public void setStatus(Status status){
		this.status = status;
	}
}
