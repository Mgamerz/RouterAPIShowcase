
package com.cs481.mobilemapper.responses.status.wlan;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Radio{
	@JsonProperty("channel")
   	private Number channel;
	
	@JsonProperty("channel_contention")
   	private Number channel_contention;
	
	@JsonProperty("channel_locked")
   	private boolean channel_locked;
	
	@JsonProperty("clients")
   	private ArrayList<Client> clients;
	
	@JsonProperty("survey")
   	private ArrayList<Survey> survey;
	
	@JsonProperty("txpower")
   	private Number txpower;

 	public Number getChannel(){
		return this.channel;
	}
	public void setChannel(Number channel){
		this.channel = channel;
	}
 	public Number getChannel_contention(){
		return this.channel_contention;
	}
	public void setChannel_contention(Number channel_contention){
		this.channel_contention = channel_contention;
	}
 	public boolean getChannel_locked(){
		return this.channel_locked;
	}
	public void setChannel_locked(boolean channel_locked){
		this.channel_locked = channel_locked;
	}
 	public ArrayList<Client> getClients(){
		return this.clients;
	}
	public void setClients(ArrayList<Client> clients){
		this.clients = clients;
	}
 	public ArrayList<Survey> getSurvey(){
		return this.survey;
	}
	public void setSurvey(ArrayList<Survey> survey){
		this.survey = survey;
	}
 	public Number getTxpower(){
		return this.txpower;
	}
	public void setTxpower(Number txpower){
		this.txpower = txpower;
	}
}
