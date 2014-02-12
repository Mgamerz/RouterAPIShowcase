
package com.cs481.mobilemapper.responses.status.lan;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Devices{
	@JsonProperty("ethernet-lan")
   	private Ethernet_lan ethernet_lan;
	
	@JsonProperty("wlan-wireless0")
   	private Wlan_wireless0 wlan_wireless0;

 	public Ethernet_lan getEthernet_lan(){
		return this.ethernet_lan;
	}
	public void setEthernet_lan(Ethernet_lan ethernet_lan){
		this.ethernet_lan = ethernet_lan;
	}
 	public Wlan_wireless0 getWlan_wireless0(){
		return this.wlan_wireless0;
	}
	public void setWlan_wireless0(Wlan_wireless0 wlan_wireless0){
		this.wlan_wireless0 = wlan_wireless0;
	}
}
