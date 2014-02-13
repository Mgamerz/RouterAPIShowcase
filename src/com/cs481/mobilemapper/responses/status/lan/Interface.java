package com.cs481.mobilemapper.responses.status.lan;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is in the devices list at /status/networks/DEVICENAME/devices/0...1 2 3 4 etc.
 * @author Mgamerz
 *
 */
public class Interface {
	@JsonProperty("state")
	private String state;
	@JsonProperty("iface")
	private String iface;
	@JsonProperty("uid")
	private String uid;
	@JsonProperty("link_type")
	private String link_type;
	@JsonProperty("type")
	private String type;
	@JsonProperty("link_arptype")
	private String link_arptype;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIface() {
		return iface;
	}

	public void setIface(String iface) {
		this.iface = iface;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLink_type() {
		return link_type;
	}

	public void setLink_type(String link_type) {
		this.link_type = link_type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLink_arptype() {
		return link_arptype;
	}

	public void setLink_arptype(String link_arptype) {
		this.link_arptype = link_arptype;
	}
}
