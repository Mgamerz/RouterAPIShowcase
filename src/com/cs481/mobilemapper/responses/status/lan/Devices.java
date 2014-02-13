
package com.cs481.mobilemapper.responses.status.lan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Devices{
	@JsonProperty("ethernet-lan")
   	private NetworkDevice NetworkDevice;
	
	@JsonProperty("wlan-wireless0")
   	private NetworkDevice wlan_wireless0;

 	public NetworkDevice getNetworkDevice(){
		return this.NetworkDevice;
	}
	public void setNetworkDevice(NetworkDevice NetworkDevice){
		this.NetworkDevice = NetworkDevice;
	}
 	public NetworkDevice getWlan_wireless0(){
		return this.wlan_wireless0;
	}
	public void setWlan_wireless0(NetworkDevice wlan_wireless0){
		this.wlan_wireless0 = wlan_wireless0;
	}
}
