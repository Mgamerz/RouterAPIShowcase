
package com.cs481.commandcenter.responses.ecm.routers;

import java.util.ArrayList;

public class Routers{
   	private ArrayList<Router> data;
   	private Meta meta;
   	private boolean success;

 	public ArrayList<Router> getData(){
		return this.data;
	}
	public void setData(ArrayList<Router> data){
		this.data = data;
	}
 	public Meta getMeta(){
		return this.meta;
	}
	public void setMeta(Meta meta){
		this.meta = meta;
	}
 	public boolean getSuccess(){
		return this.success;
	}
	public void setSuccess(boolean success){
		this.success = success;
	}
}
