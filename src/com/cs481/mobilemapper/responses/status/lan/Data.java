
package com.cs481.mobilemapper.responses.status.lan;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Data{
	@JsonProperty("clients")
   	private ArrayList<Client> clients;
	
	@JsonProperty("devices")
   	private Devices devices;
	
	@JsonProperty("networks")
   	private Network networks;
	
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
 	public Network getNetworks(){
		return this.networks;
	}
	public void setNetworks(Network networks){
		this.networks = networks;
	}
 	public Stats getStats(){
		return this.stats;
	}
	public void setStats(Stats stats){
		this.stats = stats;
	}
}
