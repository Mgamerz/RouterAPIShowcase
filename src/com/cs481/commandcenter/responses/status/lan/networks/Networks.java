package com.cs481.commandcenter.responses.status.lan.networks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Networks {
	@JsonProperty("guestlan")
	private InterfaceInfo guestlan;

	@JsonProperty("primarylan")
	private InterfaceInfo primarylan;

	public InterfaceInfo getGuestlan() {
		return this.guestlan;
	}

	public void setGuestlan(InterfaceInfo guestlan) {
		this.guestlan = guestlan;
	}

	public InterfaceInfo getPrimarylan() {
		return this.primarylan;
	}

	public void setPrimarylan(InterfaceInfo primarylan) {
		this.primarylan = primarylan;
	}
}
