
package com.cs481.commandcenter.responses.status.lan;

import java.util.ArrayList;

import com.cs481.commandcenter.responses.status.lan.networks.Networks;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Lan{
	@JsonProperty("clients")
   	private ArrayList<Client> clients;
	
	@JsonProperty("devices")
   	private Devices devices;
	
	@JsonProperty("networks")
   	private Networks networks;
	
	@JsonProperty("stats")
   	private Stats stats;

 	public ArrayList<Client> getClients(){
		return this.clients;
	}
	public void setClients(ArrayList<Client> clients){
		this.clients = clients;
	}
 	public Devices getDevices(){
		return this.devices;
	}
	public void setDevices(Devices devices){
		this.devices = devices;
	}
 	public Networks getNetworks(){
		return this.networks;
	}
	public void setNetworks(Networks networks){
		this.networks = networks;
	}
 	public Stats getStats(){
		return this.stats;
	}
	public void setStats(Stats stats){
		this.stats = stats;
	}
}
