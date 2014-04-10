package com.cs481.commandcenter.responses;

/**
 * Object that is always returned on any request.
 * Data is casted to a class
 * @author Mike Perez
 */

public class Response {
	private Object data; // cast to what you need.
	private RootElement responseInfo;

	public Response(RootElement response, Object data) {
		// TODO Auto-generated constructor stub
		this.data = data;
		this.responseInfo = response;
	}
	
	public Response(){
		//empty constructor for jackson.
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public RootElement getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(RootElement responseInfo) {
		this.responseInfo = responseInfo;
	}
}
