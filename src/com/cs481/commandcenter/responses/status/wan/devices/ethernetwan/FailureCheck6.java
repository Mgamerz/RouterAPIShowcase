
package com.cs481.commandcenter.responses.status.wan.devices.ethernetwan;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class FailureCheck6{
   	private Number idle_seconds;
   	private String mode;

 	public Number getIdle_seconds(){
		return this.idle_seconds;
	}
	public void setIdle_seconds(Number idle_seconds){
		this.idle_seconds = idle_seconds;
	}
 	public String getMode(){
		return this.mode;
	}
	public void setMode(String mode){
		this.mode = mode;
	}
}
