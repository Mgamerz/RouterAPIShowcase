
package com.cs481.mobilemapper.responses.status.productinfo;

import java.util.List;

public class Cpu{
   	private Number nice;
   	private Number system;
   	private Number user;

 	public Number getNice(){
		return this.nice;
	}
	public void setNice(Number nice){
		this.nice = nice;
	}
 	public Number getSystem(){
		return this.system;
	}
	public void setSystem(Number system){
		this.system = system;
	}
 	public Number getUser(){
		return this.user;
	}
	public void setUser(Number user){
		this.user = user;
	}
}
