
package com.cs481.commandcenter.responses.status.lan;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Status{
	@JsonProperty("link_flags")
   	private ArrayList<String> link_flags;
   	
   	@JsonProperty("link_state")
   	private String link_state;

 	public ArrayList<String> getLink_flags(){
		return this.link_flags;
	}
	public void setLink_flags(ArrayList<String> link_flags){
		this.link_flags = link_flags;
	}
 	public String getLink_state(){
		return this.link_state;
	}
	public void setLink_state(String link_state){
		this.link_state = link_state;
	}
}
