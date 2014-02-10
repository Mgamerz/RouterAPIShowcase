
package com.cs481.mobilemapper.responses.status.wlan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Client{
	@JsonProperty("aid")
   	private Number aid;
	
	@JsonProperty("bss")
   	private Number bss;
	
	@JsonProperty("bw")
   	private Number bw;
	
	@JsonProperty("mac")
   	private String mac;
	
	@JsonProperty("mcs")
   	private Number mcs;
	
	@JsonProperty("mode")
   	private Number mode;
	
	@JsonProperty("psmode")
   	private Number psmode;
	
	@JsonProperty("radio")
   	private Number radio;
	
	@JsonProperty("rssi0")
   	private Number rssi0;
	
	@JsonProperty("rssi1")
   	private Number rssi1;
	
	@JsonProperty("rssi2")
   	private Number rssi2;
	
	@JsonProperty("rxrate")
   	private Number rxrate;
	
	@JsonProperty("time")
   	private Number time;
	
	@JsonProperty("txrate")
   	private Number txrate;

 	public Number getAid(){
		return this.aid;
	}
	public void setAid(Number aid){
		this.aid = aid;
	}
 	public Number getBss(){
		return this.bss;
	}
	public void setBss(Number bss){
		this.bss = bss;
	}
 	public Number getBw(){
		return this.bw;
	}
	public void setBw(Number bw){
		this.bw = bw;
	}
 	public String getMac(){
		return this.mac;
	}
	public void setMac(String mac){
		this.mac = mac;
	}
 	public Number getMcs(){
		return this.mcs;
	}
	public void setMcs(Number mcs){
		this.mcs = mcs;
	}
 	public Number getMode(){
		return this.mode;
	}
	public void setMode(Number mode){
		this.mode = mode;
	}
 	public Number getPsmode(){
		return this.psmode;
	}
	public void setPsmode(Number psmode){
		this.psmode = psmode;
	}
 	public Number getRadio(){
		return this.radio;
	}
	public void setRadio(Number radio){
		this.radio = radio;
	}
 	public Number getRssi0(){
		return this.rssi0;
	}
	public void setRssi0(Number rssi0){
		this.rssi0 = rssi0;
	}
 	public Number getRssi1(){
		return this.rssi1;
	}
	public void setRssi1(Number rssi1){
		this.rssi1 = rssi1;
	}
 	public Number getRssi2(){
		return this.rssi2;
	}
	public void setRssi2(Number rssi2){
		this.rssi2 = rssi2;
	}
 	public Number getRxrate(){
		return this.rxrate;
	}
	public void setRxrate(Number rxrate){
		this.rxrate = rxrate;
	}
 	public Number getTime(){
		return this.time;
	}
	public void setTime(Number time){
		this.time = time;
	}
 	public Number getTxrate(){
		return this.txrate;
	}
	public void setTxrate(Number txrate){
		this.txrate = txrate;
	}
}



