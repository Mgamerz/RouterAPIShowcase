
package com.cs481.commandcenter.responses.config.wlan;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Wps{
   	private Number mode;
   	private boolean pbc;
   	private String pin;
   	private Number state;

 	public Number getMode(){
		return this.mode;
	}
	public void setMode(Number mode){
		this.mode = mode;
	}
 	public boolean getPbc(){
		return this.pbc;
	}
	public void setPbc(boolean pbc){
		this.pbc = pbc;
	}
 	public String getPin(){
		return this.pin;
	}
	public void setPin(String pin){
		this.pin = pin;
	}
 	public Number getState(){
		return this.state;
	}
	public void setState(Number state){
		this.state = state;
	}
}
