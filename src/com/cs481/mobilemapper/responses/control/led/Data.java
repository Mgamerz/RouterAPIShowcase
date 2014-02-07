
package com.cs481.mobilemapper.responses.control.led;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Data{
	@JsonProperty("reset_leds")
   	private boolean reset_leds;

 	public boolean getReset_leds(){
		return this.reset_leds;
	}
	public void setReset_leds(boolean reset_leds){
		this.reset_leds = reset_leds;
	}

	@JsonIgnore
	private transient String Exception;
	
	@JsonProperty("exception")
	public void setException(String exception) {
		this.Exception = exception;
	}
	
	@JsonIgnore
	public String getException() {
		return Exception;
	}
	
	@JsonIgnore
	private transient String Key;
	
	@JsonIgnore
	public String getKey() {
		return Key;
	}

	@JsonProperty("key")
	public void setKey(String key) {
		Key = key;
	}
}
