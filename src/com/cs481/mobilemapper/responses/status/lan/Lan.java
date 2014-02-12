
package com.cs481.mobilemapper.responses.status.lan;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Lan{
	@JsonProperty("data")
   	private Data data;
	
	@JsonProperty("success")
   	private boolean success;

 	public Data getData(){
		return this.data;
	}
	public void setData(Data data){
		this.data = data;
	}
 	public boolean getSuccess(){
		return this.success;
	}
	public void setSuccess(boolean success){
		this.success = success;
	}
}
