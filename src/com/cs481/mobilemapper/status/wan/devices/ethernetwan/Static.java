
package com.cs481.mobilemapper.status.wan.devices.ethernetwan;

import java.util.List;

public class Static{
   	private List dns;
   	private String gateway;
   	private String ip_address;
   	private String netmask;

 	public List getDns(){
		return this.dns;
	}
	public void setDns(List dns){
		this.dns = dns;
	}
 	public String getGateway(){
		return this.gateway;
	}
	public void setGateway(String gateway){
		this.gateway = gateway;
	}
 	public String getIp_address(){
		return this.ip_address;
	}
	public void setIp_address(String ip_address){
		this.ip_address = ip_address;
	}
 	public String getNetmask(){
		return this.netmask;
	}
	public void setNetmask(String netmask){
		this.netmask = netmask;
	}
}
