package com.cs481.mobilemapper.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class defines a root element. It contains some code for id's and
 * exceptions in Json responses so we don't have to copy paste them. Top level
 * response objects should subclass this.
 * 
 * @author Mgamerz
 * 
 */
/**
 * @author mjperez
 *
 */
public class RootElement {
	@JsonProperty("id")
	private transient String id;

	@JsonIgnore
	public String getId() {
		return id;
	}

	@JsonIgnore
	public void setId(String id) {
		this.id = id;
	}
	
	@JsonProperty("reason")
	private transient String reason;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
}
