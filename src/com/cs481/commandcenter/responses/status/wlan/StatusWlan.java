
package com.cs481.commandcenter.responses.status.wlan;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusWlan{
	@JsonProperty("channels_2_4ghz_first")
   	private Number channels_2_4ghz_first;
	
	@JsonProperty("channels_2_4ghz_last")
   	private Number channels_2_4ghz_last;
	
   	@JsonProperty("channels_5ghz_first")
   	private Number channels_5ghz_first;
   	
   	@JsonProperty("channels_5ghz_last")
   	private Number channels_5ghz_last;
   	
   	@JsonProperty("clients")
   	private ArrayList<Client> clients;
   	
   	@JsonProperty("events")
   	private Events events;
   	
   	@JsonProperty("radio")
   	private ArrayList<Radio> radio;
   	
   	@JsonProperty("state")
   	private String state;
   	
   	@JsonProperty("wps")
   	private Wps wps;

 	public Number getChannels_2_4ghz_first(){
		return this.channels_2_4ghz_first;
	}
	public void setChannels_2_4ghz_first(Number channels_2_4ghz_first){
		this.channels_2_4ghz_first = channels_2_4ghz_first;
	}
 	public Number getChannels_2_4ghz_last(){
		return this.channels_2_4ghz_last;
	}
	public void setChannels_2_4ghz_last(Number channels_2_4ghz_last){
		this.channels_2_4ghz_last = channels_2_4ghz_last;
	}
 	public Number getChannels_5ghz_first(){
		return this.channels_5ghz_first;
	}
	public void setChannels_5ghz_first(Number channels_5ghz_first){
		this.channels_5ghz_first = channels_5ghz_first;
	}
 	public Number getChannels_5ghz_last(){
		return this.channels_5ghz_last;
	}
	public void setChannels_5ghz_last(Number channels_5ghz_last){
		this.channels_5ghz_last = channels_5ghz_last;
	}
 	public ArrayList<Client> getClients(){
		return this.clients;
	}
	public void setClients(ArrayList<Client> clients){
		this.clients = clients;
	}
 	public Events getEvents(){
		return this.events;
	}
	public void setEvents(Events events){
		this.events = events;
	}
 	public ArrayList<Radio> getRadio(){
		return this.radio;
	}
	public void setRadio(ArrayList<Radio> radio){
		this.radio = radio;
	}
 	public String getState(){
		return this.state;
	}
	public void setState(String state){
		this.state = state;
	}
 	public Wps getWps(){
		return this.wps;
	}
	public void setWps(Wps wps){
		this.wps = wps;
	}
	
	public String toString(){
		String str = "";
		str = "Wlan state: "+getState();
		return str;
	}
}
