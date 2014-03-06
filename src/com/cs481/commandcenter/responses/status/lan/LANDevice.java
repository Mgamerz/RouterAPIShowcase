package com.cs481.commandcenter.responses.status.lan;

import com.cs481.commandcenter.responses.status.lan.networks.NetworkInterfaceDevice;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class is under /status/lan/devices/DEVICENAME - it only shows some of
 * the network information. The other part is in the NetworkInterface part
 * 
 * @author Mgamerz
 * 
 */
public class LANDevice {
	@JsonProperty("info")
	private NetworkInterfaceDevice info;

	@JsonProperty("stats")
	private Stats stats;

	@JsonProperty("status")
	private Status status;

	public NetworkInterfaceDevice getInfo() {
		return this.info;
	}

	public void setInfo(NetworkInterfaceDevice info) {
		this.info = info;
	}

	public Stats getStats() {
		return this.stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
