
package com.cs481.mobilemapper.responses.status.lan;

import com.cs481.mobilemapper.responses.RootElement;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Lan extends RootElement{
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
