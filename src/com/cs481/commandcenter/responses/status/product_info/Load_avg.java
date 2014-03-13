
package com.cs481.commandcenter.responses.status.product_info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Load_avg{
	@JsonProperty("15min")
   	private int min15;
	
	@JsonProperty("1min")
   	private int min1;
	
	@JsonProperty("5min")
   	private int min5;

 	public int getmin15(){
		return this.min15;
	}
	public void setmin15(int min15){
		this.min15 = min15;
	}
 	public int getmin1(){
		return this.min1;
	}
	public void setmin1(int min1){
		this.min1 = min1;
	}
 	public int getmin5(){
		return this.min5;
	}
	public void setmin5(int min5){
		this.min5 = min5;
	}
}
