package com.cs481.commandcenter.responses.status.lan.networks;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Info {
	
	@JsonProperty("uid")
	private String uid;
	
	@JsonProperty("ip6_addresses")
	private ArrayList<String> ip6_addresses;
	
	@JsonProperty("ip_address")
	private String ip_address;
	
	@JsonProperty("ip_addresses")
	private ArrayList<String> ip_addresses;
	
	@JsonProperty("hostname")
	private String hostname;
	
	@JsonProperty("broadcast")
	private String broadcast;
	
	@JsonProperty("netmask")
	private String netmask;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("name")
	private String name;
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public ArrayList<String> getIp6_addresses() {
		return ip6_addresses;
	}

	public void setIp6_addresses(ArrayList<String> ip6_addresses) {
		this.ip6_addresses = ip6_addresses;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public ArrayList<String> getIp_addresses() {
		return ip_addresses;
	}

	public void setIp_addresses(ArrayList<String> ip_addresses) {
		this.ip_addresses = ip_addresses;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getBroadcast() {
		return broadcast;
	}

	public void setBroadcast(String broadcast) {
		this.broadcast = broadcast;
	}

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
