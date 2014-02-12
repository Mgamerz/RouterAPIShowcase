
package com.cs481.mobilemapper.responses.status.lan;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Info{
	@JsonProperty("iface")
   	private String iface;
	
	@JsonProperty("link_artype")
   	private String link_arptype;
	
	@JsonProperty("link_type")
   	private String link_type;
	
	@JsonProperty("state")
   	private String state;
	
	@JsonProperty("type")
   	private String type;
	
	@JsonProperty("uid")
   	private String uid;

 	public String getIface(){
		return this.iface;
	}
	public void setIface(String iface){
		this.iface = iface;
	}
 	public String getLink_arptype(){
		return this.link_arptype;
	}
	public void setLink_arptype(String link_arptype){
		this.link_arptype = link_arptype;
	}
 	public String getLink_type(){
		return this.link_type;
	}
	public void setLink_type(String link_type){
		this.link_type = link_type;
	}
 	public String getState(){
		return this.state;
	}
	public void setState(String state){
		this.state = state;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
 	public String getUid(){
		return this.uid;
	}
	public void setUid(String uid){
		this.uid = uid;
	}
}
