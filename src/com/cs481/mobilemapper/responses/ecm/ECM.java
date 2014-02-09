package com.cs481.mobilemapper.responses.ecm;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ECM {
	@JsonProperty("data")
	private ArrayList<Object> data;

	@JsonProperty("success")
	private transient boolean success;

	public ArrayList<Object> getData() {
		return this.data;
	}

	public void setData(ArrayList<Object> data) {
		this.data = data;
	}

	public boolean getSuccess() {
		return this.success;
	}

	@JsonIgnore
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
