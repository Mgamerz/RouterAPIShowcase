
package com.cs481.mobilemapper.responses.config.wlan;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Config_Wlan{
	@JsonProperty("radio")
   	private ArrayList<Radio> radios;
	
	@JsonProperty("radius_verbosity")
   	private int radius_verbosity;
	
	@JsonProperty("wlan_verbosity")
   	private int wlan_verbosity;

 	public ArrayList<Radio> getRadios(){
		return this.radios;
	}
	public void setRadio(ArrayList<Radio> radios){
		this.radios = radios;
	}
 	public int getRadius_verbosity(){
		return this.radius_verbosity;
	}
	public void setRadius_verbosity(int radius_verbosity){
		this.radius_verbosity = radius_verbosity;
	}
 	public int getWlan_verbosity(){
		return this.wlan_verbosity;
	}
	public void setWlan_verbosity(int wlan_verbosity){
		this.wlan_verbosity = wlan_verbosity;
	}
}
