package com.cs481.mobilemapper.responses.status.lan;

import java.util.ArrayList;

public class InterfaceInfo {
	
	
	private ArrayList<InterfaceInfo> devices;
	private InterfaceInfo Interface;

	public ArrayList<InterfaceInfo> getDevices() {
		return this.devices;
	}

	public void setDevices(ArrayList<InterfaceInfo> devices) {
		this.devices = devices;
	}

	public InterfaceInfo getInterface() {
		return this.Interface;
	}

	public void setInterface(InterfaceInfo Interface) {
		this.Interface = Interface;
	}
}
