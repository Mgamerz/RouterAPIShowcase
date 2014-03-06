package com.cs481.commandcenter.responses.status.lan.networks;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InterfaceInfo {
	
	@JsonProperty("devices")
	private ArrayList<NetworkInterfaceDevice> devices;
	
	@JsonProperty("info")
	private Info info;

	public ArrayList<NetworkInterfaceDevice> getDevices() {
		return devices;
	}

	public void setDevices(ArrayList<NetworkInterfaceDevice> devices) {
		this.devices = devices;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}
	
	
}
