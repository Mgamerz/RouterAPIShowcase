/* Generated by JavaFromJSON */
/*http://javafromjson.dashingrocket.com*/

package com.cs481.mobilemapper.responses.control.gpio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GPIO {
	@JsonProperty("success")
	private transient java.lang.Boolean success;

 	public void setSuccess(java.lang.Boolean success) {
		this.success = success;
	}

 	@JsonIgnore
	public java.lang.Boolean getSuccess() {
		return success;
	}

	@JsonProperty("data")
	private Data data;

 	public void setData(Data data) {
		this.data = data;
	}

	public Data getData() {
		return data;
	}
	
}