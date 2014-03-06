
package com.cs481.commandcenter.responses.status.lan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Devices{
	@JsonProperty("ethernet-lan")
   	private LANDevice NetworkDevice;
	
	@JsonProperty("wlan-wireless0")
   	private LANDevice wlan_wireless0;

 	public LANDevice getNetworkDevice(){
		return this.NetworkDevice;
	}
	public void setNetworkDevice(LANDevice NetworkDevice){
		this.NetworkDevice = NetworkDevice;
	}
 	public LANDevice getWlan_wireless0(){
		return this.wlan_wireless0;
	}
	public void setWlan_wireless0(LANDevice wlan_wireless0){
		this.wlan_wireless0 = wlan_wireless0;
	}
}
