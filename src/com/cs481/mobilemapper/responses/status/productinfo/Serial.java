
package com.cs481.mobilemapper.responses.status.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Serial{
	@JsonProperty("status")
   	private String status;

 	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	}
}
