
package com.cs481.mobilemapper.responses.status.lan;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Client{
	@JsonProperty("ip_address")
   	private String ip_address;
	
	@JsonProperty("mac")
   	private String mac;

 	public String getIp_address(){
		return this.ip_address;
	}
	public void setIp_address(String ip_address){
		this.ip_address = ip_address;
	}
 	public String getMac(){
		return this.mac;
	}
	public void setMac(String mac){
		this.mac = mac;
	}
}
