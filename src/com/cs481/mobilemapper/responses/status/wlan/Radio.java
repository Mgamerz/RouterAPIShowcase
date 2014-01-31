
package com.cs481.mobilemapper.responses.status.wlan;

import java.util.List;

public class Radio{
   	private Number channel;
   	private Number channel_contention;
   	private boolean channel_locked;
   	private List clients;
   	private List survey;
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
 	public List getClients(){
		return this.clients;
	}
	public void setClients(List clients){
		this.clients = clients;
	}
 	public List getSurvey(){
		return this.survey;
	}
	public void setSurvey(List survey){
		this.survey = survey;
	}
 	public Number getTxpower(){
		return this.txpower;
	}
	public void setTxpower(Number txpower){
		this.txpower = txpower;
	}
}
