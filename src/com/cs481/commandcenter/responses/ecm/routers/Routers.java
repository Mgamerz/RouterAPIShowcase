package com.cs481.commandcenter.responses.ecm.routers;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Routers {
	private ArrayList<Router> data;
	private Meta meta;
	private boolean success;

	public ArrayList<Router> getData() {
		return this.data;
	}

	public void setData(ArrayList<Router> data) {
		this.data = data;
	}

	public Meta getMeta() {
		return this.meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public boolean getSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@JsonProperty("exception")
	private transient String exception;

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getException() {
		return exception;
	}

	@JsonProperty("message")
	private transient String message;

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@JsonProperty("path")
	private transient String path;

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	@JsonProperty("status_code")
	private transient int status_code;

	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}

	public int getStatus_code() {
		return status_code;
	}
}
