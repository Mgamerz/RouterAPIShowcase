package com.cs481.mobilemapper.responses.status.lan;

import com.cs481.mobilemapper.responses.status.lan.networks.InterfaceInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class is under /status/lan/devices/networklan - it only shows some of
 * the network information. The other part is in the NetworkInterface part
 * 
 * @author Mgamerz
 * 
 */
public class NetworkInterface {
	@JsonProperty("info")
	private InterfaceInfo info;

	@JsonProperty("stats")
	private Stats stats;

	@JsonProperty("status")
	private Status status;

	public InterfaceInfo getInfo() {
		return this.info;
	}

	public void setInfo(InterfaceInfo info) {
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
