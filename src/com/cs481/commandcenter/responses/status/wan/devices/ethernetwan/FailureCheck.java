
package com.cs481.commandcenter.responses.status.wan.devices.ethernetwan;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class FailureCheck{
   	private String mode;

 	public String getMode(){
		return this.mode;
	}
	public void setMode(String mode){
		this.mode = mode;
	}
}
