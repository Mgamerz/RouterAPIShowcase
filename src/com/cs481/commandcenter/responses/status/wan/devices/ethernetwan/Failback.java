
package com.cs481.commandcenter.responses.status.wan.devices.ethernetwan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Failback{
   	private boolean immediate;
   	private String mode;
   	private Number rate_bandwidth;
   	private Number rate_time;
   	private Number time;

 	public boolean getImmediate(){
		return this.immediate;
	}
	public void setImmediate(boolean immediate){
		this.immediate = immediate;
	}
 	public String getMode(){
		return this.mode;
	}
	public void setMode(String mode){
		this.mode = mode;
	}
 	public Number getRate_bandwidth(){
		return this.rate_bandwidth;
	}
	public void setRate_bandwidth(Number rate_bandwidth){
		this.rate_bandwidth = rate_bandwidth;
	}
 	public Number getRate_time(){
		return this.rate_time;
	}
	public void setRate_time(Number rate_time){
		this.rate_time = rate_time;
	}
 	public Number getTime(){
		return this.time;
	}
	public void setTime(Number time){
		this.time = time;
	}
}
