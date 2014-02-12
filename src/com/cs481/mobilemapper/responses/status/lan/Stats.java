
package com.cs481.mobilemapper.responses.status.lan;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stats{
	@JsonProperty("collisions")
   	private Number collisions;
	
	@JsonProperty("idrops")
   	private Number idrops;
	
	@JsonProperty("ierrors")
   	private Number ierrors;
	
	@JsonProperty("in")
   	private Number in;
	
	@JsonProperty("ipackets")
   	private Number ipackets;
	
	@JsonProperty("multicast")
   	private Number multicast;
	
	@JsonProperty("odrops")
   	private Number odrops;
	
	@JsonProperty("oerrors")
   	private Number oerrors;
	
	@JsonProperty("opackets")
   	private Number opackets;
	
	@JsonProperty("out")
   	private Number out;

 	public Number getCollisions(){
		return this.collisions;
	}
	public void setCollisions(Number collisions){
		this.collisions = collisions;
	}
 	public Number getIdrops(){
		return this.idrops;
	}
	public void setIdrops(Number idrops){
		this.idrops = idrops;
	}
 	public Number getIerrors(){
		return this.ierrors;
	}
	public void setIerrors(Number ierrors){
		this.ierrors = ierrors;
	}
 	public Number getIn(){
		return this.in;
	}
	public void setIn(Number in){
		this.in = in;
	}
 	public Number getIpackets(){
		return this.ipackets;
	}
	public void setIpackets(Number ipackets){
		this.ipackets = ipackets;
	}
 	public Number getMulticast(){
		return this.multicast;
	}
	public void setMulticast(Number multicast){
		this.multicast = multicast;
	}
 	public Number getOdrops(){
		return this.odrops;
	}
	public void setOdrops(Number odrops){
		this.odrops = odrops;
	}
 	public Number getOerrors(){
		return this.oerrors;
	}
	public void setOerrors(Number oerrors){
		this.oerrors = oerrors;
	}
 	public Number getOpackets(){
		return this.opackets;
	}
	public void setOpackets(Number opackets){
		this.opackets = opackets;
	}
 	public Number getOut(){
		return this.out;
	}
	public void setOut(Number out){
		this.out = out;
	}
}
