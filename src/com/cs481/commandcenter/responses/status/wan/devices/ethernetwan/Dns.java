
package com.cs481.commandcenter.responses.status.wan.devices.ethernetwan;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dns{
   	private String ip_address;

 	public String getIp_address(){
		return this.ip_address;
	}
	public void setIp_address(String ip_address){
		this.ip_address = ip_address;
	}
}
