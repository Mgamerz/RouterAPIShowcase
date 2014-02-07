
package com.cs481.mobilemapper.responses.control.led;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LED{
	@JsonProperty("data")
   	private Data data;
   	
	@JsonProperty("success")
   	private transient boolean success;

 	public Data getData(){
		return this.data;
	}
 	
	public void setData(Data data){
		this.data = data;
	}
 	public boolean getSuccess(){
		return this.success;
	}
 	
 	@JsonIgnore
	public void setSuccess(boolean success){
		this.success = success;
	}
}
