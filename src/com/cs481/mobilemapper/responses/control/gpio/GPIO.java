
package com.cs481.mobilemapper.responses.control.gpio;

import java.util.List;

public class GPIO{
   	private Data data;
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
