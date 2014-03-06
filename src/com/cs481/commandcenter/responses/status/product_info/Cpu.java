
package com.cs481.commandcenter.responses.status.product_info;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cpu{
	@JsonProperty("nice")
   	private int nice;
	
	@JsonProperty("system")
   	private int system;
   	
	@JsonProperty("user")
	private int user;

 	public int getNice(){
		return this.nice;
	}
	public void setNice(int nice){
		this.nice = nice;
	}
 	public int getSystem(){
		return this.system;
	}
	public void setSystem(int system){
		this.system = system;
	}
 	public int getUser(){
		return this.user;
	}
	public void setUser(int user){
		this.user = user;
	}
}
