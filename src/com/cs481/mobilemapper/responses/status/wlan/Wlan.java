package com.cs481.mobilemapper.responses.status.wlan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Wlan {
	@JsonProperty("data")
	private Data data;

	public Data getData() {
		return this.data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	//RootElement
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
	private transient boolean success;

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean getSuccess() {
		return success;
	}
}
