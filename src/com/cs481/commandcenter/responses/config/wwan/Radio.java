
package com.cs481.commandcenter.responses.config.wwan;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Radio{
	
	@JsonProperty("mode")
   	private String mode;
	
	@JsonProperty("profiles")
   	private ArrayList<WANProfile> WANProfiles;

 	public String getMode(){
		return this.mode;
	}
	public void setMode(String mode){
		this.mode = mode;
	}
 	public ArrayList<WANProfile> getProfiles(){
		return this.WANProfiles;
	}
	public void setProfiles(ArrayList<WANProfile> profiles){
		this.WANProfiles = profiles;
	}
}
