package com.cs481.mobilemapper.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class is only here so you can copy paste the code into 'root response' objects.
 * You can't sublcass this as annotations in Java are not inherited apparently.
 * 
 * @author Mgamerz
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

	@JsonProperty("success")
	private transient boolean success; //must be boolean not Boolean! Jackson doesn't know how to map to Boolean only false/true

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean getSuccess() {
		return success;
	}

}
